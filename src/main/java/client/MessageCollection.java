package client;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used for storing the chatting history of a communication.
 */

public class MessageCollection {
    private Map<String, String> map = new HashMap<String, String>();
    private String owner;

    public MessageCollection(String owner) {
        this.owner = owner;
    }

    public boolean add(String username, String content) {
        if (map.containsKey(username)) {
            String history = map.get(username);
            history += content + "\n";
            map.put(username, history);
        }
        else {
            map.put(username, content + "\n");
        }
        return true;
    }

    public String showContent(String username) {
        return map.get(username);
    }
}
