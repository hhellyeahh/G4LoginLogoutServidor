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
 * @author Leire & Unai B & Zuli
 */
public class Pool {

    private static Pool pool;
    private static Stack<Connection> stack = new Stack();

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
        Connection con = null;
        if (stack.size() > 0) {
            con = stack.pop();
        } else {
            ConnectionOpenClose conOpCl = new ConnectionOpenClose();
            con = conOpCl.openConnection();
        }
        return con;
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
        Connection toCleanConnection = null;
        for (int i = 0; i <= stack.size(); i++) {
            toCleanConnection = stack.pop();
            toCleanConnection.close();
        }

    }
}
