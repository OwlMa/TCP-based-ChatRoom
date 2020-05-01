package client;

import dao.Userdao;
import model.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ClientTest {
    private String username;
    private Socket socket;
    private List<String> friendsList;

    public ClientTest(Socket socket, String username, List<String> friends) throws IOException {
        this.socket = socket;
        this.username = username;
        this.friendsList = friends;
//        sendLoginInfo(socket);
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

//    public void sendLoginInfo(Socket s) throws IOException {
//        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
//        Message message = new Message();
//        message.setContent(username);
//        oos.writeObject(message);
//    }

    public static void main(String username, Socket socket, List<String> friends) throws IOException {
        ClientTest clientTest = new ClientTest(socket, username, friends);
        Thread t = new Thread(new ClientReceiveThread(socket, username, friends));
        t.start();
//        AddFriend.add(socket, username, "admin", clientTest.getFriendsList());
        while (true) {
            clientTest.clientSendMessage(clientTest.setMessage());
        }
    }

}
