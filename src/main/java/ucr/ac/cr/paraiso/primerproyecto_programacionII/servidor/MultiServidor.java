package ucr.ac.cr.paraiso.primerproyecto_programacionII.servidor;

import java.io.IOException;
import java.net.ServerSocket;

public class MultiServidor {

    public static void main(String[] args) {
        ServerSocket serverSocket = null; // Este socket espera por
        // una conexión entrante
        boolean escuchando = true;

        try {
            serverSocket = new ServerSocket(9999);
            System.out.println("Servidor activo");
            while(escuchando){
                MultiServidorHilo hilo = new MultiServidorHilo(serverSocket.accept());
                hilo.start();
            }
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }



    }

}
