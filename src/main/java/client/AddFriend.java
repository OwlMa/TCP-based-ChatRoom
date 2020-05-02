package client;

import model.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * this class is used for adding friend
 */
public class AddFriend {
    public static boolean add(Socket socket, String username, String friendName) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        Message message = new Message();
        message.setType("addFriendRequest");
        message.setSender(username);
        message.setGetter("server");
        message.setContent(friendName);
        oos.writeObject(message);
        return true;
    }

}
