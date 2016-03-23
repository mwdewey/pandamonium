package Table;

/**
 * Created by root on 3/23/16.
 */
public class TableObject {
    public String ip;
    public int port;
    public int sent;
    public int received;
    public int pSent;
    public int pReceived;
    public String host;
    public int ping;
    
    public TableObject(String ip, int port,int sent,int received,int pSent,int pReceived,String host,int ping){
        this.ip = ip;
        this.port = port;
        this.sent = sent;
        this.received = received;
        this.pSent = pSent;
        this.pReceived = pReceived;
        this.host = host;
        this.ping = ping;
    }
    

}
