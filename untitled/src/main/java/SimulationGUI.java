import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class SimulationGUI {
    public static void main(String[] args) {


        // Create the JFrame
        JFrame frame = new JFrame("Simulation Configuration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(8, 2));

        // Create the labels and text fields
        JLabel numQueuesLabel = new JLabel("Number of Queues:");
        JTextField numQueuesTextField = new JTextField();
        JLabel numClientsLabel = new JLabel("Number of Clients:");
        JTextField numClientsTextField = new JTextField();
        JLabel timeSimulationLabel = new JLabel("Time Simulation:");
        JTextField timeSimulationTextField = new JTextField();
        JLabel serviceTimeMinLabel = new JLabel("Minimum Service Time:");
        JTextField serviceTimeMinTextField = new JTextField();
        JLabel serviceTimeMaxLabel = new JLabel("Maximum Service Time:");
        JTextField serviceTimeMaxTextField = new JTextField();
        JLabel waitingTimeMinLabel = new JLabel("Minimum Waiting Time:");
        JTextField waitingTimeMinTextField = new JTextField();
        JLabel waitingTimeMaxLabel = new JLabel("Maximum Waiting Time:");
        JTextField waitingTimeMaxTextField = new JTextField();

        // Create the "Start" button
        JButton startButton = new JButton("Start Simulation");

        // Add the components to the frame
        frame.add(numQueuesLabel);
        frame.add(numQueuesTextField);
        frame.add(numClientsLabel);
        frame.add(numClientsTextField);
        frame.add(timeSimulationLabel);
        frame.add(timeSimulationTextField);
        frame.add(serviceTimeMinLabel);
        frame.add(serviceTimeMinTextField);
        frame.add(serviceTimeMaxLabel);
        frame.add(serviceTimeMaxTextField);
        frame.add(waitingTimeMinLabel);
        frame.add(waitingTimeMinTextField);
        frame.add(waitingTimeMaxLabel);
        frame.add(waitingTimeMaxTextField);
        frame.add(new JLabel()); // Empty label for spacing
        frame.add(startButton);

        // Create an ActionListener for the startButton
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the values from the text fields
                int numQueues = Integer.parseInt(numQueuesTextField.getText());
                int numClients = Integer.parseInt(numClientsTextField.getText());
                int timeSimulation = Integer.parseInt(timeSimulationTextField.getText());
                int serviceTimeMin = Integer.parseInt(serviceTimeMinTextField.getText());
                int serviceTimeMax = Integer.parseInt(serviceTimeMaxTextField.getText());
                int waitingTimeMin = Integer.parseInt(waitingTimeMinTextField.getText());
                int waitingTimeMax = Integer.parseInt(waitingTimeMaxTextField.getText());

                // Run the simulation
                runSimulation(numQueues, numClients, timeSimulation, serviceTimeMin, serviceTimeMax, waitingTimeMin, waitingTimeMax);
            }
        });

        // Set the frame visible
        frame.setVisible(true);
    }

    public static void runSimulation(int numQueues, int numClients, int timeSimulation, int serviceTimeMin, int serviceTimeMax, int waitingTimeMin, int waitingTimeMax) {

        Logger logger = Logger.getLogger("SimulationLog");
        try {
            FileHandler fh = new FileHandler("simulation.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }

        Queue<Client>[] queues = new Queue[numQueues];
        for (int i = 0; i < numQueues; i++) {
            queues[i] = new LinkedList<>();
        }
        Queue<Client> waitingRoom = new LinkedList<>();

        // Generate random clients
        Random random = new Random();
        for (int i = 1; i <= numClients; i++) {
            int serviceTime = random.nextInt(serviceTimeMax - serviceTimeMin + 1) + serviceTimeMin;
            int arrivalTime = random.nextInt(timeSimulation + 1);
            Client client = new Client(i, serviceTime, arrivalTime);
            waitingRoom.add(client);
        }

        // Simulation loop
        int totalWaitingTime = 0;
        int currentTime = 0; // Initialize the currentTime variable
        String logText = ""; // Initialize logText as an empty string
        while (!waitingRoom.isEmpty() || hasClientsInQueues(queues)) {
            logText = "Time: " + currentTime + "\n";
            System.out.println(logText);
            logger.info(logText);

            // Print status of each queue
            for (int i = 0; i < numQueues; i++) {
                Queue<Client> q = queues[i];
                logText = "Queue " + (i + 1) + ": ";
                if (q.isEmpty()) {
                    logText += "closed";
                } else {
                    logText += "open, Clients: ";
                    for (Client client : q) {
                        logText += client.getId() + " ";
                    }
                }
                System.out.println(logText);
                logger.info(logText);
            }

            // Add clients to the queues if their arrival time matches the current time
            for (Iterator<Client> iterator = waitingRoom.iterator(); iterator.hasNext(); ) {
                Client client = iterator.next();
                if (client.getArrivalTime() == currentTime) {
                    int shortestQueueIndex = getShortestQueueIndex(queues);
                    client.setQueueId(shortestQueueIndex);
                    client.setServiceStartTime(currentTime);
                    queues[shortestQueueIndex].add(client);
                    iterator.remove();
                    logText = "Client " + client.getId() + " (Arrival: " + client.getArrivalTime() + ", Service: " + client.getServiceTime() + ") placed in Queue " + (shortestQueueIndex + 1);
                    System.out.println(logText);
                    logger.info(logText);
                }
            }


            for (int i = 0; i < numQueues; i++) {
                Queue<Client> q = queues[i];
                Queue<Client> clientsToRemove = new LinkedList<>();

                for (Client client : q) {
                    int serviceFinishTime = client.getServiceStartTime() + client.getServiceTime();
                    if (serviceFinishTime <= currentTime) {
                        clientsToRemove.add(client);

                        // Remove the client from the queue
                        logText = "Client " + client.getId() + " (Arrival: " + client.getArrivalTime() + ", Service: " + client.getServiceTime() + ") finished service at time " + currentTime;
                        System.out.println(logText);
                        logger.info(logText);

                        // Calculate waiting time for the client
                        int waitingTime = currentTime - client.getArrivalTime();
                        totalWaitingTime += waitingTime;
                    }
                }


                // Remove the finished clients from the queue
                q.removeAll(clientsToRemove);
            }
            // Print clients still waiting in the waiting room
            logText = "Clients still waiting in the waiting room: ";
            for (Client waitingClient : waitingRoom) {
                logText += waitingClient.getId() + " ";
            }
            System.out.println(logText);
            logger.info(logText);

            currentTime++; // Increment the currentTime
        }


        // Print summary statistics
        int totalClientsServed = numClients - waitingRoom.size();
        for (Queue<Client> queue : queues) {
            for (Client client : queue) {
                totalWaitingTime += currentTime - client.getArrivalTime();
            }
        }
        // Calculate average waiting time
        double averageWaitingTime = (double) totalWaitingTime / totalClientsServed;

        // Add average waiting time to log and print it
        logText += "\nAverage waiting time: " + averageWaitingTime;
        System.out.println(logText);
        logger.info(logText);
    }


    private static boolean hasClientsInQueues(Queue<Client>[] queues) {
        for (Queue<Client> queue : queues) {
            if (!queue.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private static int getShortestQueueIndex(Queue<Client>[] queues) {
        int shortestQueueIndex = 0;
        int shortestQueueSize = queues[0].size();
        for (int i = 1; i < queues.length; i++) {
            int queueSize = queues[i].size();
            if (queueSize < shortestQueueSize) {
                shortestQueueIndex = i;
                shortestQueueSize = queueSize;
            }
        }
        return shortestQueueIndex;
    }

}
