package ucr.ac.cr.paraiso.primerproyecto_programacionII.servidor;

import ucr.ac.cr.paraiso.primerproyecto_programacionII.cliente.Cliente;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.ClasificacionXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.PatronXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.protocolo.MultiServidorProtocolo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class MultiServidorHilo extends Thread {
    private Socket socket;
    private PatronXMLData patronXMLData;
    private ClasificacionXMLData clasificacionXMLData;

    public MultiServidorHilo(Socket socket, PatronXMLData patronXMLData, ClasificacionXMLData clasificacionXMLData) {
        super("MultiServidorHilo");
        this.socket = socket;
        this.patronXMLData = patronXMLData;
        this.clasificacionXMLData = clasificacionXMLData;
    }

    public void run() {
        PrintWriter writer = null;
        BufferedReader reader = null;
        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            MultiServidorProtocolo protocolo = new MultiServidorProtocolo(patronXMLData, clasificacionXMLData);
            String salida = protocolo.procesarEntrada(null);
            writer.println(salida);
            String entrada;
            while ((entrada = reader.readLine()) != null) {
                salida = protocolo.procesarEntrada(entrada);
                writer.println(salida);
                if (salida.equals("Adios."))
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) writer.close();
                if (reader != null) reader.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


