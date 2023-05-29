public class Client {
    private final int id;
    private  int serviceTime;
    private int queueId;
    private int serviceStartTime;
    private int arrivalTime;

    public Client(int id, int serviceTime, int arrivalTime) {
        this.id = id;
        this.serviceTime = serviceTime;
        this.queueId = -1;
        this.serviceStartTime = -1;
        this.arrivalTime = arrivalTime;
    }

    public int getId() {
        return id;
    }

    public int getServiceTime() {
        return serviceTime;
    }



    public void setQueueId(int queueId) {
        this.queueId = queueId;
    }

    public int getServiceStartTime() {
        return serviceStartTime;
    }

    public void setServiceStartTime(int serviceStartTime) {
        this.serviceStartTime = serviceStartTime;
    }
    public void setServiceTime(int serviceStartTime) {
        this.serviceTime = serviceTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }


}
