package View;

import dao.Userdao;
import model.Message;
import server.ServerCollection;
import server.ServerThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerMain {
    private JPanel JPanle1;
    private JList list_users;
    private JButton Button_start;
    private JButton Button_stop;
    private JTextArea textArea1_msglist;
    private JTextField textField1_msgwrite;
    private JButton button_sendmsg;
    private JLabel Lable_usersnum;
    private JLabel Lable_serverisstart;
    private JTabbedPane tabbedPane1;
    private JTextArea textArea2_state;
    private JTextField textFiled_port;
    private JScrollPane online_users;
    private Socket s;
    private ServerSocket serverSocket;

    public ServerMain() {
        Button_start.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                try {
                    int port = Integer.parseInt(textFiled_port.getText());
                    serverSocket = new ServerSocket(port);
                    String account ="";
                    Thread t = new Thread(new ServerThread(serverSocket,Lable_usersnum,
                            textArea1_msglist,list_users,Lable_usersnum,
                            account,textArea2_state, online_users));
                    t.start();
                    Button_start.setEnabled(false);
                    textFiled_port.setEnabled(false);
                    Lable_serverisstart.setText("working");
                    textArea2_state.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"   The server is open!\n\r");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        });
        Button_stop.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                ServerThread.closeServer();
                try {
                    ServerThread.closeServer();
                    serverSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                Lable_serverisstart.setText("Server is stopped");
                Button_start.setEnabled(true);
            }
        });
//        button_sendmsg.addMouseListener(new MouseAdapter() {
//            public void mouseClicked(MouseEvent e) {
//                Message message = new Message();
//                message.setContent(textField1_msgwrite.getText());
//                message.setSender("System information");
//                message.setType("System");
//                Date date = new Date();
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                message.setTime(sdf.format(date));
//                ServerThread.serverSendMsg(message);
//                textArea1_msglist.append(message.getContent()+"\n\r");
//                textField1_msgwrite.setText("");
//            }
//        });
        /**
//         * kick off
//         */
//        list_users.addMouseListener(new MouseAdapter() {
//            public void mouseClicked(MouseEvent e) {
//                if(JOptionPane.showConfirmDialog(null,"Do you want to kick off the user: "+list_users.getSelectedValue())==0){
//                    try {
//                        String account = Userdao.getaccountbyusername(list_users.getSelectedValue().toString());
//                        ServerCollection.get(account).closeThread();
//                        ServerCollection.remove(account);
//                        Userdao.putlogin(account);
//                        textArea2_state.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"   "+list_users.getSelectedValue().toString()+"Has been kicked off by the server\n\r");
//                        ServerThread.setonlines(ServerCollection.GetOnline());
//                        //TODO
//                    } catch (SQLException e1) {
//                        e1.printStackTrace();
//                    } catch (IOException e1) {
//                        e1.printStackTrace();
//                    }
//                }
//            }
//        });
    }

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        JFrame frame = new JFrame("ServerMain");
        frame.setContentPane(new ServerMain().JPanle1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1000,600));
        frame.setLocation(800,250);
        frame.pack();
        frame.setVisible(true);
    }
}
