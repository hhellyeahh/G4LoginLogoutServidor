/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.ServerImplementation;
import classes.LoginLogout;
import classes.User;
import classes.UserPrivilege;
import classes.UserStatus;
import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author unaiz
 */
public class pruebaSQL {

    public static void main(String[] args) {

        LoginLogout uwu = new ServerImplementation();

        User userRegister = new User();
        userRegister.setId(null);
        userRegister.setLogin("Leire");
        userRegister.setPassword("abcd*1234");
        userRegister.setEmail("placeholder@gmail.com");
        userRegister.setFullName("Lorem ipsum tuki");
        userRegister.setStatus(UserStatus.ENABLE);
        userRegister.setPrivilege(UserPrivilege.USER);
        userRegister.setLastPasswordChange(new java.sql.Timestamp(new java.util.Date().getTime()));

        userRegister = uwu.signUp(userRegister);

        if (!(userRegister == null)) {
            User userLogin = new User();
            userLogin.setLogin(userRegister.getLogin());
            userLogin.setPassword(userRegister.getPassword());
            System.out.println(userLogin.getLogin());
            userLogin = uwu.logIn(userLogin);
            if (userLogin == null) {
                System.out.println("efe login");
            } else {
                System.out.println(userLogin.toString());
            }

        }
        else
            System.out.println("efe register");
    }

}
