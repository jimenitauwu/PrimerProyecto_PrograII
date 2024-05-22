package ucr.ac.cr.paraiso.primerproyecto_programacionII.servidor;

import ucr.ac.cr.paraiso.primerproyecto_programacionII.cliente.Cliente;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.protocolo.MultiServidorProtocolo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class MultiServidorHilo extends Thread{

    private List<Cliente> clientes;
    private Socket socket;

    public MultiServidorHilo(Socket socket) {
        super("MultiServidorHilo");
        this.socket = socket;
    }

    public void run() {

        PrintWriter writer;
        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            MultiServidorProtocolo protocolo = new MultiServidorProtocolo();
            String salida = protocolo.procesarEntrada(null);
            writer.println(salida);
            String entrada = null;
            while ((entrada = reader.readLine()) != null) {
                salida = protocolo.procesarEntrada(entrada);
                writer.println(salida);
                if (salida.equals("Adios."))
                    break;
            }// while
            writer.close();
            reader.close();
            socket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }// run


//
//    public void InsertarInformacion(String nombre, String informacion) {
//
//    }
//
//    public String ConsultarInformacion(String nombre) {
//
//        return "Cliente no encontrado";
//    }
//
//    public void ModificarInformacion(String nombre, String nuevaInformacion) {
//
//    }
//
//    public String BuscarInformacion(String nombre) {
//        return ConsultarInformacion(nombre);
//    }
//
//    public void CrearInformacion(String nombre, String informacion) {
//        InsertarInformacion(nombre, informacion);
//    }
//
//    public void ActualizarInformacion(String nombre, String nuevaInformacion) {
//        ModificarInformacion(nombre, nuevaInformacion);
//    }
//
//    public List<Cliente> getClientes() {
//        return clientes;
//    }
//
//    public void setClientes(List<Cliente> clientes) {
//        this.clientes = clientes;
//    }

}
