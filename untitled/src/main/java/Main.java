import java.io.IOException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
        private static final Logger logger = Logger.getLogger("SimulationLog");

        public static void runSimulation(int numQueues, int numClients, int timeSimulation, int serviceTimeMin, int serviceTimeMax, int waitingTimeMin, int waitingTimeMax) {

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
//                BlockingQueue<Client>[] queues = new BlockingQueue[numQueues];
//                for (int i = 0; i < numQueues; i++) {
//                        queues[i] = new LinkedBlockingQueue<>();
//                }


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
                int totalWaitingTime=0;
                int currentTime = 0; // Initialize the currentTime variable
                String logText;
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
                                        }
                                }

                                // Remove the finished clients from the queue
                                q.removeAll(clientsToRemove);
                        }
//                        for (int i = 0; i < numQueues; i++) {
//                                Queue<Client> q = queues[i];
//                                // Create a new QueueProcessor thread for each queue
//                                OrderProcessor processor = new OrderProcessor;
//                                Thread thread = new Thread(processor);
//                                thread.start();
//                        }

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
                double averageWaitingTime = (double) totalWaitingTime / totalClientsServed;
                logText = "\nSimulation Summary";
                logText += "\n------------------";
                logText += "\nTotal clients served: " + totalClientsServed;
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

        public static void main(String[] args) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter the number of queues: ");
                int numQueues = scanner.nextInt();

                System.out.println("Enter the number of clients: ");
                int numClients = scanner.nextInt();

                System.out.println("Enter the time simulation: ");
                int timeSimulation = scanner.nextInt();

                System.out.println("Enter the minimum service time: ");
                int serviceTimeMin = scanner.nextInt();

                System.out.println("Enter the maximum service time: ");
                int serviceTimeMax = scanner.nextInt();

                System.out.println("Enter the minimum waiting time: ");
                int waitingTimeMin = scanner.nextInt();

                System.out.println("Enter the maximum waiting time: ");
                int waitingTimeMax = scanner.nextInt();

                runSimulation(numQueues, numClients, timeSimulation, serviceTimeMin, serviceTimeMax, waitingTimeMin, waitingTimeMax);
        }
}
