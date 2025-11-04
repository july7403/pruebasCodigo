package InterfazUsuario;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class InterfazConsola implements InterfazUsuario{

    private final LineReader lectorEntrada;
    private final String prompt =">>> "; //despues lo saco

    public InterfazConsola () throws IOException {
        Terminal terminal = TerminalBuilder.builder().system(true).build();
        lectorEntrada = LineReaderBuilder.builder()
                .terminal(terminal)
                .build();
    }


    @Override
    public String pedirNombreUsuario() {
        this.mostrarMensaje("Ingrese nombre usuario:");
        this.mostrarMensaje(prompt);
        while(true){
            String input = lectorEntrada.readLine();
            if (input.isEmpty()){
                this.mostrarMensaje("Opcion Invalida. Intente nuevamente.");
            }else {
                return input;
            }
        }
    }

    @Override
    public String pedirAccion() {
        mostrarMensaje("\nAcciones disponibles: (mover, atacar, pasar)");
        while (true) {
            try {
                String input = lectorEntrada.readLine("Ingrese acción: ").trim().toLowerCase();

                if (input.equals("mover") || input.equals("atacar") || input.equals("pasar")) {
                    return input;
                }
                mostrarMensaje("Acción invalida. Intenta de nuevo.");
            } catch (UserInterruptException e) {
                mostrarMensaje("\nOperación cancelada por el usuario.");
                return null;
            } catch (Exception e) {
                mostrarMensaje("Error: " + e.getMessage());
            }
        }
    }

    @Override
    public String pedirMovimiento() {
        mostrarMensaje("\nIngresa coordenadas del movimiento o colocación (x,y):");
        while (true) {
            try {
                String input = lectorEntrada.readLine(">>> ").trim();

                if (input.equalsIgnoreCase("salir")) {
                    mostrarMensaje("Operación cancelada por el usuario.");
                    return null;
                }

                if (input.matches("\\d+\\s*,\\s*\\d+")) {
                    return input;
                }

                mostrarMensaje("Formato invalido. Use el formato x,y.");

            } catch (UserInterruptException e) {
                mostrarMensaje("\nOperación cancelada por el usuario.");
                return null;
            } catch (Exception e) {
                mostrarMensaje("Error: " + e.getMessage());
            }
        }
    }

    @Override
    public int pedirNumeroOpciones(String titulo, List<String> opciones) {
        this.mostrarMensaje(titulo);
        for ( int i = 0; i<opciones.size();i++){
            this.mostrarMensaje((i+1)+". "+opciones.get(i));
        }
        while(true){
            String input = lectorEntrada.readLine("Ingrese numero de la opcion deseada: ");
            try{
                int opcion = Integer.parseInt(input);
                if(opcion >= 1 && opcion <= opciones.size()){
                    return opcion-1;
                }
            }catch (NumberFormatException ignorara){}
            this.mostrarMensaje("Opcion Invalida. Intente nuevamente.");
        }
    }

    //Para pequeños mensajes de error o demas
    @Override
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    @Override
    public InputStream pedirRutaArchivo() {
        while (true){
            try {
                String rutaArchivo = lectorEntrada.readLine(prompt).trim();
                if (rutaArchivo.isEmpty()) {
                    System.err.println("ERROR: la ruta no puede ser vacia");
                    continue;
                }
                try {
                    Path ruta = Paths.get(rutaArchivo);
                    if (Files.exists(ruta) && Files.isRegularFile(ruta)) {
                        return new FileInputStream(ruta.toFile());
                    }
                } catch (Exception ignored) {

                }
                InputStream input = getClass().getClassLoader().getResourceAsStream(rutaArchivo);
                if (input != null) {
                    System.out.println("Archivo cargado");
                    return input;
                }
                System.err.println("❌ ERROR: Archivo no encontrado en la ruta de disco ni en el classpath.");
                System.out.println();
            }catch (org.jline.reader.UserInterruptException e){
                System.out.println("\nOperación cancelada.");
                return null;
            }catch (org.jline.reader.EndOfFileException e) {
                System.out.println("\nOperación cancelada por Ctrl+D.");
                return null;
            }
            catch (Exception e) {
                System.err.println("ERROR: error de I/O"+e.getMessage());
                System.out.println();
            }
        }
    }
}
