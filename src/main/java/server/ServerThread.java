package server;

import dao.Groupdao;
import dao.Userdao;
import model.Message;
import model.User;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServerThread implements Runnable{
    private ServerSocket serverSocket;
    private JLabel label;
    private static JTextArea textArea;
    private String username;
    private String password;
    private String ServerName;
    private static boolean isStart = true;
    private static JList list_users;
    private static JLabel Label_username;
    private static JTextArea textArea_state;
    private static JScrollPane online_users;

    public ServerThread(ServerSocket serverSocket,JLabel label,
                        JTextArea jtextArea,JList jList,
                        JLabel jLabel,String ServerName,
                        JTextArea textArea2_state, JScrollPane Online_users){
        this.serverSocket = serverSocket;
        this.label = label;
        textArea = jtextArea;
        this.ServerName = ServerName;
        list_users = jList;
        Label_username = jLabel;
        textArea_state = textArea2_state;
        online_users = Online_users;
    }

    public Message receiveLoginInfo(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Message message = (Message)ois.readObject();
        String[] content = message.getContent().split(" ");
        username = content[0];
        password = content[1];
        return message;
    }

    private Message responseLoginInfo(String username) {
        Message message = new Message();
        message.setType("loginResponse");
        message.setTime(System.currentTimeMillis());
        message.setSender("server");
        message.setGetter(username);
        return message;
    }

    private void sendMsg(Message message, Socket s) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
        oos.writeObject(message);
    }

    public void run() {
        while (isStart){
            try {
                Socket s = serverSocket.accept();
                Message message = receiveLoginInfo(s);
                if (message.getType().equals("loginRequest")) {
                    String databasePassword = Userdao.getPassword(username);
                    Integer status = Userdao.getStatus(username);
                    Message returnMessage = responseLoginInfo(username);
                    if (databasePassword.equals(password) && status == 0) {
                        //login allowed
                        Userdao.setStatusOn(username);
                        String content = "agree";

                        //set friendsList
                        List<String> friendsList= Userdao.getFriend(username);
                        String friends = "";
                        for (String str: friendsList) {
                            friends += str + "\n";
                        }
                        content += " " + friends;
                        returnMessage.setContent(content);

                        //create the serverReceiveThread for this username
                        ServerReciveThread serverReciveThread = new ServerReciveThread(username,s,label,textArea,textArea_state);
                        ServerCollection.add(username,serverReciveThread);
                        Thread t = new Thread(serverReciveThread, username);
                        t.start();
                        textArea_state.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"    User:"
                                +username+"("+ Userdao.getAccountByUserName(username) + ") is on the line！\n\r");
                    }
                    else if (!databasePassword.equals(password)){
                        //wrong username or password
                        String content = "disagree" + " " + "1";
                        returnMessage.setContent(content);
                    }
                    else {
                        //This username is login
                        String content = "disagree" + " " + "2";
                        returnMessage.setContent(content);
                    }
                    sendMsg(returnMessage, s);
                }
                setOnline();
                System.out.println("====" + ServerCollection.printUsers() + "----");

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }



    public static void closeServer(){
        isStart = false;
    }

    /**
     *Server update the number of the online users
     */
    public static void setOnline() throws SQLException {
        List<String> list = ServerCollection.getOnlineList();
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        for (String s: list) {
            listModel.addElement(s);
        }
        list_users.setModel(listModel);
        Label_username.setText("Number of online users:"+list.size());
    }
    /**
     * send messages to group
     */
//    public static void serverSendMsg(Message message){
//        try {
//            String[] onlines = ServerCollection.getOnline().split(" ");
//            for (String online:onlines) {
//                ObjectOutputStream oos = new ObjectOutputStream(ServerCollection.get(online).getSocket().getOutputStream());
//                oos.writeObject(message);
//            }
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }
//    }

    /**
     * send update messages to client to update the number of online users
     */
//    public static void sendOnlines() throws IOException {
//        Message message = new Message();
//        message.setContent(ServerCollection.getOnline());
//        message.setType("setOnline");
//        String[] strings = ServerCollection.getOnline().split(" ");
//        for (String str:strings) {
//            ServerReciveThread serverReciveThread = ServerCollection.get(str);
//            ObjectOutputStream oos = new ObjectOutputStream(serverReciveThread.getSocket().getOutputStream());
//            oos.writeObject(message);
//        }
//    }

    /**
     *sender send the message to the group but not include the sender
     */
    public static void sendmsgtoall(Message message) throws IOException, SQLException {
//        String[] onlines = ServerCollection.GetOnline().split(" ");
//        for (String online:onlines) {
//            if (online.equals(message.getSender())){
//                continue;
//            }
//            ServerReciveThread serverReciveThread = ServerCollection.get(online);
//            ObjectOutputStream oos = new ObjectOutputStream(serverReciveThread.getSocket().getOutputStream());
//            oos.writeObject(message);
//        }
        String[] users = Groupdao.getMembers(Groupdao.getgroupnumber(message.getGetter())).split(" ");
        for (String user:users) {
            if (user.equals(message.getSender())){
                continue;
            }
            ServerReciveThread serverReciveThread = ServerCollection.get(user);
            if (serverReciveThread==null){
                continue;
            }
            ObjectOutputStream oos = new ObjectOutputStream(serverReciveThread.getSocket().getOutputStream());
            oos.writeObject(message);
        }
    }
}
