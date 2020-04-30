package server;

import java.util.*;

public class ServerCollection {
    private static Map<String, ServerReciveThread> map = new HashMap<String, ServerReciveThread>();
    private static List<String> userList = new ArrayList<String>();

    public static void add(String username, ServerReciveThread serverReciveThread){
        map.put(username, serverReciveThread);
        userList.add(username);
    }

    /**
     * get the serverReceiveThread by using the account name;
     * @param username
     * @return return the thread of the communication thread between the user and the server, which is the serverReceiveThread found by its username
     */
    public static ServerReciveThread get(String username){
        return map.get(username);
    }

    /**
     * delete the serverReceiveThread by its username
     * set the user's status offline
     * @param username
     */
    public static void remove(String username){
        map.remove(username);
        userList.remove(username);
    }

    /**
     * traverse the online users
     * @return the string contains all the online user
     */
    public static List<String> getOnlineList(){
//        String online = "";
//        for (Map.Entry<String, ServerReciveThread> entry: map.entrySet()) {
//            online += entry.getKey() + " ";
//        }
//        System.out.println(online);
        return userList;
    }

    public static String printUsers() {
        String users = "";
        for (String user: userList) {
            users += user + "/n";
        }
        return  users;
    }
}
