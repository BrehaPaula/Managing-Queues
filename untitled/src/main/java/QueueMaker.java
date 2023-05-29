import java.util.Comparator;
import java.util.LinkedList;


public class QueueMaker {

    private static LinkedList<Client> clients;
    private Thread dequeueThread;

    public QueueMaker() {
        clients = new LinkedList<>();
        dequeueThread = new Thread(new DequeueRunnable());
        dequeueThread.start();
        Thread updateThread = new Thread(this::update);
        updateThread.start();
    }



    public static boolean isEmpty() {
        return clients.isEmpty();
    }


    public void update() {
        while (true) {
            try {
                Thread.sleep(5000); // sleep for 5 seconds
            } catch (InterruptedException e) {
                return;
            }
            System.out.println("Current queue state:");
            for (Client client : clients) {
                System.out.println("Client " + client.getId() + " arrived at " + client.getArrivalTime());
            }
        }
    }

    private class DequeueRunnable implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (QueueMaker.this) {
                    while (isEmpty()) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                    clients.removeFirst();
                    notify();
                }
            }
        }
    }

    private static Client poll(QueueMaker queue) {
        return poll(queue);
    }
}

