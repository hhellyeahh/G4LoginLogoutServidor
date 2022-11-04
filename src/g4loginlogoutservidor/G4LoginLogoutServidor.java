/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g4loginlogoutservidor;

import classes.LoginLogout;
import classes.Message;
import classes.Type;
import classes.User;
import exceptions.IncorrectLoginException;
import exceptions.ServerException;
import exceptions.UserAlreadyExistExpection;
import factories.FactoryServer;
import hilos.SocketConnectionThread;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 2dam
 */
public class G4LoginLogoutServidor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int PUERTO = 5000;
        Socket skCliente = null;
        try {

            ServerSocket skServidor = new ServerSocket(PUERTO);
            // BUCLE 
            while (true) {
                //Accept connection
                skCliente = skServidor.accept();

                //Crear hilo pasándole el Socket skCliente
                SocketConnectionThread socketConnectionThread = new SocketConnectionThread(skCliente);
                //Iniciar hilo
                socketConnectionThread.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(G4LoginLogoutServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
