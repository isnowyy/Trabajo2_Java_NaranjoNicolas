package productos.vista;

import java.util.List;
import java.util.Locale;
import productos.modelo.Producto;

/**
 * Presentación de los productos por consola.
 *
 * Separar el formato de la lógica mantiene al servicio libre de llamadas a
 * {@code System.out}: el servicio decide qué productos salen y esta clase
 * decide cómo se ven.
 */
public final class ConsolaProductos {

    /** Ancho de la columna del nombre, para que los precios queden alineados. */
    private static final int ANCHO_NOMBRE = 26;

    /** Clase de utilidad: no está pensada para instanciarse. */
    private ConsolaProductos() {
    }

    /**
     * Imprime una lista de productos bajo un título.
     *
     * @param titulo    encabezado del bloque
     * @param productos productos a mostrar; si está vacía se avisa
     */
    public static void mostrarLista(String titulo, List<Producto> productos) {
        System.out.println(titulo);
        System.out.println("-".repeat(titulo.length()));

        if (productos == null || productos.isEmpty()) {
            System.out.println("(no hay productos para mostrar)");
            return;
        }

        productos.forEach(producto -> System.out.printf(
                "  %-" + ANCHO_NOMBRE + "s %-14s %12s%n",
                producto.getNombre(),
                producto.getCategoria(),
                formatearPrecio(producto.getPrecio())));
    }

    /**
     * Da formato monetario a un precio.
     *
     * Se fija el {@link Locale} de forma explícita para que la salida sea la
     * misma en cualquier equipo, sin depender de la configuración regional.
     *
     * @param precio valor a formatear
     * @return el precio con separador de miles y dos decimales
     */
    public static String formatearPrecio(double precio) {
        return String.format(Locale.US, "$%,.2f", precio);
    }

    /** Imprime una línea en blanco como separador entre bloques. */
    public static void separador() {
        System.out.println();
    }
}
