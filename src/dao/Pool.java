/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 *
 * @author Leire
 */
public class Pool {

    private static Pool pool;
    private static ConnectionUsed conU;
    private static Deque<ConnectionUsed> connections = new ArrayDeque<ConnectionUsed>();

    private Pool() {
        connections = new ArrayDeque();
    }

    /**
     *
     * @return pool
     */
    public static Pool getPool() {
        if (pool == null) {
            pool = new Pool();
        }
        return pool;
    }

    /**
     *
     * @param con
     */
    public static void returnConnection(Connection con) {
        conU.setCon(con);
        connections.push(conU);
    }

    /**
     *
     * @return con
     */
    public static ConnectionUsed getConnection() {
        ConnectionUsed con = connections.pop();
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
            ConnectionUsed con = getConnection();
            con.getCon().close();
        }
    }
}
