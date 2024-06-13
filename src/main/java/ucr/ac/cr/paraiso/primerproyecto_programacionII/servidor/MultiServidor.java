package ucr.ac.cr.paraiso.primerproyecto_programacionII.servidor;

import org.jdom2.JDOMException;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.ClasificacionXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.PatronXMLData;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class MultiServidor {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        boolean escuchando = true;

        try {
            // Inicializa las clases XMLData según tu implementación
            ClasificacionXMLData clasificacionXMLData = new ClasificacionXMLData("clasificaciones.xml");
            PatronXMLData patronXMLData = new PatronXMLData("patrones.xml", clasificacionXMLData);

            serverSocket = new ServerSocket(9999, 0, InetAddress.getLocalHost());

            System.out.println("Servidor activo");
            while (escuchando) {
                new MultiServidorHilo(serverSocket.accept(), patronXMLData, clasificacionXMLData).start();
            }
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (JDOMException e) {
            throw new RuntimeException(e);
        }
    }
}
