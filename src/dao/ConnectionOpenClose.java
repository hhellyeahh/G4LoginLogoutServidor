/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

/**
 *
 * @author zuli
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionOpenClose {

    protected ResourceBundle configFile;
    protected String url;
    protected String user;
    protected String pass;
    protected Connection con;

    /**
     * this method takes all the necessary info from the config file in order to create a conection
     */
    public ConnectionOpenClose() {
        configFile = ResourceBundle.getBundle("config.config");
        url = configFile.getString("URL");
        user = configFile.getString("USER");
        pass = configFile.getString("PASSWORD");
    }
/**
 * this methods creates a connection ussing the necessary info
 * @return returns a usable conections
 */
    public Connection openConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            Logger.getLogger(ConnectionOpenClose.class.getName()).log(Level.SEVERE, null, e);
        }
        return con;
    }
/**
 * this method is used for closing the coneection
 * @param stmt the msq stmt is recieved in order to close it
 * @param con the used coneection is recieved in order to close it
 * @throws SQLException 
 */
    public void closeConnection(PreparedStatement stmt, Connection con) throws SQLException {
        if (stmt != null) {
            stmt.close();
        }

        if (con != null) {
            con.close();
        }

    }
}
