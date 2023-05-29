import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class simulationLogger {

    private FileWriter fileWriter;

    public simulationLogger(String fileName) {
        try {
            this.fileWriter = new FileWriter(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void log(String message) {
        try {
            fileWriter.write(message + "\n");
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logQueuesStatus(int currentTime, Queue<Client>[] queues) {
        log(String.format("Time: %d", currentTime));
        for (int i = 0; i < queues.length; i++) {
            Queue<Client> q = queues[i];
            int numClientsInQueue = q.size();
            if (numClientsInQueue == 0) {
                log(String.format("Queue %d: closed", i + 1));
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("Queue %d: (", i + 1));
                for (Client client : q) {
                    sb.append(client.getId());
                    if (q.size() > 0) {
                        sb.append(", ");
                    }
                }
                sb.append(")");
                log(sb.toString());
            }
        }
    }

    public void logClientFinishedService(Client client, int currentTime, int queueIndex) {
        log(String.format("Client %d finished service at time %d and left queue %d", client.getId(), currentTime, queueIndex + 1));
    }

    public void logClientsStillWaiting(Queue<Client> waitingRoom) {
        if (!waitingRoom.isEmpty()) {
            log("Clients still waiting:");
            for (Client client : waitingRoom) {
                log(String.format("Client %d, arrival time: %d, service time: %d", client.getId(), client.getArrivalTime(), client.getServiceTime()));
            }
        } else {
            log("All clients serviced.");
        }
    }

    public void logWaitingRoomStatus(Queue<Client> waitingRoom) {
        int numClientsInWaitingRoom = waitingRoom.size();
        if (numClientsInWaitingRoom == 0) {
            log("The waiting room is empty");
        } else {
            log("Waiting room:");
            for (Client client : waitingRoom) {
                log(Integer.toString(client.getId()));
            }
        }
    }

    public void close() {
        try {
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
