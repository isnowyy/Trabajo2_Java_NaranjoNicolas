package productos;

import java.util.List;
import productos.datos.CatalogoProductos;
import productos.modelo.Producto;
import productos.servicio.ServicioProductos;
import productos.vista.ConsolaProductos;

/**
 * Programa principal.
 *
 * Toma el catálogo, lo filtra por una categoría y muestra el resultado
 * ordenado de mayor a menor precio. El filtrado y la ordenación se delegan
 * en {@link ServicioProductos}, que los resuelve con expresiones lambda.
 *
 * Se puede indicar la categoría como argumento de ejecución:
 *   java -cp out productos.Main Ropa
 * Si no se indica ninguna, se usa "Electrónica".
 */
public class Main {

    /** Categoría que se consulta cuando no se recibe ningún argumento. */
    private static final String CATEGORIA_POR_DEFECTO = "Electrónica";

    public static void main(String[] args) {
        String categoria = (args.length > 0) ? args[0] : CATEGORIA_POR_DEFECTO;

        List<Producto> catalogo = CatalogoProductos.obtenerProductos();

        ConsolaProductos.mostrarLista("Catálogo completo (" + catalogo.size() + " productos)", catalogo);
        ConsolaProductos.separador();

        System.out.println("Categorías disponibles: "
                + String.join(", ", ServicioProductos.obtenerCategorias(catalogo)));
        ConsolaProductos.separador();

        // Filtrado y ordenación, ambos resueltos con expresiones lambda.
        List<Producto> resultado = ServicioProductos.filtrarYOrdenar(catalogo, categoria);

        ConsolaProductos.mostrarLista(
                "Productos de la categoría \"" + categoria + "\" ordenados por precio (descendente)",
                resultado);

        ConsolaProductos.separador();
        System.out.println("Productos encontrados: " + resultado.size() + " de " + catalogo.size());

        if (!resultado.isEmpty()) {
            Producto masCaro = resultado.get(0);
            Producto masBarato = resultado.get(resultado.size() - 1);
            System.out.println("Más caro:   " + masCaro.getNombre()
                    + " (" + ConsolaProductos.formatearPrecio(masCaro.getPrecio()) + ")");
            System.out.println("Más barato: " + masBarato.getNombre()
                    + " (" + ConsolaProductos.formatearPrecio(masBarato.getPrecio()) + ")");
        }
    }
}
