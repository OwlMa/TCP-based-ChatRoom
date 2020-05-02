package server;

import java.util.*;

public class ServerCollection {
    private static Map<String, ServerReceiveThread> map = new HashMap<String, ServerReceiveThread>();
    private static List<String> userList = new ArrayList<String>();

    public static void add(String username, ServerReceiveThread serverReceiveThread){
        map.put(username, serverReceiveThread);
        userList.add(username);
    }

    /**
     * get the serverReceiveThread by using the account name;
     * @param username
     * @return return the thread of the communication thread between the user and the server, which is the serverReceiveThread found by its username
     */
    public static ServerReceiveThread get(String username){
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

    public static boolean contains(String username) {
        if (map.containsKey(username) && userList.contains(username)) {
            return true;
        }
        return false;
    }
    /**
     * traverse the online users
     * @return the string contains all the online user
     */
    public static List<String> getOnlineList(){
        return userList;
    }

    public static String printUsers() {
        String users = "\n";
        for (String user: userList) {
            users += user + "\n";
        }
        return  users;
    }
}
