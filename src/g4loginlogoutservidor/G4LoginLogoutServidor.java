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
import dao.ServerImplementation;
import exceptions.IncorrectLoginException;
import exceptions.ServerException;
import exceptions.UnknownModelTypeException;
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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 2dam
 */
public class G4LoginLogoutServidor extends Thread {

    private Integer PUERTO = 5000;
    private static Boolean serverRunning = true;
    private Integer MaxUsers = 10;
    private static ArrayList<SocketConnectionThread> actualConections = new ArrayList<>();

    /**
     * @param args the command line arguments
     */
    public void run() {

        Socket skCliente = null;
        try {

            ServerSocket skServidor = new ServerSocket(PUERTO);
            // BUCLE 
            while (serverRunning) {
                //Preguntar si no ha superado el limite
                if (actualConections.size() < MaxUsers) {
                    //Accept connection
                    skCliente = skServidor.accept();
                    //Crear hilo pasándole el Socket skCliente
                    SocketConnectionThread socketConnectionThread = new SocketConnectionThread(skCliente, FactoryServer.getLoginLogout());
                    //Añadimos user all array 
                    actualConections.add(socketConnectionThread);
                }else{
                               //aceptamos conection             
                    skCliente = skServidor.accept();
                    //devolvemos un error al cliente con que no se aceptan mas peticiones
                     ObjectOutputStream oos = new ObjectOutputStream(skCliente.getOutputStream());
                     Message msg = new Message(Type.MAX_USERS_EXCEPTION);
                     oos.writeObject(msg);
                     
                }
            }
            //  cuando se cierra el servidor se cierra
            skServidor.close();

        } catch (IOException ex) {
            Logger.getLogger(G4LoginLogoutServidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownModelTypeException ex) {
            Logger.getLogger(G4LoginLogoutServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
          public static void setServerOn(boolean serverRunning) {
            serverRunning = serverRunning;
    }

}
