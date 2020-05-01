package View;

import client.ClientTest;
import model.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Login {
    private JPanel JPanel1;
    private JButton LoginButton;
    private JTextField textFieldAccount;
    private JPasswordField passwordField;
    private JButton Button_register;
    private JButton button_setport;
    private static JFrame jFramemod;
    private String username;
    private String password;
    private int port = 2333;
    private boolean login = true;
    private Socket socket;
    private boolean check;
    private String reason = "";
    private List<String> friends = new ArrayList<String>();

    public Login() {
        LoginButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                username = new String(textFieldAccount.getText());
                password = new String(passwordField.getPassword());
                try {

                    //open up the main window
                    socket = new Socket("localhost",port);
                    //send the login information to the server
                    sendLoginInfo(socket);
                    run();
//                  ClientTest.main(username, s);
                    if (check && reason == "" && friends != null) {
                        Main.RunMain(username, socket, friends);
                        jFramemod.dispose();
                    }
                    else if (!check && reason != null){
                        JOptionPane.showMessageDialog(null, reason, "Failed", JOptionPane.PLAIN_MESSAGE);
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null, "Please check the server and the port", "Failed", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });
        button_setport.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    port = Integer.parseInt(JOptionPane.showInputDialog(null,"Please input the port(Default 3306)：\n","2333"));
                }catch (Exception e1){

                }
            }
        });
        Button_register.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Register.runRegister();
            }
        });
    }
    /**
     * this constructor is used for terminal login.
     */
    public Login(String username, String password) throws IOException {
        this.username = username;
        this.password = password;
        socket = new Socket("localhost",port);
        sendLoginInfo(socket);
        run();
        if (check && reason == "" && friends != null) {
            ClientTest.main(username, socket, friends);
        }
    }

    /**
     * receiver the login message from the server.
     */
    public void run() {
        while (login) {
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                if (!message.getType().equals("loginResponse")) {
                    continue;
                }
                String[] content = message.getContent().split(" ");
                if (content[0].equals("disagree")) {
                    check = false;
                    reason += content[1];
                    login = false;
                }
                else if (content[0].equals("agree")) {
                    check = true;
                    for (String friend: content[1].split("\n")) {
                        friends.add(friend);
                    }
                    login = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param s
     * send the login information to the server
     */
    private void sendLoginInfo(Socket s) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
        Message message = new Message();
        message.setType("loginRequest");
        message.setSender(username);
        message.setTime(System.currentTimeMillis());
        message.setSender("server");
        String content = username + " " + password;
        message.setContent(content);
        oos.writeObject(message);
    }

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        JFrame frame = new JFrame("Login");
        jFramemod = frame;
        frame.setContentPane(new Login().JPanel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500,320));
        frame.setLocation(250,350);
        frame.pack();
        frame.setVisible(true);
    }
}
