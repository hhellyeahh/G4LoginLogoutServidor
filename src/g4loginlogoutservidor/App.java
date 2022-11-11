/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g4loginlogoutservidor;

import java.util.Scanner;
import java.util.logging.Logger;

/**
 *
 * @author unaizGontzal
 */
public class App extends Thread {

    private static boolean on = true; //a static boolean that mantains the thread listening to a "EXIT"
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
/**
 * THE MAIN OF THE SERVER, ITS A THREAD THAT WILL LAUNCH THE SERVER AND MEANWHILE 
 * IT WILL REMAINS LISTENING TO THE ADMIN THAT LAUNCH THE SERVER 
 * TO WRITE EXIT IN CONSOLE TO CLOSE IT.
 * @param args 
 */
    public static void main(String[] args) {
  
        G4LoginLogoutServidor server = new G4LoginLogoutServidor(); //It createds the server
        server.start(); //start the server
        LOGGER.info("Servidor iniciado");

        while (on) {
            LOGGER.info("ESCRIBA EXIT PARA CERRAR EL SERVER");
            if (new Scanner(System.in).next().trim().equalsIgnoreCase("EXIT")) {
                server.setServerOn(false); //cambiamos la variable del servidor a false para que deje de escuchar peticiones de usuarios
                server.interrupt(); // interrumpe el hilo en caso de que el metodo anterior no haya sido capaz de terminarlo por su cuenta
                on = false; //terminamos el bucle que mantenia la escucha de teclado
                System.exit(0); //salimos de la app
            }
        }
        LOGGER.info("Servidor cerrado");
    }
}
