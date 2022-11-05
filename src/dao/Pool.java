/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Stack;

/**
 *
 * @author Leire
 */
public class Pool {

    private Pool pool;
    private Stack connections;

    private Pool() {
        connections = new Stack();
    }

    /**
     *
     * @return pool
     */
    public Pool getPool() {
        if (pool == null) {
            pool = new Pool();
        }
        return pool;
    }

    /**
     *
     * @param con
     */
    public void returnConnection(Connection con) {
        connections.push(con);
    }

    /**
     *
     * @return con
     */
    public Connection getConnection() {
        Connection con = (Connection) connections.pop();
        return con;
    }

    /**
     *
     * @return the size of the pool
     */
    public int getConnectionSize() {
        return connections.size();
    }

    /**
     *
     * @throws SQLException
     */
    public void closePool() throws SQLException {
        for (int i = 0; i < connections.size(); i++) {
            Connection con = getConnection();
            con.close();
        }
    }
}
