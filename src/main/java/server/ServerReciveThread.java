package server;

import model.Message;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerReciveThread implements Runnable{
    private Socket s;
    private JTextArea textArea;
    private JLabel label;
    private String account;
    private JTextArea textArea_state;
    private static boolean isrun = true;

    public ServerReciveThread(String account,Socket socket,JLabel label,JTextArea textArea,JTextArea textArea2_state){
        this.account = account;
        this.label = label;
        this.textArea = textArea;
        this.s = socket;
        this.textArea_state = textArea2_state;
    }

    public Socket getSocket(){
        return s;
    }

    public void run() {
        while(isrun){
            try {
                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                Message message = (Message) ois.readObject();
                /**
                 * forward the message by its type(personal or group)
                 */
                if (message.getType().equals("group")){
                    ServerThread.sendmsgtoall(message);
                }else if (message.getType().equals("personal")){
                    ServerThread.sendmsgpersonal(message);
                }else {
                    textArea.append("failed when accessing the type of message!\n\r");
                    textArea_state.append("Error：accessing the type of message failed！\n\r");
                }

//                textArea.append(Userdao.getusernamebyaccount(message.getSender())+": to :"+message.getGetter()+": "+message.getContent()+"\n\r");

            } catch (IOException e) {
                textArea_state.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"    User:" +account+ "connection close！\n\r");
                ServerCollection.remove(account);
                try {
//                    Userdao.putlogin(account);
                    ServerThread.setonlines(ServerCollection.GetOnline());
//                    ServerThread.sendonlines();
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
        isrun = false;
        s.close();
    }
}
