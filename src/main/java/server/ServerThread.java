package server;

import dao.Groupdao;
import dao.Userdao;
import model.Message;

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
    private static boolean isStart = true;
    private static JList list_users;
    private static JLabel Label_username;
    private static JTextArea textArea_state;
    private static JScrollPane online_users;

    public ServerThread(ServerSocket serverSocket,JLabel label,
                        JTextArea jtextArea,JList jList,
                        JLabel jLabel,String username,
                        JTextArea textArea2_state, JScrollPane Online_users){
        this.serverSocket = serverSocket;
        this.label = label;
        textArea = jtextArea;
        this.username = username;
        list_users = jList;
        Label_username = jLabel;
        textArea_state = textArea2_state;
        online_users = Online_users;
    }

    public void receiveLoginInfo(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Message message = (Message)ois.readObject();
        username = message.getContent();
    }

    public void run() {
        while (isStart){
            try {
                Socket s = serverSocket.accept();
                receiveLoginInfo(s);
                ServerReciveThread serverReciveThread = new ServerReciveThread(username,s,label,textArea,textArea_state);
                ServerCollection.add(username,serverReciveThread);
                Thread t = new Thread(serverReciveThread, username);
                t.start();
                textArea_state.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"    User:"
                +username+"("+ Userdao.getAccountByUserName(username) + ") is on the line！\n\r");
                setOnline();
                System.out.println("====" + ServerCollection.printUsers() + "----");
//                sendonlines();

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
     *Server update the number of the online users itself
     */
    public static void setOnline() throws SQLException {
        String[] list = ServerCollection.getOnlineList().toArray(new String[1]);
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        for (String s: list) {
            listModel.addElement(s);
        }
        list_users = new JList<String>(listModel);
        Label_username.setText("Number of online users:"+list.length);
        online_users = new JScrollPane(list_users);
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
    /**
     * server send private message
     */
    public static void sendMsgPersonal(Message message) throws SQLException, IOException {
        ServerReciveThread serverReciveThread = ServerCollection.get(message.getGetter());
        try {
            ObjectOutputStream oos = new ObjectOutputStream(serverReciveThread.getSocket().getOutputStream());
            oos.writeObject(message);
        }catch (Exception e){
            textArea.append(message.getGetter() + "getter is off line now!\n\r");
        }
    }
}
