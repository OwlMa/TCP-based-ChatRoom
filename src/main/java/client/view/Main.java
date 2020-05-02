package client.view;


import client.AddFriend;
import client.ClientReceiveThread;
import client.GroupMessageCollection;
import client.MessageCollection;
import model.Message;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

public class Main {
    private JPanel JPanel1;
    private JList list1;
    private JTextArea textArea_msglist;
    private JTextField textField_msgsend;
    private JButton button_send;
    private JLabel Lable_name;
    private JLabel Lable_usersnum;
    private JPanel JPanel_chatwindow;
    private JTabbedPane tabbedPane1;
    private JList list2;
    private JLabel Lable_username;
    private JLabel Lable_icon;
    private JPanel tp1;
    private JPanel tp2;
    private JTextField friend_name;
    private JButton button_add_friend;
    private static String username;
    private static Socket s;
    private static List<String> friendsList;
    private static List<String> groupsList;
    private static MessageCollection messageCollection;
    private static GroupMessageCollection groupMessageCollection;

    public Main() throws SQLException {

        //load the friends list into list1
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        for (String friend: friendsList) {
            listModel.addElement(friend);
        }
        list1.setModel(listModel);
        messageCollection = new MessageCollection(username);
        //load the groups list into the list2
        DefaultListModel<String> listModelGroup = new DefaultListModel<String>();
        for (String group: groupsList) {
            listModelGroup.addElement(group);
        }
        list2.setModel(listModelGroup);

        groupMessageCollection = new GroupMessageCollection(username);
        Thread t = new Thread(new ClientReceiveThread(s, username, textArea_msglist, list1, list2, friendsList, messageCollection, groupMessageCollection));
        t.start();

        button_send.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Message message = new Message();
                message.setContent(textField_msgsend.getText());
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                message.setTime(System.currentTimeMillis());
                try {
                    if (list1.getSelectedValue() == null && list2.getSelectedValue() != null) {
                        message.setType("group");
                        message.setGetter(list2.getSelectedValue().toString());
                        groupMessageCollection.add(list2.getSelectedValue().toString(),"\t\t\t\tMe:" + message.getContent() + "\n\r");
                    }
                    else if (list1.getSelectedValue() != null && list2.getSelectedValue() == null) {
                        message.setType("personal");
                        message.setGetter(list1.getSelectedValue().toString());
                        messageCollection.add(list1.getSelectedValue().toString(), "\t\t\t\tMe:" + message.getContent() + "\n\r");
//                        String content = ChatContentcollection.getContent(Userdao.getaccountbyusername(list1.getSelectedValue().toString()));
//                        content += "\t\t\t\tMe:" + message.getContent() + "\n\r";
                    } else {
                        textArea_msglist.append("please select a user or group！\n\r");
                        return;
                    }
                    message.setSender(username);
                } catch (Exception e1) {
                    textArea_msglist.append("please select a user to send message\n\r");
                    return;
                }
                try {
                    ClientReceiveThread.clientSendMessage(message, s);
                } catch (IOException e1) {
                    textArea_msglist.append("please connect to the server\n\r");
                    return;
                }
                textArea_msglist.append("\t\t\t\tMe:" + message.getContent() + "\n\r");
                textField_msgsend.setText("");
            }
        });

        button_add_friend.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String friend = friend_name.getText();
                if (friendsList.contains(friend)) {
                    JOptionPane.showMessageDialog(null, "This user has been your friend", "Failed", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                else {
                    try {
                        AddFriend.add(s, username, friend);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (list1.getSelectedValue()==null) {
                        return;
                    }
                    Lable_name.setText(list1.getSelectedValue().toString());
                    if (!list2.isSelectionEmpty()) {
                        list2.clearSelection();
                    }
                    String content = messageCollection.showContent(list1.getSelectedValue().toString());
                    textArea_msglist.setText(content);
                }
            }
        });

        list2.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()){
                    if (list2.getSelectedValue()==null) {
                        return;
                    }
                    Lable_name.setText(list2.getSelectedValue().toString());
                    if (!list1.isSelectionEmpty()) {
                        list1.clearSelection();
                    }
                    String content = groupMessageCollection.showContent(list2.getSelectedValue().toString());
                    textArea_msglist.setText(content);
                }
            }
        });
    }

    public static void RunMain(String userName, Socket socket, List<String> list, List<String> groups) throws SQLException {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("Chat Room    "+ userName);
        username = userName;
        friendsList = list;
        groupsList = groups;
        s = socket;
        frame.setContentPane(new Main().JPanel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1000,600));
        frame.setLocation(200,200);
        frame.pack();
        frame.setVisible(true);
    }

}
