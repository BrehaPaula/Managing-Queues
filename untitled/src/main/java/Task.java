public class Task {
    private final int arrivalTime;
    private int processingTime;
    private int startTime;
    private int finishTime;

    public Task(int arrivalTime, int processingTime) {
        this.arrivalTime = arrivalTime;
        this.processingTime = processingTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    public int getTotalTime() {
        return finishTime - arrivalTime;
    }

    public boolean isEmpty() {
        return processingTime == 0;
    }

    public Task remove() {
        processingTime--;
        return this;
    }
    public synchronized Task getTask() {
        Task task = null;
        try {
            while (task.isEmpty()) {
                wait();
            }
            task = task.remove();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return task;
    }

    public void setInProgress(boolean b) {
    }
}