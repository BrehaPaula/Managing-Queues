//import java.util.Collection;
//import java.util.Collections;
//import java.util.concurrent.LinkedBlockingQueue;
//
//public class Server implements Runnable {
//    private final LinkedBlockingQueue<Client> clientsQueue;
//    private final int id;
//    private final int serviceTime;
//    private final int waitingPeriod;
//    private boolean running;
//
//    public Server(int id, int serviceTime, int waitingPeriod) {
//        this.clientsQueue = new LinkedBlockingQueue<>();
//        this.id = id;
//        this.serviceTime = serviceTime;
//        this.waitingPeriod = waitingPeriod;
//        this.running = true;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public int getServiceTime() {
//        return serviceTime;
//    }
//
//    public synchronized void addTask(Client client) {
//        clientsQueue.add(client);
//        notify();
//    }
//
//    public synchronized void stop() {
//        running = false;
//        notify();
//    }
//
//    @Override
//    public void run() {
//        while (running) {
//            synchronized (this) {
//                while (clientsQueue.isEmpty()) {
//                    try {
//                        wait();
//                    } catch (InterruptedException e) {
//                        return;
//                    }
//                }
//                Client client = clientsQueue.peek();
//                long waitingTime = System.currentTimeMillis() - client.getArrivalTime();
//                if (waitingTime < waitingPeriod) {
//                    try {
//                        wait(waitingPeriod - waitingTime);
//                    } catch (InterruptedException e) {
//                        return;
//                    }
//                }
//                client = clientsQueue.poll();
//            }
//            try {
//                Thread.sleep(serviceTime);
//            } catch (InterruptedException e) {
//                return;
//            }
//        }
//    }
//
//    public Queue getQueue() {
//        Queue queue = null;
//        return queue;
//    }
//
//    public synchronized Task getTask() {
//        Task task = null;
//        try {
//            while (task.isEmpty()) {
//                wait();
//            }
//            task = task.remove();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return task;
//    }
//    public Collection<? extends Task> getTasks() {
//        Collection<? extends Task> task = null;
//        synchronized (task) {
//            return Collections.unmodifiableCollection(task);
//        }
//    }
//
//    public boolean isEmpty() {
//        Server tasks = null;
//        return tasks.isEmpty();
//    }
//}
import java.util.Queue;

public class Server {

    private Queue<Client> clientsQueue;
    private Queue<Task> tasksQueue;
    private Object tasksLock = new Object();

    public Server(Queue<Client> clientsQueue, Queue<Task> tasksQueue) {
        this.clientsQueue = clientsQueue;
        this.tasksQueue = tasksQueue;
    }

    public synchronized boolean isEmpty() {
        return clientsQueue.isEmpty();
    }

    public synchronized Client getQueue() {
        return clientsQueue.poll();
    }

    public synchronized Task getTask() {
        Task task = tasksQueue.poll();
        if (task != null) {
            task.setInProgress(true);
        }
        return task;
    }

    public void addTask(Task task) {
        synchronized (tasksLock) {
            tasksQueue.add(task);
        }
    }

    public synchronized int getTasksCount() {
        return tasksQueue.size();
    }
}