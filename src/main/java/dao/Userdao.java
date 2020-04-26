package dao;

import db.DButil;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Userdao {
    /**
     * add an user
     * @param u
     * @return
     * @throws SQLException
     */
    public static int addUser(User u) throws SQLException {
        Connection conn = DButil.getConn();
        String sql = "INSERT INTO user(username,password,email) VALUES (?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, u.getUsername());
        ps.setString(2, u.getPassword());
        ps.setString(3, u.getEmail());
        return ps.executeUpdate();
    }
    /**
     * get the friends of a user
     */
    public static String getfriend(String account) throws SQLException {
        Connection con = DButil.getConn();
        String sql = "SELECT friend FROM user WHERE account = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1,account);
        ResultSet rs =  ps.executeQuery();
        System.out.println(1);
        if (rs.next()){
            return rs.getString("friend");
        }else{
            return "error";
        }

    }

    /**
     * get the groups of a user
     */
    public static String getgroup(String account) throws SQLException {
        Connection conn = DButil.getConn();
        String sql = "SELECT groups FROM user WHERE account = ? ";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,account);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            return rs.getString("groups");
        }else{
            return "error";
        }
    }

    /**
     * 传入用户对象对用户进行更新
     * @param u
     * @throws SQLException
     */
    public void updateUser(User u) throws SQLException {
        Connection conn = DButil.getConn();
        String sql = "UPDATE  user SET password=? WHERE username=?";
        //准备
        PreparedStatement ps = conn.prepareStatement(sql);
        //设置参数(对应于数据库的数据类型)
        ps.setString(2, u.getUsername());
        ps.setString(1, u.getPassword());
        //执行
        ps.execute();
    }

    /**
     * 通过username删除用户
     * @param username
     * @throws SQLException
     */
    public void deleteUser(String username) throws SQLException {
        Connection conn = DButil.getConn();
        String sql = "DELETE FROM user WHERE username = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ps.execute();
    }
    /**
     * 通过账号获取
     */
    public static String getusernamebyaccount(String account) throws SQLException {
        Connection conn = DButil.getConn();
        String sql = "SELECT username FROM user WHERE account = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,account);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("username");
        }else{
            return new String("error");
        }
    }
    /**
     * 通过用户名
     */
    public static String getaccountbyusername(String username) throws SQLException {
        Connection conn = DButil.getConn();
        String sql = "SELECT account FROM user WHERE username = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,username);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("account");
        }else{
            return new String("error");
        }
    }

    /**
     * 返回用户集合
     * @return
     * @throws SQLException
     */
    public List<User> query() throws SQLException {
        Connection conn = DButil.getConn();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM user");
        List<User> getlist = new ArrayList<User>();
        User u = null;
        while (rs.next()) {
            u = new User();
            u.setUsername(rs.getString("username"));
            u.setPassword(rs.getString("password"));
            getlist.add(u);
        }
        return getlist;
    }

    /**
     * 通过username获取用户
     * @param username
     * @return
     * @throws SQLException
     */
    public User get(String username) throws SQLException {
        Connection conn = DButil.getConn();
        String sql = "SELECT * FROM user WHERE username=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);

        ResultSet rs = ps.executeQuery();

        User u = new User();
        //不要忘记了加上rs.next
        while (rs.next()) {
            u.setUsername(rs.getString("username"));
            u.setPassword(rs.getString("password"));
        }
        return u;
    }

    /**
     * 用户登录
     * @param u
     * @return
     * @throws SQLException
     */
    public static int login(User u) throws SQLException {
        Connection conn = DButil.getConn();
        String sql = "SELECT  * FROM user WHERE account = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, u.getAccount());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String password = rs.getString("password");
            if (password.equals(u.getPassword())) {
                return 1;
            } else {
                return 0;
            }
        }
        return 0;
    }

    /**
     * 用户注册
     * @param username
     * @param password
     * @return
     * @throws SQLException
     */
    public static String Register(String username, String password,String email) throws SQLException {
        Connection conn = DButil.getConn();
        //判断用户名是否重复
        PreparedStatement ps1 = conn.prepareStatement("SELECT * FROM user WHERE username=?");
        ps1.setString(1, username);
        ResultSet rs = ps1.executeQuery();
        while (rs.next()) {
            return "error";
        }
        int ac = (int)((Math.random()*9+1)*100000);
        String account = Integer.toString(ac);
        while (!getusernamebyaccount(account).equals("error")){
            //账号已被注册
            ac = (int)((Math.random()*9+1)*100000);
            account = Integer.toString(ac);
        }
        //(account,username,password,email,friend,groups,islogin)
        String sql = "insert into user VALUES (?,?,?,?,?,?,0)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,account);
        ps.setString(2, username);
        ps.setString(3, password);
        ps.setString(4,email);
        ps.setString(5,"123456 ");
        ps.setString(6,"654321 ");
        ps.executeUpdate();
        return account;
    }

    /**
     * islogin
     */
    public static int islogin(String account) throws SQLException {
        Connection conn = DButil.getConn();
        String sql = "SELECT * FROM user WHERE account = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, account);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            return Integer.parseInt(rs.getString("islogin"));
        }else{
            return -1;
        }
    }

    public static void setlogin(String account) throws SQLException {
        Connection conn = DButil.getConn();
        String sql = "UPDATE user SET islogin=1 WHERE account = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, account);
        ps.execute();
    }

    public static void putlogin(String account) throws SQLException {
        Connection conn = DButil.getConn();
        String sql = "UPDATE user SET islogin=0 WHERE account = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, account);
        ps.execute();
    }
}