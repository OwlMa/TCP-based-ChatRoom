package client;

import dao.Userdao;
import model.Message;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ClientReceiveThread implements Runnable{
    private Socket socket;
    private String username;
    private List<String> friendsList;
    private boolean isRun = true;
    private JList list1;
    private JList list2;
    private static JTextArea textArea;

    public ClientReceiveThread(Socket socket, String username, JTextArea jtextArea, JList list1, JList list2) throws SQLException {
        this.socket = socket;
        this.username = username;
        textArea= jtextArea;
        this.list1 = list1;
        this.list2 = list2;
        friendsList = Userdao.getFriend(username);
        Integer status = Userdao.getStatus(username);
        if (status != 1) {
            close();
        }
    }

    public ClientReceiveThread(Socket socket, String username) throws SQLException {
        this.socket = socket;
        this.username = username;
        friendsList = Userdao.getFriend(username);
        Integer status = Userdao.getStatus(username);
        if (status != 1) {
            close();
        }
    }

    public void close() {
        this.isRun = false;
    }

    public void run() {
        while (isRun) {
//            Message message = null;
//            try {
//                message = setMessage();
//                System.out.println("To: " + message.getGetter() + "\n" + message.getContent() + "-----" + message.getTime());
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            try {
//                ClientReceiveThread.clientSendMessage(message, socket);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message)ois.readObject();
                if (message.getType().equals("personal")) {
                    String content = message.getContent();
                    System.out.println("From: " + message.getSender()
                            + "\n" + content + "-----"
                            + message.getTime());
                    textArea.setText(message.getContent());
                    System.out.println("sender: " + message.getSender());
                    list1.setSelectedValue(message.getSender(), true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void clientSendMessage(Message message, Socket socket) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(message);
    }

}
