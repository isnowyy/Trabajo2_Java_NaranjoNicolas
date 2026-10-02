package productos.datos;

import java.util.List;
import productos.modelo.Producto;

/**
 * Fuente de datos del programa.
 *
 * Aísla el catálogo de ejemplo del resto de la aplicación: si mañana los
 * productos vinieran de un archivo o de una base de datos, solo cambiaría
 * esta clase y ni el servicio ni el main se enterarían.
 */
public final class CatalogoProductos {

    /** Clase de utilidad: no está pensada para instanciarse. */
    private CatalogoProductos() {
    }

    /**
     * Devuelve el catálogo de ejemplo.
     *
     * La lista es inmutable, de modo que ninguna operación posterior pueda
     * modificar los datos de origen por accidente.
     *
     * @return lista de productos de varias categorías y precios
     */
    public static List<Producto> obtenerProductos() {
        return List.of(
                new Producto("Laptop Pro 14", "Electrónica", 4299.99),
                new Producto("Audífonos inalámbricos", "Electrónica", 189.50),
                new Producto("Monitor 27 pulgadas", "Electrónica", 899.00),
                new Producto("Teclado mecánico", "Electrónica", 320.75),
                new Producto("Mouse ergonómico", "Electrónica", 189.50),
                new Producto("Camiseta de algodón", "Ropa", 79.90),
                new Producto("Chaqueta impermeable", "Ropa", 249.00),
                new Producto("Zapatos deportivos", "Ropa", 310.00),
                new Producto("Cafetera italiana", "Hogar", 145.25),
                new Producto("Juego de sábanas", "Hogar", 210.00),
                new Producto("Lámpara de escritorio", "Hogar", 98.40));
    }
}
