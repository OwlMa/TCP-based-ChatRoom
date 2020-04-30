package server;

import dao.Userdao;
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
    private String username;
    private JTextArea textArea_state;
    private static boolean isRun = true;

    public ServerReciveThread(String username,Socket socket,JLabel label,JTextArea textArea,JTextArea textArea2_state){
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
                 * forward the message by its type(personal or group)
                 */
                if (message.getType().equals("group")){
                    ServerThread.sendmsgtoall(message);
                }else if (message.getType().equals("personal")){
                    System.out.println(message.getContent());
                    ServerThread.sendMsgPersonal(message);
                }else {
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
}
