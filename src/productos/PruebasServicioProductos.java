package productos;

import java.util.ArrayList;
import java.util.List;
import productos.modelo.Producto;
import productos.servicio.ServicioProductos;

/**
 * Verificación de las operaciones de {@link ServicioProductos} y de las
 * validaciones de {@link Producto}.
 *
 * No usa librerías externas: cada caso compara el resultado obtenido contra el
 * esperado y deja constancia en consola. Cubre escenarios que la ejecución
 * normal del programa no recorre: categorías inexistentes, listas nulas,
 * empates de precio y datos inválidos.
 *
 * Ejecutar con:  java -cp out productos.PruebasServicioProductos
 */
public class PruebasServicioProductos {

    private static int pruebasEjecutadas = 0;
    private static int pruebasFallidas = 0;

    /** Catálogo reducido y controlado, independiente del catálogo de la aplicación. */
    private static List<Producto> catalogoDePrueba() {
        return List.of(
                new Producto("Laptop", "Electrónica", 4000.00),
                new Producto("Monitor", "Electrónica", 900.00),
                new Producto("Teclado", "Electrónica", 300.00),
                new Producto("Mouse", "Electrónica", 300.00),
                new Producto("Camiseta", "Ropa", 80.00),
                new Producto("Chaqueta", "Ropa", 250.00));
    }

    public static void main(String[] args) {
        System.out.println("=== Verificación de ServicioProductos ===\n");

        verificarFiltrado();
        verificarOrdenacion();
        verificarFiltradoConOrdenacion();
        verificarInmutabilidad();
        verificarValidacionesDelProducto();

        System.out.println("\n=== Resultado ===");
        System.out.println("Pruebas ejecutadas: " + pruebasEjecutadas);
        System.out.println("Pruebas fallidas:   " + pruebasFallidas);

        if (pruebasFallidas > 0) {
            System.exit(1);
        }
    }

    private static void verificarFiltrado() {
        System.out.println("-- filtrarPorCategoria --");
        List<Producto> catalogo = catalogoDePrueba();

        comprobar("Devuelve solo los productos de la categoría",
                nombresDe(ServicioProductos.filtrarPorCategoria(catalogo, "Ropa")),
                List.of("Camiseta", "Chaqueta"));

        comprobar("No distingue mayúsculas de minúsculas",
                ServicioProductos.filtrarPorCategoria(catalogo, "rOpA").size(), 2);

        comprobar("Ignora los espacios sobrantes",
                ServicioProductos.filtrarPorCategoria(catalogo, "  Ropa  ").size(), 2);

        comprobar("Una categoría inexistente devuelve lista vacía",
                ServicioProductos.filtrarPorCategoria(catalogo, "Juguetes").isEmpty(), true);

        comprobar("Una categoría nula devuelve lista vacía",
                ServicioProductos.filtrarPorCategoria(catalogo, null).isEmpty(), true);

        comprobar("Una categoría en blanco devuelve lista vacía",
                ServicioProductos.filtrarPorCategoria(catalogo, "   ").isEmpty(), true);

        comprobar("Una lista nula devuelve lista vacía",
                ServicioProductos.filtrarPorCategoria(null, "Ropa").isEmpty(), true);
    }

    private static void verificarOrdenacion() {
        System.out.println("\n-- ordenarPorPrecioDescendente --");
        List<Producto> catalogo = catalogoDePrueba();

        comprobar("Ordena de mayor a menor precio",
                nombresDe(ServicioProductos.ordenarPorPrecioDescendente(catalogo)),
                List.of("Laptop", "Monitor", "Teclado", "Mouse", "Chaqueta", "Camiseta"));

        comprobar("Ante precios iguales conserva el orden original",
                nombresDe(ServicioProductos.ordenarPorPrecioDescendente(
                        ServicioProductos.filtrarPorCategoria(catalogo, "Electrónica"))),
                List.of("Laptop", "Monitor", "Teclado", "Mouse"));

        comprobar("Una lista vacía sigue vacía",
                ServicioProductos.ordenarPorPrecioDescendente(List.of()).isEmpty(), true);

        comprobar("Una lista nula devuelve lista vacía",
                ServicioProductos.ordenarPorPrecioDescendente(null).isEmpty(), true);

        List<Producto> unico = List.of(new Producto("Único", "Varios", 10.00));
        comprobar("Una lista de un elemento se devuelve igual",
                ServicioProductos.ordenarPorPrecioDescendente(unico).size(), 1);
    }

    private static void verificarFiltradoConOrdenacion() {
        System.out.println("\n-- filtrarYOrdenar --");
        List<Producto> catalogo = catalogoDePrueba();

        comprobar("Filtra y ordena en una sola operación",
                nombresDe(ServicioProductos.filtrarYOrdenar(catalogo, "Electrónica")),
                List.of("Laptop", "Monitor", "Teclado", "Mouse"));

        comprobar("Equivale a aplicar el filtro y luego la ordenación",
                nombresDe(ServicioProductos.filtrarYOrdenar(catalogo, "Ropa")),
                nombresDe(ServicioProductos.ordenarPorPrecioDescendente(
                        ServicioProductos.filtrarPorCategoria(catalogo, "Ropa"))));

        comprobar("Una categoría inexistente devuelve lista vacía",
                ServicioProductos.filtrarYOrdenar(catalogo, "Juguetes").isEmpty(), true);

        System.out.println("\n-- obtenerCategorias --");
        comprobar("Devuelve las categorías sin repetir y ordenadas",
                ServicioProductos.obtenerCategorias(catalogo),
                List.of("Electrónica", "Ropa"));
    }

    private static void verificarInmutabilidad() {
        System.out.println("\n-- No se altera la lista de origen --");

        List<Producto> original = new ArrayList<>(catalogoDePrueba());
        List<String> antes = nombresDe(original);

        ServicioProductos.filtrarPorCategoria(original, "Ropa");
        ServicioProductos.ordenarPorPrecioDescendente(original);
        ServicioProductos.filtrarYOrdenar(original, "Electrónica");

        comprobar("La lista original conserva su contenido y su orden",
                nombresDe(original), antes);
    }

    private static void verificarValidacionesDelProducto() {
        System.out.println("\n-- Validaciones de Producto --");

        comprobarExcepcion("Un nombre vacío se rechaza",
                () -> new Producto("  ", "Ropa", 10.00));

        comprobarExcepcion("Un nombre nulo se rechaza",
                () -> new Producto(null, "Ropa", 10.00));

        comprobarExcepcion("Una categoría vacía se rechaza",
                () -> new Producto("Camiseta", "", 10.00));

        comprobarExcepcion("Un precio negativo se rechaza",
                () -> new Producto("Camiseta", "Ropa", -1.00));

        Producto gratis = new Producto("Muestra gratis", "Promociones", 0.00);
        comprobar("Un precio de cero es válido", gratis.getPrecio(), 0.00);

        Producto conEspacios = new Producto("  Camiseta  ", "  Ropa  ", 80.00);
        comprobar("Se recortan los espacios del nombre", conEspacios.getNombre(), "Camiseta");
        comprobar("perteneceA compara sin distinguir mayúsculas",
                conEspacios.perteneceA("ROPA"), true);
        comprobar("perteneceA responde false ante una categoría nula",
                conEspacios.perteneceA(null), false);
    }

    /** Extrae los nombres de una lista de productos, para comparar resultados legibles. */
    private static List<String> nombresDe(List<Producto> productos) {
        return productos.stream()
                .map(producto -> producto.getNombre())
                .toList();
    }

    /**
     * Compara un resultado con el valor esperado y reporta el veredicto.
     *
     * @param descripcion qué se está comprobando
     * @param obtenido    valor devuelto
     * @param esperado    valor que debería devolver
     */
    private static void comprobar(String descripcion, Object obtenido, Object esperado) {
        pruebasEjecutadas++;
        boolean exito = esperado.equals(obtenido);
        if (!exito) {
            pruebasFallidas++;
            System.out.println("[FALLA] " + descripcion
                    + "\n        esperado: " + esperado
                    + "\n        obtenido: " + obtenido);
        } else {
            System.out.println("[OK]    " + descripcion);
        }
    }

    /**
     * Comprueba que una operación inválida lance {@link IllegalArgumentException}.
     *
     * @param descripcion qué se está comprobando
     * @param operacion   código que debería fallar
     */
    private static void comprobarExcepcion(String descripcion, Runnable operacion) {
        pruebasEjecutadas++;
        try {
            operacion.run();
            pruebasFallidas++;
            System.out.println("[FALLA] " + descripcion + " (no se lanzó ninguna excepción)");
        } catch (IllegalArgumentException esperada) {
            System.out.println("[OK]    " + descripcion);
        }
    }
}
