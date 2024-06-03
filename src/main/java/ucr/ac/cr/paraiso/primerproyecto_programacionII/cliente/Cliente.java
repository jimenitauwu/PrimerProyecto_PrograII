package ucr.ac.cr.paraiso.primerproyecto_programacionII.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente {
    public static void main(String[] args) throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        Socket echoSocket = null;
        PrintWriter writer = null;
        BufferedReader reader = null;
        BufferedReader lectorTeclado = null;

        try {
            echoSocket = new Socket(inetAddress, 9999);
            writer = new PrintWriter(echoSocket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            String entrada = reader.readLine();
            System.out.println("Servidor: " + entrada);
            String salida;
            lectorTeclado = new BufferedReader(new InputStreamReader(System.in));
            while ((salida = lectorTeclado.readLine()) != null) {
                writer.println(salida);
                entrada = reader.readLine(); // Lee lo que viene del servidor
                System.out.println("Servidor:" + entrada);
            }
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
                if (writer != null) writer.close();
                if (lectorTeclado != null) lectorTeclado.close();
                if (echoSocket != null) echoSocket.close();
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
    }
}



