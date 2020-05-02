package server;

import dao.Userdao;
import model.Message;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

public class ServerReceiveThread implements Runnable{
    private Socket s;
    private JTextArea textArea;
    private JLabel label;
    private String username;
    private JTextArea textArea_state;
    private static boolean isRun = true;

    public ServerReceiveThread(String username, Socket socket, JLabel label, JTextArea textArea, JTextArea textArea2_state){
        this.username = username;
        this.label = label;
        this.textArea = textArea;
        this.s = socket;
        this.textArea_state = textArea2_state;
    }

    public Socket getSocket(){
        return s;
    }

    public void run() {
        while(isRun){
            try {
                System.out.println("start");
                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                Message message = (Message) ois.readObject();
                System.out.println(message.getContent());
                System.out.println("done");
                /**
                 * forward the message by its type(group, personal or addFriend)
                 */
                if (message.getType().equals("group")){
                    List<String> userList =  ServerCollection.getOnlineList();
                    for (String user: userList) {
                        if (user.equals(username)) {
                            continue;
                        }
                        ServerReceiveThread getterReceiveThread = ServerCollection.get(user);
                        getterReceiveThread.sendMsgPersonal(message);
                    }
                }
                else if (message.getType().equals("personal")){
                    if (ServerCollection.contains(message.getGetter())) {
                        ServerReceiveThread getterReceiveThread = ServerCollection.get(message.getGetter());
                        getterReceiveThread.sendMsgPersonal(message);
                    }
                }
                else if (message.getType().equals("addFriendRequest")) {
                    String friendName = message.getContent();
                    String from = message.getSender();
                    Message returnMessage = new Message();

                    returnMessage.setGetter(from);
                    returnMessage.setType("addFriendResponse");
                    returnMessage.setTime(System.currentTimeMillis());
                    returnMessage.setSender("server");

                    List<String> friends = Userdao.getFriend(friendName);
                    if (friends == null) {
                        returnMessage.setContent("disagree");
                        sendMsgPersonal(returnMessage);
                    }
                    else {
                        returnMessage.setContent(friendName);
                        friends.add(from);
                        Userdao.addFriend(friendName, friends);
                        List<String> l = Userdao.getFriend(from);
                        l.add(friendName);
                        Userdao.addFriend(from, l);
                        sendMsgPersonal(returnMessage);

                        //inform the friend as well
                        returnMessage.setGetter(friendName);
                        ServerReceiveThread friendServerThread = ServerCollection.get(friendName);
                        if (friendServerThread == null) {
                            textArea.append(friendName + " is not on the line right now!");
                            ServerCollection.remove(friendName);
                        }
                        else {
                            friendServerThread.sendMsgPersonal(returnMessage);
                        }
                    }
                }
                else {
                    textArea.append("failed when accessing the type of message!\n\r");
                    textArea_state.append("Error：accessing the type of message failed！\n\r");
                }

                textArea.append(username+": to :"+message.getGetter()+": "+message.getContent()+"\n\r");

            } catch (IOException e) {
//                textArea_state.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"    User: " +username+ " connection close！\n\r");
//                ServerCollection.remove(username);
                try {
                    Userdao.setStatusOn(username);
                    ServerThread.setOnline();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeThread() throws IOException {
        isRun = false;
        s.close();
    }

    /**
     * server send private message
     */
    public void sendMsgPersonal(Message message) throws SQLException, IOException {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            oos.writeObject(message);
        }catch (Exception e){
            textArea.append(message.getGetter() + "getter is off line now!\n\r");
        }
    }
}
