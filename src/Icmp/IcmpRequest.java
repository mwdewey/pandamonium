package Icmp;

public class IcmpRequest {

    private long startTime;
    private short id;
    private boolean isDone;
    private long latency;

    public IcmpRequest(short id){
        startTime = System.currentTimeMillis();
        this.id = id;
        isDone = false;
        latency = 0;
    }

    public void replyReceived(){
        if(!isDone) {
            latency = System.currentTimeMillis() - startTime;
            isDone = true;
        }
    }

    public long getLatency(){return latency;}

}
