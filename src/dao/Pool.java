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
 * @author ZuliLeireBonilla
 */
public class Pool {
    
    private static Pool pool; //static pool in order to only being one for class not from object.
    private static Stack<Connection> stack = new Stack(); // the same as the pool, we only want one for class.

    /**
     this method is used in order to get a pool and if this class doesnt have a pool yet it create it
     * @return pool it returns the pool
     */
    public static Pool getPool() {
        if (pool == null) {
            pool = new Pool();
        }
        return pool;
    }

    /**
     * this method saves the used conection to the stack of the pool
     * @param con it recieves the con after its used
     */
    public static void returnConnection(Connection con) {
        stack.push(con);
    }

    /**
     * this method takes a conection from the stack if there are any, if they are not it creates one from ConnectionOpenClose class.
     * @return con it returns the connection ready for use.
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
     * used to get the size of the stack in the pool
     * @return con it returns a Integer with the value of the size of the stack
     */
    public static int getConnections() {
        return stack.size();
    }

    /**
     * it takes out all the connections in the stack and it close all of them
     * @throws SQLException it throws a SQL exception if there is any problem clossing them
     */
    public void closePool() throws SQLException {
        Connection toCleanConnection = null;
        for (int i = 0; i <= stack.size(); i++) {
            toCleanConnection = stack.pop();
            toCleanConnection.close();
        }

    }
}
