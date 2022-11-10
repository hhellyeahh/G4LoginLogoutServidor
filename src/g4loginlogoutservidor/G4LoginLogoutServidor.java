/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g4loginlogoutservidor;

import classes.Message;
import classes.Type;
import dao.Pool;
import exceptions.UnknownModelTypeException;
import factories.FactoryServer;
import hilos.SocketConnectionThread;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author unaiz
 *
 */
public class G4LoginLogoutServidor extends Thread {

    private final ResourceBundle configFile = ResourceBundle.getBundle("config.config");
    private final Integer PUERTO = Integer.parseInt(configFile.getString("PORT"));
    private final Integer MAXUSERS = Integer.parseInt(configFile.getString("MAXUSERS"));
    private static Boolean serverRunning = true;
    private static ServerSocket skServidor;
    protected static Integer howMuchClients = 0;

    /**
     *
     */
    public void run() {

        try {
            Socket skCliente = null;
            skServidor = new ServerSocket(PUERTO);
            // BUCLE 
            while (serverRunning) {
                //Preguntar si no ha superado el limite
                if (howMuchClients < MAXUSERS) {
                    //Accept connection
                    skCliente = skServidor.accept();
                    //Crear hilo pasándole el Socket skCliente
                    SocketConnectionThread socketConnectionThread = new SocketConnectionThread(skCliente, FactoryServer.getLoginLogout());
                    //Añadimos hilo all array 
                    addClient(socketConnectionThread);
                } else {
                    //aceptamos conection             
                    skCliente = skServidor.accept();
                    //devolvemos un error al cliente con que no se aceptan mas peticiones
                    ObjectOutputStream oos = new ObjectOutputStream(skCliente.getOutputStream());
                    Message msg = new Message(Type.MAX_USERS_EXCEPTION);
                    oos.writeObject(msg);

                }
            }
            //cuando se cierra el servidor se cierra
            closeServer();

        } catch (IOException ex) {
            Logger.getLogger(G4LoginLogoutServidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownModelTypeException ex) {
            Logger.getLogger(G4LoginLogoutServidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(G4LoginLogoutServidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void setServerOn(boolean serverRunning) {
        serverRunning = serverRunning;
    }

    //incrementa el numero de usuarios conectados, se sinroniza para que el integer se incremente en paralelo
    public static synchronized void addClient(SocketConnectionThread socketConnectionThread) {
        howMuchClients++;
    }

    //drecrementa el numero de usuarios conectados, se sinroniza para que el integer baje en paralelo
    public static synchronized void removeClient(SocketConnectionThread socketConnectionThread) {
        howMuchClients--;
    }

    /**
     *
     * @throws SQLException
     * @throws IOException
     */
    public static void closeServer() throws SQLException, IOException {
        if (howMuchClients > 0) {
            Pool pool = Pool.getPool();
            pool.closePool();

        }
        skServidor.close();
    }

}
