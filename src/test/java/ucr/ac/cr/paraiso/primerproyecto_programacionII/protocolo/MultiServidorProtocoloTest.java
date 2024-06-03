package ucr.ac.cr.paraiso.primerproyecto_programacionII.protocolo;

import org.jdom2.JDOMException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.ClasificacionXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.PatronXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Clasificacion;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Patron;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MultiServidorProtocoloTest {

    private PatronXMLData patronXMLData;
    private ClasificacionXMLData clasificacionXMLData;

    @BeforeEach
    public void setUp() throws IOException, JDOMException {

        patronXMLData = new PatronXMLData("test_patrones.xml", new ClasificacionXMLData("test_clasificaciones.xml"));
        clasificacionXMLData = new ClasificacionXMLData("test_clasificaciones.xml");
    }

    @Test
    public void testAgregarYBuscarPatron() throws IOException, JDOMException {
        Patron nuevoPatron = new Patron("1", "Patron de prueba", "Contexto de prueba", "Problema de prueba",
                "Solucion de prueba", "Ejemplo de prueba", "1");

        // Añadir el nuevo patrón
        patronXMLData.insertarPatron(nuevoPatron);

        // Buscar el patrón recién añadido
        List<Patron> patrones = patronXMLData.obtenerPatrones();
        boolean encontrado = false;
        for (Patron patron : patrones) {
            if (patron.getIdPatron().equals(nuevoPatron.getIdPatron())) {
                encontrado = true;
                break;
            }
        }

        assertTrue(encontrado, "El patrón añadido no se encontró en la lista de patrones");

        Patron nuevoPatron2 = new Patron("2", "Patron de prueba 2", "Contexto de prueba 2", "Problema de prueba 2",
                "Solucion de prueba 2", "Ejemplo de prueba 2", "2");

        patronXMLData.insertarPatron(nuevoPatron2);


        boolean encontrado2 = false;
        for (Patron patron : patrones) {
            if (patron.getIdPatron().equals(nuevoPatron2.getIdPatron())) {
                encontrado2 = true;
                break;
            }
        }

        assertTrue(encontrado2, "El patrón añadido no se encontró en la lista de patrones");
    }

    @Test
    public void testModificarPatron() throws IOException, JDOMException {

        String idPatron = patronXMLData.generarNuevoIdPatron();

        // Crear un patrón de prueba
        Patron nuevoPatron = new Patron(idPatron, "Patron de prueba", "Contexto de prueba", "Problema de prueba",
                "Solucion de prueba", "Ejemplo de prueba", "1");

        // Añadir el patrón
        patronXMLData.insertarPatron(nuevoPatron);

        // Modificar el nombre del patrón
        nuevoPatron.setName("Nuevo nombre de patrón");
        patronXMLData.modificarPatron(nuevoPatron);

        // Obtener el patrón modificado
        List<Patron> patrones = patronXMLData.obtenerPatrones();
        Patron patronModificado = null;
        for (Patron patron : patrones) {
            if (patron.getIdPatron().equals(idPatron)) {
                patronModificado = patron;
                break;
            }
        }


        assertEquals("Nuevo nombre de patrón", patronModificado.getName(), "El nombre del patrón no se ha modificado correctamente");
    }

    @Test
    public void testAgregarYBuscarClasificacion() throws IOException {
        Clasificacion nuevaClasificacion = new Clasificacion("1", "Clasificacion de prueba");

        // Añadir la nueva clasificación
        clasificacionXMLData.insertarClasificacion(nuevaClasificacion);

        // Buscar la clasificación recién añadida
        List<Clasificacion> clasificaciones = clasificacionXMLData.obtenerClasificaciones();
        boolean encontrado = false;
        for (Clasificacion clasificacion : clasificaciones) {
            if (clasificacion.getIdClasificacion().equals(nuevaClasificacion.getIdClasificacion())) {
                encontrado = true;
                break;
            }
        }

        assertTrue(encontrado, "La clasificación añadida no se encontró en la lista de clasificaciones");
    }

    @Test
    public void testEliminarPatron() throws IOException, JDOMException {

        String idPatron = patronXMLData.generarNuevoIdPatron();

        // Crear un patrón de prueba
        Patron nuevoPatron = new Patron(idPatron, "Patron de prueba", "Contexto de prueba", "Problema de prueba",
                "Solucion de prueba", "Ejemplo de prueba", "1");

        // Añadir el patrón
        patronXMLData.insertarPatron(nuevoPatron);

        // Eliminar el patrón
        patronXMLData.eliminarPatron(idPatron);

        // Verificar que el patrón ha sido eliminado
        List<Patron> patrones = patronXMLData.obtenerPatrones();
        assertFalse(patrones.stream().anyMatch(patron -> patron.getIdPatron().equals(idPatron)), "El patrón no se ha eliminado correctamente");
    }

    @Test
    public void testBuscarClasificacion() throws IOException, JDOMException {

        String idClasificacion = "1";

        // Buscar la clasificación
        Clasificacion clasificacion = clasificacionXMLData.obtenerClasificacionPorId(idClasificacion);

        // Verificar que se ha encontrado la clasificación
        assertEquals(idClasificacion, clasificacion.getIdClasificacion(), "La clasificación no se ha encontrado correctamente");
    }

    @Test
    public void testModificarClasificacion() throws IOException, JDOMException {

        String idClasificacion = "1";
        String nuevoNombre = "Nuevo nombre de clasificación";

        // Obtener la clasificación existente
        Clasificacion clasificacion = clasificacionXMLData.obtenerClasificacionPorId(idClasificacion);

        // Verificar si la clasificación se ha encontrado correctamente
        assertNotNull(clasificacion, "La clasificación no se ha encontrado");

        // Modificar el nombre de la clasificación solo si se ha encontrado correctamente
        if (clasificacion != null) {
            clasificacion.setNameClasificacion(nuevoNombre);
            clasificacionXMLData.modificarClasificacion(clasificacion);

            // Verificar que el nombre de la clasificación se ha modificado correctamente
            Clasificacion clasificacionModificada = clasificacionXMLData.obtenerClasificacionPorId(idClasificacion);
            assertEquals(nuevoNombre, clasificacionModificada.getNameClasificacion(), "El nombre de la clasificación no se ha modificado correctamente");
        }
    }


    @Test
    public void testEliminarClasificacion() throws IOException, JDOMException {

        String idClasificacion = clasificacionXMLData.generarNuevoIdClasificacion();

        // Crear una clasificación de prueba
        Clasificacion nuevaClasificacion = new Clasificacion(idClasificacion, "Clasificacion de prueba");

        // Añadir la clasificación
        clasificacionXMLData.insertarClasificacion(nuevaClasificacion);

        // Eliminar la clasificación
        clasificacionXMLData.eliminarClasificacion(idClasificacion);

        // Verificar que la clasificación ha sido eliminada
        List<Clasificacion> clasificaciones = clasificacionXMLData.obtenerClasificaciones();
        assertFalse(clasificaciones.stream().anyMatch(clasificacion -> clasificacion.getIdClasificacion().equals(idClasificacion)), "La clasificación no se ha eliminado correctamente");
    }



    @Test
    public void testListaClasificacionesVaciaAlInicio() throws IOException, JDOMException {

        // Obtener las clasificaciones al inicio
        List<Clasificacion> clasificaciones = clasificacionXMLData.obtenerClasificaciones();

        // Verificar que la lista está vacía
        assertFalse(clasificaciones.isEmpty(), "La lista de clasificaciones no está vacía al inicio");
    }

    @Test
    public void testListaPatronesVaciaAlInicio() throws IOException, JDOMException {

        // Obtener los patrones al inicio
        List<Patron> patrones = patronXMLData.obtenerPatrones();

        // Verificar que la lista está vacía
        assertFalse(patrones.isEmpty(), "La lista de patrones no está vacía al inicio");
    }


    @Test
    public void testBuscarClasificacionInexistentePorId() throws IOException, JDOMException {

        String idClasificacionInexistente = "9999";

        // Buscar la clasificación inexistente
        Clasificacion clasificacionObtenida = clasificacionXMLData.obtenerClasificacionPorId(idClasificacionInexistente);

        // Verificar que la clasificación no se ha encontrado
        assertNull(clasificacionObtenida, "Se encontró una clasificación que no debería existir");
    }


}



