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
import java.util.Date;

public class ServerThread implements Runnable{
    private ServerSocket serverSocket;
    private JLabel label;
    private static JTextArea textArea;
    private String account;
    private static boolean isStart = true;
    private static JList list_users;
    private static JLabel Lable_usersnum;
    private static JTextArea textArea_state;

    public ServerThread(ServerSocket serverSocket,JLabel label,JTextArea jtextArea,JList jList,JLabel jLabel,String account,JTextArea textArea2_state){
        this.serverSocket = serverSocket;
        this.label = label;
        textArea = jtextArea;
        this.account = account;
        list_users = jList;
        Lable_usersnum  = jLabel;
        textArea_state = textArea2_state;
    }

    public void receiveLoginInfo(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Message message = (Message)ois.readObject();
        account = message.getContent();
    }

    public void run() {
        while (isStart){
            try {
                Socket s = serverSocket.accept();
                receiveLoginInfo(s);
                ServerReciveThread serverReciveThread = new ServerReciveThread(account,s,label,textArea,textArea_state);
                ServerCollection.add(account,serverReciveThread);
                Thread t = new Thread(serverReciveThread);
                t.start();
                textArea_state.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"    User:"
                +account+"("+ Userdao.getusernamebyaccount(account)+ ") is on the line！\n\r");

                setonlines(ServerCollection.GetOnline());
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
    public static void setonlines(String onlines) throws SQLException {
        String[] strings = onlines.split(" ");
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        for (String s:strings) {
            if (s.equals("")){
                break;
            }
            listModel.addElement(Userdao.getusernamebyaccount(s));
        }
        Lable_usersnum.setText("Number of online users:"+listModel.size());
        list_users.setModel(listModel);
    }
    /**
     * send messages to group
     */
    public static void serversendmsg(Message message){
        try {
            String[] onlines = ServerCollection.GetOnline().split(" ");
            for (String online:onlines) {
                ObjectOutputStream oos = new ObjectOutputStream(ServerCollection.get(online).getSocket().getOutputStream());
                oos.writeObject(message);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * send update messages to client to update the number of online users
     */
    public static void sendonlines() throws IOException {
        Message message = new Message();
        message.setContent(ServerCollection.GetOnline());
        message.setType("setonline");
        String[] strings = ServerCollection.GetOnline().split(" ");
        for (String str:strings) {
            ServerReciveThread serverReciveThread = ServerCollection.get(str);
            ObjectOutputStream oos = new ObjectOutputStream(serverReciveThread.getSocket().getOutputStream());
            oos.writeObject(message);
        }
    }

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
    public static void sendmsgpersonal(Message message) throws SQLException, IOException {
        ServerReciveThread serverReciveThread = ServerCollection.get(Userdao.getaccountbyusername(message.getGetter()));
        try {
            ObjectOutputStream oos = new ObjectOutputStream(serverReciveThread.getSocket().getOutputStream());
            oos.writeObject(message);
        }catch (Exception e){
            textArea.append(message.getGetter() + "getter is off line now!\n\r");
        }
    }
}
