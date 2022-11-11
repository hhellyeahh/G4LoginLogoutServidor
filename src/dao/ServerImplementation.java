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
 * @author ZuliUnaiB
 */
public class ServerImplementation implements LoginLogout {

    private Pool pool;
    private Connection con = null;
    private ConnectionOpenClose conOpCl;
    private PreparedStatement stmt;
    private PreparedStatement stmt2;

    private final String SEARCHUser = "SELECT * from retologinlogout.user where login = ? and userPassword = ?";
    private final String UserEXISTS = "SELECT * from retologinlogout.user where login = ?";
    private final String INSERTLogin = "{CALL insertLogin(?)}";
    private final String CREATEUserSQL = "{CALL createUser(?,?,?,?,?,?)}";

    private static final ResourceBundle CONFIG = ResourceBundle.getBundle("config.config");
    private static final int MAXIMUM_USERS = Integer.parseInt(CONFIG.getString("MAXUSERS"));

    /**
     * Constructor initializating the pool and the connection
     */
    public ServerImplementation() {
        this.pool = Pool.getPool();
        this.conOpCl = new ConnectionOpenClose();
    }

    /**
     *
     * @param user it recieves the user in order to make the select in the database
     * @return it returns the user with full information after the login is correct
     * @throws IncorrectLoginException this exception is throw if the login is incorrect
     * @throws ServerException this exception is throw if the are any problem with the server
     * @throws UnknownTypeException this exception will be catched if the type of the message is incorrect
     */
    @Override
    public User logIn(User user) throws IncorrectLoginException, ServerException, UnknownTypeException {

        ResultSet rs = null;
        User loginUser = user;

        con = pool.getConnection();

        try {

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
                throw new IncorrectLoginException("Incorrect login");
            }
            // una vez comprobado de que exista insertamos el login

            stmt2 = con.prepareCall(INSERTLogin);
            stmt2.setString(1, loginUser.getLogin());
            stmt2.execute();

            stmt2.close();
            stmt.close();

            pool.returnConnection(con); // returns the conection to the pool

        } catch (SQLException ex) {
            Logger.getLogger(ServerImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServerException(ex.getMessage());
        }

        return loginUser;
    }

    /**
     *
     * @param user it recieves the user in order to make the insert in the database
     * @return it returns the user after the register
     * @throws ServerException this exception is throw if the are any problem with the server
     * @throws UserAlreadyExistExpection this exception is catched if the user already exist in the database
     * @throws UnknownTypeException this exception will be catched if the type of the message is incorrect 
     */
    @Override
    public User signUp(User user) throws ServerException, UserAlreadyExistExpection, UnknownTypeException {
        ResultSet rs = null;
        User userRegister = user;

        // Abrimos la conexión
        con = pool.getConnection();
        try {
            // Comprobar que el usuario no existe
            stmt = con.prepareStatement(UserEXISTS);
            stmt.setString(1, userRegister.getLogin());
            rs = stmt.executeQuery();

            // Si existe sale por el error, si no ejecuta la procedura
            if (!rs.next()) {
                stmt = con.prepareCall(CREATEUserSQL);
                stmt.setString(1, userRegister.getLogin());
                stmt.setString(2, userRegister.getPassword());
                stmt.setString(3, userRegister.getEmail());
                stmt.setString(4, userRegister.getFullName());
                stmt.setInt(5, userRegister.getStatus().ordinal());
                stmt.setInt(6, userRegister.getPrivilege().ordinal());

                stmt.execute();

                stmt.close();

                pool.returnConnection(con);
            } else {
                throw new UserAlreadyExistExpection("User already exists");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServerImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServerException(ex.getMessage());
        }

        return userRegister;

    }
}
