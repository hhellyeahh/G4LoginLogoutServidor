/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factories;

import dao.ServerImplementation;
import classes.LoginLogout;
import exceptions.UnknownTypeException;

/**
 *
 * @author ZuluLeire
 */
public class FactoryServer {

    private static LoginLogout data;

    /**
     * Load the data variable, with the server implementation
     *
     * @return data LoginLogout returns the server implementation of the inteface 
     */
    public static LoginLogout getLoginLogout(){
        data = new ServerImplementation();
        return data;
    }
}
