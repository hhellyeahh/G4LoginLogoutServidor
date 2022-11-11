/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilos;

import classes.*;
import exceptions.*;
import g4loginlogoutservidor.G4LoginLogoutServidor;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author unaizGontzal
 *
 */
public class SocketConnectionThread extends Thread {

    private Socket skCliente;
    private ObjectInputStream inputStream = null;
    private ObjectOutputStream outputStream = null;
    private User user;
    private Message msg;
    private LoginLogout dao;

    public SocketConnectionThread() {
    }
    /**
     * 
     * @param skCLiente the sockect of the client that makes the petition
     * @param dao the interface implementation in order to be used
     */
    public SocketConnectionThread(Socket skCLiente, LoginLogout dao) {
        this.skCliente = skCLiente;
        this.dao = dao;
        this.start(); //starts the trhead
    }
    
    @Override
    /**
     * it will get the object from the client socket and interpretate the recieved
     * message type in order to make a login or a register
     */
    public void run() {
        try {
//RECIBO  
            //Recive object
            inputStream = new ObjectInputStream(skCliente.getInputStream());
            outputStream = new ObjectOutputStream(skCliente.getOutputStream());

            //Read message
            msg = (Message) inputStream.readObject();

            //Get user from message
            user = msg.getUser();

            //Interpretate the call type
            switch (msg.getCallType()) {
                case LOGIN_REQUEST:
                    //LLAMAR AL dao
                    user = dao.logIn(user);
                    break;
                case SIGNUP_REQUEST:
                    //LLAMAR AL dao
                    user = dao.signUp(user);
                    break;
            }

            //ENVIO
            //Write response message
            msg.setCallType(Type.OKAY_RESPONSE);
            msg.setUser(user);
            
        } catch (IncorrectLoginException e) { //if the are any error with the login the returned message will be this
            msg.setCallType(Type.INCORRECT_LOGIN_RESPONSE);
        } catch (UserAlreadyExistExpection e) { //if the user already exist the returned message will be this
            msg.setCallType(Type.USER_ALREADY_EXIST_RESPONE);
        } catch (Exception e) { //any other error it will be returned with a server error
            msg.setCallType(Type.SERVER_ERROR_RESPONSE);
        } finally {
            try {
                outputStream.writeObject(msg);
                outputStream.close();
                inputStream.close();
                G4LoginLogoutServidor.removeClient(); // the syncronized method of the server is static so it will revome this client from it
                skCliente.close();
            } catch (IOException ex) {
                Logger.getLogger(G4LoginLogoutServidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
/**
 * it will close all from this client 
 * @throws IOException 
 */
    public void close() throws IOException {
        if (!skCliente.isClosed()) {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            skCliente.close();
        }
    }
}
