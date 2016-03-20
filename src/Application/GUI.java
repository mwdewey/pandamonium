package Application;

import java.util.HashMap;
import java.util.Map;

public class GUI {

    public enum ID {
        MainPacketTable,
        MainTargetStatus,
        MainInterfaceStatus,
        PacketManager,
        DeviceManager
    }

    private static Map<ID,Object> components = new HashMap<>();

    public static Object getComponent(ID id) {
        return components.get(id);
    }

    public static void setComponent(ID id, Object obj){
        components.put(id,obj);
    }
}
