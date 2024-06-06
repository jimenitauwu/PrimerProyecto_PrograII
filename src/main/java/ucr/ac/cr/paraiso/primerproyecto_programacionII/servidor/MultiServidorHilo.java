package ucr.ac.cr.paraiso.primerproyecto_programacionII.servidor;


import org.jdom2.JDOMException;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.ClasificacionXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.PatronXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.protocolo.MultiServidorProtocolo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
            String salida = "";
            String entrada;
            StringBuilder temp = new StringBuilder();

            while ((entrada = reader.readLine()) != null) {
                if (!entrada.equalsIgnoreCase("incluir") && !entrada.equalsIgnoreCase("consultar")
                        && !entrada.equalsIgnoreCase("modificar") && !entrada.equalsIgnoreCase("eliminar")
                        && !entrada.equalsIgnoreCase("incluir_clasificacion")
                        && !entrada.equalsIgnoreCase("modificar_clasificacion")
                        && !entrada.equalsIgnoreCase("eliminar_clasificacion")
                        && !entrada.equalsIgnoreCase("consultar_patron_por_id")
                        && !entrada.equalsIgnoreCase("consultar_clasificacion_por_id")) {
                    temp.append(entrada);
                } else {
                    salida = protocolo.procesarEntrada(temp.toString(), entrada.toLowerCase());
                    writer.println(salida);
                    temp.setLength(0);
                }

                if (salida.equals("Adios.")) {
                    break;
                }
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


