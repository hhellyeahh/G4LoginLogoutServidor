/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g4loginlogoutservidor;

import classes.Message;
import classes.Type;
import dao.Pool;
import factories.FactoryServer;
import hilos.SocketConnectionThread;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author unaizGontzal
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
     * The run method will be on a infinte loop until a admin close the server,
     * it will take user petitions and create a separate thread from each one of them
     * until a maximum of 10 users, it there are more than 10 users petitions at the same time,
     * we will return a message to the client saying that the server cant handle more petitions
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
                    //incrementamos el numero de clientes 
                    addClient();
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
        } catch (SQLException ex) {
            Logger.getLogger(G4LoginLogoutServidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
/**
 * this method will set the server off when its called from the main 
 * @param serverRunning  a boolean to stop the server
 */
    public static void setServerOn(boolean serverRunning) {
        serverRunning = serverRunning;
    }

   
    /**
     * incrementa el numero de usuarios conectados, se sinroniza para que el integer se incremente en paralelo
     *
     */
    public static synchronized void addClient() {
        howMuchClients++;
    }


    /**
     *  drecrementa el numero de usuarios conectados, se sinroniza para que el integer baje en paralelo
     */
    public static synchronized void removeClient() {
        howMuchClients--;
    }

    /**
     *
     * @throws SQLException it throws a SQLException if the are any of them
     * @throws IOException every other excpetions will be handle by the IOException
     */
    public static void closeServer() throws SQLException, IOException {
        if (howMuchClients > 0) {
            Pool pool = Pool.getPool();
            pool.closePool();

        }
        skServidor.close(); //close the server sockets
    }

}
