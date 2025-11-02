package InterfazUsuario;

import java.util.List;

public interface InterfazUsuario {
    String pedirNombreUsuario();
    String pedirAccion();
    String pedirMovimiento();
    int pedirNumeroOpciones(String titulo, List<String> opciones);
    void mostrarMensaje(String mensaje);
}
