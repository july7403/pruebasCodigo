package InterfazUsuario;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.*;

import java.io.IOException;
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
        return "";
    }

    @Override
    public String pedirMovimiento() {
        return "";
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
}
