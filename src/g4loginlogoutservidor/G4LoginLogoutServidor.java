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
import factories.FactoryServer;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author 2dam
 */
public class G4LoginLogoutServidor {

    static final int PUERTO = 5000;
    User loginUser = new User();
    Message msg = new Message();
    public G4LoginLogoutServidor() {
        try {

//RECIBO  
            System.out.println("Escucho por el puerto " + PUERTO);
            ServerSocket skServidor = new ServerSocket(PUERTO);
            Socket skCliente = skServidor.accept();
            InputStream delCliente = skCliente.getInputStream();
            ObjectInputStream flujo2 = new ObjectInputStream(delCliente);
            msg = (Message) flujo2.readObject();
            loginUser = msg.getUser();
            if (msg.getCallType().equals(msg.getCallType().LOGIN_REQUEST)) {
                LoginLogout serverLoginLogout = null;
                serverLoginLogout = FactoryServer.getLoginLogout();
              loginUser = serverLoginLogout.logIn(loginUser);
            }
            
            //ENVIO
            msg.setCallType(Type.OKAY_RESPONSE);
            msg.setUser(loginUser);
            
            if(loginUser == null){
                msg.setCallType(Type.REJECT_RESPONSE);
            }
            
            for (int numCli = 0; numCli < 1; numCli++) {

                OutputStream aux = skCliente.getOutputStream();
                ObjectOutputStream flujo = new ObjectOutputStream(aux);

                flujo.writeObject(msg);

                skCliente.close();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

    }

}
