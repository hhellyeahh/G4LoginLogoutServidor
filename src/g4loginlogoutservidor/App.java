/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g4loginlogoutservidor;

import static java.lang.System.exit;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 *
 * @author unaiz
 */
public class App extends Thread {

    private static boolean on = true;
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {

        G4LoginLogoutServidor server = new G4LoginLogoutServidor();
        server.start();

        while (on) {
            LOGGER.info("ESCRIBA EXIT PARA CERRAR EL SERVER");
            if (new Scanner(System.in).next().trim().equalsIgnoreCase("EXIT")) {
                server.setServerOn(false);
                server.interrupt();
                on = false;
            }
        }
        LOGGER.info("Servidor cerrado");
        exit(0);

    }
}
