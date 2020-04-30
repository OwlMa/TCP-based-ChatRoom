package View;

import client.ClientTest;
import dao.Userdao;
import model.Message;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;

public class Login{
    private JPanel JPanel1;
    private JButton LoginButton;
    private JTextField textFieldAccount;
    private JPasswordField passwordField;
    private JButton Button_register;
    private JButton button_setport;
    private static JFrame jFramemod;
    private String username;
    private String password;
    private Integer account;
    private int port = 2333;

    public Login() {
        LoginButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                username = new String(textFieldAccount.getText());
                password = new String(passwordField.getPassword());
                User u = new User(username, password);
                try {
                    if(Userdao.login(u) && Userdao.getStatus(username)==0){
//                        account = Userdao.getAccountByUserName(username);
                        //open up the main window
                        Socket s = new Socket("localhost",port);
                        //send the login information to the server
                        sendLoginInfo(s);
                        Userdao.setStatusOn(username);
//                        ClientTest.main(username, s);
                        Main.RunMain(username, s);
                        jFramemod.dispose();
                    }else if (Userdao.getStatus(username)==1){
                        JOptionPane.showMessageDialog(null, "User is login", "Failed", JOptionPane.PLAIN_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(null, "Incorrect username or password", "Failed", JOptionPane.PLAIN_MESSAGE);
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

    public void sendLoginInfo(Socket s) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
        Message message = new Message();
        message.setContent(username);
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
