/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import classes.LoginLogout;
import classes.User;
import classes.UserPrivilege;
import classes.UserStatus;
import exceptions.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 2dam
 */
public class ServerImplementation implements LoginLogout {

    private Pool pool;
    private Connection con = null;
    private ConnectionUsed conU;
    private PreparedStatement stmt;

    private ConnectionOpenClose conection = new ConnectionOpenClose();
    private final String SEARCHUser = "SELECT * from retologinlogout.user where login = ? and userPassword = ?";
    private final String createUserSQL = "{CALL createUser(?,?,?,?,?,?)}";

    private static final ResourceBundle CONFIG = ResourceBundle.getBundle("config.config");
    private static final int MAXIMUM_USERS = Integer.parseInt(CONFIG.getString("MAXUSERS"));

    //TODO
    public ServerImplementation() {
        pool.getPool();
        conection = new ConnectionOpenClose();
    }

    /**
     * INSERT INTO retologinlogout.USER VALUES( null, "zuliyaki", "abcd*1234",
     * "zuluagaunai@gmail.com" , "Unai Zuluaga Ruiz", "ENABLE", "ADMIN", now())
     */
    @Override
    public User logIn(User user) throws IncorrectLoginException, ServerException, UnknownTypeException {

        ResultSet rs = null;
        User loginUser = user;

        con = conection.openConnection();
        if (con == null) {
            System.out.println("tuki");
        }

        try {

            //TODO
            getPoolConnection();

            stmt = con.prepareStatement(SEARCHUser);
            stmt.setString(1, loginUser.getLogin());
            stmt.setString(2, loginUser.getPassword());
            rs = stmt.executeQuery();

            if (rs.next()) {
                loginUser.setId(rs.getInt("id"));
                loginUser.setEmail(rs.getString("email"));
                loginUser.setFullName(rs.getString("fullName"));
                loginUser.setLastPasswordChange(rs.getTimestamp("lastPasswordChange"));

                UserPrivilege userPrivilege = null;
                for (UserPrivilege a : UserPrivilege.values()) {
                    if (a.ordinal() == rs.getInt("userPrivilege")) {
                        userPrivilege = a;
                    }
                }
                loginUser.setPrivilege(userPrivilege);

                UserStatus userStatus = null;
                for (UserStatus a : UserStatus.values()) {
                    if (a.ordinal() == rs.getInt("userStatus")) {
                        userStatus = a;
                    }
                }
                loginUser.setStatus(userStatus);

            } else {
                throw new IncorrectLoginException("Login Incorrecto");
            }

            //TODO
            rs.close();
            stmt.close();
            releaseConnection();

        } catch (SQLException ex) {
            Logger.getLogger(ServerImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServerException(ex.getMessage());
        }

        return loginUser;
    }

    @Override
    public User signUp(User user) throws ServerException, UserAlreadyExistExpection, UnknownTypeException {
        User userRegister = user;

        // Abrimos la conexión
        con = conection.openConnection();
        try {
            //TODO 
            getPoolConnection();

            stmt = con.prepareCall(createUserSQL);
            stmt.setString(1, userRegister.getLogin());
            stmt.setString(2, userRegister.getPassword());
            stmt.setString(3, userRegister.getEmail());
            stmt.setString(4, userRegister.getFullName());
            stmt.setInt(5, userRegister.getStatus().ordinal());
            stmt.setInt(6, userRegister.getPrivilege().ordinal());

            stmt.execute();

            stmt.close();

            //TODO
            releaseConnection();
        } catch (SQLException ex) {
            Logger.getLogger(ServerImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServerException(ex.getMessage());
        }

        return userRegister;

    }

    //TODO
    public void releaseConnection() {
        pool.returnConnection(con);
        conU.setUsed(false);
    }

    //TODO
    public void getPoolConnection() throws SQLException {
        if (pool.getConnectionSize() < MAXIMUM_USERS) {
            con = conection.openConnection();
        } else {
            con = pool.getConnection().getCon();
        }
        conU.setUsed(true);
    }

}
