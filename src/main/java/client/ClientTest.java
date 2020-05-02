package client;

import model.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class ClientTest {
    private String username;
    private Socket socket;
    private List<String> friendsList;
    private List<String> groupsList;

    public ClientTest(Socket socket, String username, List<String> friends, List<String> groupsList) {
        this.socket = socket;
        this.username = username;
        this.friendsList = friends;
        this.groupsList = groupsList;
    }

    public List<String> getFriendsList() {
        return friendsList;
    }

    private void clientSendMessage(Message message) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(message);
    }
    private Message setMessage(){
        Message message = new Message();
        message.setType("personal");
        System.out.println("plz input:");
        Scanner input = new Scanner(System.in);
        message.setContent(input.next());
        message.setGetter(friendsList.get(1));
        message.setSender(username);
        message.setTime(System.currentTimeMillis());
        return message;
    }

    private Message setGroupMessage() {
        Message message = new Message();
        message.setType("group");
        System.out.println("plz input to group:");
        Scanner input = new Scanner(System.in);
        message.setContent(input.next());
        message.setGetter(groupsList.get(0));
        message.setSender(username);
        message.setTime(System.currentTimeMillis());
        return message;
    }


    public static void main(String username, Socket socket, List<String> friends, List<String> groups) throws IOException {
        ClientTest clientTest = new ClientTest(socket, username, friends, groups);
        Thread t = new Thread(new ClientReceiveThread(socket, username, friends));
        t.start();
        while (true) {
            Scanner input = new Scanner(System.in);
            if (input.next().equals("1")) {
                System.out.println("to admin:");
                clientTest.clientSendMessage(clientTest.setMessage());
            }
            else {
                System.out.println("to world:");
                clientTest.clientSendMessage(clientTest.setGroupMessage());
            }

        }
    }

}
