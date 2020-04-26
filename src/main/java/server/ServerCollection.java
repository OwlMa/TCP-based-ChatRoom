package server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ServerCollection {
    private static Map<String, ServerReciveThread> map = new HashMap<String, ServerReciveThread>();

    public static void add(String username, ServerReciveThread serverReciveThread){
        map.put(username, serverReciveThread);
    }

    /**
     * get the serverReceiveThread by using the account name;
     * @param Name user account
     * @return return the thread of the communication thread between the user and the server, which is the serverReceiveThread found by its username
     */
    public static ServerReciveThread get(String Name){
        return map.get(Name);
    }

    /**
     * delete the serverReceiveThread by its username
     * set the user's status offline
     * @param Name
     */
    public static void remove(String Name){
        map.remove(Name);
    }

    /**
     * traverse the online users
     * @return the string contains all the online user
     */
    public static String GetOnline(){
        String Online = "";
        Iterator<String> it  = map.keySet().iterator();
        while(it.hasNext()){
            Online += it.next().toString()+" ";
        }
        return Online;
    }
}
