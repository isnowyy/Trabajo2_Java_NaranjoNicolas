package productos.servicio;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import productos.modelo.Producto;

/**
 * Operaciones de consulta sobre una lista de productos.
 *
 * Tanto el filtrado como la ordenación se resuelven con expresiones lambda,
 * que es lo que pide el reto. Ninguna operación modifica la lista recibida:
 * todas devuelven una lista nueva, por lo que el catálogo original queda
 * intacto después de consultarlo.
 */
public final class ServicioProductos {

    /** Clase de utilidad: no está pensada para instanciarse. */
    private ServicioProductos() {
    }

    /**
     * Filtra los productos que pertenecen a una categoría.
     *
     * El criterio se expresa como una lambda asignada a un {@link Predicate}:
     * recibe un producto y responde si debe conservarse.
     *
     * @param productos lista a filtrar; si es nula se trata como vacía
     * @param categoria categoría buscada, sin distinguir mayúsculas de minúsculas
     * @return lista nueva con los productos de esa categoría; vacía si no hay coincidencias
     */
    public static List<Producto> filtrarPorCategoria(List<Producto> productos, String categoria) {
        if (productos == null || categoria == null || categoria.isBlank()) {
            return List.of();
        }

        // Expresión lambda que define el criterio de filtrado.
        Predicate<Producto> esDeLaCategoria = producto -> producto.perteneceA(categoria);

        return productos.stream()
                .filter(esDeLaCategoria)
                .collect(Collectors.toList());
    }

    /**
     * Ordena los productos de mayor a menor precio.
     *
     * El criterio se expresa como una lambda asignada a un {@link Comparator}:
     * compara el precio del segundo contra el del primero, que es lo que
     * invierte el orden natural y produce el descendente.
     *
     * Se usa {@code Double.compare} y no una resta entre precios porque la
     * resta de dos {@code double} devuelve un decimal que al convertirse a
     * {@code int} se trunca: una diferencia de 0.50 se volvería 0 y el
     * comparador trataría como iguales dos precios distintos.
     *
     * @param productos lista a ordenar; si es nula se trata como vacía
     * @return lista nueva ordenada de forma descendente por precio
     */
    public static List<Producto> ordenarPorPrecioDescendente(List<Producto> productos) {
        if (productos == null) {
            return List.of();
        }

        // Expresión lambda que define el criterio de ordenación.
        Comparator<Producto> porPrecioDescendente =
                (primero, segundo) -> Double.compare(segundo.getPrecio(), primero.getPrecio());

        return productos.stream()
                .sorted(porPrecioDescendente)
                .collect(Collectors.toList());
    }

    /**
     * Aplica el filtrado y la ordenación en una sola pasada.
     *
     * Es la operación que usa el programa principal: encadena las dos lambdas
     * sobre el mismo stream en lugar de recorrer la lista dos veces.
     *
     * @param productos lista de origen; si es nula se trata como vacía
     * @param categoria categoría buscada, sin distinguir mayúsculas de minúsculas
     * @return productos de la categoría, ordenados de mayor a menor precio
     */
    public static List<Producto> filtrarYOrdenar(List<Producto> productos, String categoria) {
        if (productos == null || categoria == null || categoria.isBlank()) {
            return List.of();
        }

        Predicate<Producto> esDeLaCategoria = producto -> producto.perteneceA(categoria);
        Comparator<Producto> porPrecioDescendente =
                (primero, segundo) -> Double.compare(segundo.getPrecio(), primero.getPrecio());

        return productos.stream()
                .filter(esDeLaCategoria)
                .sorted(porPrecioDescendente)
                .collect(Collectors.toList());
    }

    /**
     * Devuelve las categorías presentes en la lista, sin repetir y en orden
     * alfabético. Sirve para mostrarle al usuario qué puede consultar.
     *
     * @param productos lista de origen; si es nula se trata como vacía
     * @return categorías únicas ordenadas alfabéticamente
     */
    public static List<String> obtenerCategorias(List<Producto> productos) {
        if (productos == null) {
            return List.of();
        }

        return productos.stream()
                .map(producto -> producto.getCategoria())
                .distinct()
                .sorted((primera, segunda) -> primera.compareToIgnoreCase(segunda))
                .collect(Collectors.toList());
    }
}
