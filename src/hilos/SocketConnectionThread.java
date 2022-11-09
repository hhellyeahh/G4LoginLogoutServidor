/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilos;

import classes.*;
import exceptions.*;
import factories.FactoryServer;
import g4loginlogoutservidor.G4LoginLogoutServidor;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author unaiz
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

    public SocketConnectionThread(Socket skCLiente, LoginLogout dao) {
        this.skCliente = skCLiente;
        this.dao = dao;
        this.start();
    }

    @Override
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
                    if (dao.logIn(user) == null) {
                        user = dao.signUp(user);
                    } else 
                        throw new UserAlreadyExistExpection();
                    break;
            }

            //ENVIO
            //Write response message
            msg.setCallType(Type.OKAY_RESPONSE);
            msg.setUser(user);

            /**
             * if (user == null) {
             * msg.setCallType(Type.INCORRECT_LOGIN_RESPONSE); }
             */
        } catch (IncorrectLoginException e) {
            msg.setCallType(Type.INCORRECT_LOGIN_RESPONSE);
        } catch (UserAlreadyExistExpection e) {
            msg.setCallType(Type.USER_ALREADY_EXIST_RESPONE);
        } catch (Exception e) {
            msg.setCallType(Type.SERVER_ERROR_RESPONSE);
        } finally {
            try {
                outputStream.writeObject(msg);
                outputStream.close();
                inputStream.close();
                G4LoginLogoutServidor.removeClient(this);
                skCliente.close();
            } catch (IOException ex) {
                Logger.getLogger(G4LoginLogoutServidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

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
