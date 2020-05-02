package client;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used for storing the chatting history of a group.
 */
public class GroupMessageCollection {
    private Map<String, String> map = new HashMap<String, String>();
    private String owner;

    public GroupMessageCollection(String owner) {
        this.owner = owner;
    }
    public boolean add(String groupName, String content) {
        if (map.containsKey(groupName)) {
            String history = map.get(groupName);
            history += content + "\n";
            map.put(groupName, history);
        }
        else {
            map.put(groupName, content + "\n");
        }
        return true;
    }

    public String showContent(String groupName) {
        return map.get(groupName);
    }
}
