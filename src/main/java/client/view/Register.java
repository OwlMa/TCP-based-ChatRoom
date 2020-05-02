package client.view;

import dao.Userdao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class Register {
    private JPanel JPanel1;
    private JTextField textField_username;
    private JTextField textField_email;
    private JTextField textField_pwd1;
    private JTextField textField_pwd2;
    private JButton Button;

    public Register() {
        Button.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String username = textField_username.getText();
                String pwd1 = new String(textField_pwd1.getText());
                String pwd2 = new String(textField_pwd2.getText());
                String email = textField_email.getText();
                if (username.length()<5){
                    JOptionPane.showMessageDialog(null, "username should more than 5 characters");
                    return;
                }
                if (!pwd1.equals(pwd2)){
                    JOptionPane.showMessageDialog(null, "two passwords not match");
                    return;
                }
                //TODO
                try {
                    //String status = Userdao.register(username,pwd1,email);
                    Integer status = Userdao.getStatus(username);
                    if(status != -1){
                        JOptionPane.showMessageDialog(null,"username:"+username+"+has existed!");
                    }else {
                        Userdao.register(username, pwd1, email);
                        JOptionPane.showMessageDialog(null,"register successfully\n\raccount:"+status+"\n\rpassword:"+pwd1+"\n\remail:"+email+"");
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
    public static void runRegister(){
        JFrame frame = new JFrame("register");
        frame.setContentPane(new Register().JPanel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(550,450));
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("R egister");
        frame.setContentPane(new Register().JPanel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(550,450));
        frame.pack();
        frame.setVisible(true);
    }
}
