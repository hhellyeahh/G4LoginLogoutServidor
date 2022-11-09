/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.ResourceBundle;
import java.util.Stack;

/**
 *
 * @author Leire & Unai
 */
public class Pool {

    private static Pool pool;
    private static Stack stack = new Stack();

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
        stack.push(con);
    }

    /**
     *
     * @return con
     */
    public static Connection getConnection() {
        return (Connection) stack.pop();
    }
    
    /**
     *
     * @return con
     */
    public static int getConnections() {
        return stack.size();
    }

    /**
     *
     * @throws SQLException
     */
    public void closePool() throws SQLException {
        stack.clear();
    }
}
