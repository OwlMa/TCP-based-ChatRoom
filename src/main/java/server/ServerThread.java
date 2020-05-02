package server;

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
import java.util.List;

public class ServerThread implements Runnable{
    private ServerSocket serverSocket;
    private JLabel label;
    private static JTextArea textArea;
    private String username;
    private String password;
    private String serverName;
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
        this.serverName = ServerName;
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

    private Message responseMsg(String username, String type) {
        Message message = new Message();
        message.setType(type);
        message.setTime(System.currentTimeMillis());
        message.setSender(serverName);
        message.setGetter(username);
        return message;
    }

    private void sendMsg(Message message, Socket s) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
        oos.writeObject(message);
    }

    @Override
    public void run() {
        while (isStart){
            try {
                Socket s = serverSocket.accept();
                Message message = receiveLoginInfo(s);
                if (message.getType().equals("loginRequest")) {
                    String databasePassword = Userdao.getPassword(username);
                    Integer status = Userdao.getStatus(username);
                    Message returnMessage = responseMsg(username, "loginResponse");
                    if (databasePassword.equals(password) && status == 0) {
                        //login allowed
                        Userdao.setStatusOn(username);
                        String content = "agree";

                        //set friendsList
                        List<String> friendsList = Userdao.getFriend(username);
                        String friends = "";
                        for (String str: friendsList) {
                            friends += str + "\n";
                        }
                        content += " " + friends;
                        List<String> groupsList = Userdao.getGroup(username);
                        String groups = "";
                        for (String groupName: groupsList) {
                            groups += groupName + "\n";
                        }
                        content += " " + groups;
                        returnMessage.setContent(content);

                        //create the serverReceiveThread for this username
                        ServerReceiveThread serverReceiveThread = new ServerReceiveThread(username,s,label,textArea,textArea_state);
                        ServerCollection.add(username, serverReceiveThread);
                        Thread t = new Thread(serverReceiveThread, username);
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
     *sender send the message to the "world" group
     */
    public static void sendMsgToAll(Message message) throws IOException, SQLException {
        List<String> userList = ServerCollection.getOnlineList();
        for (String user: userList) {
            ServerReceiveThread serverReceiveThread = ServerCollection.get(user);
            serverReceiveThread.sendMsgPersonal(message);
        }
    }
}
