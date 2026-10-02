package productos.modelo;

/**
 * Representa un producto del catálogo.
 *
 * La clase es inmutable: sus atributos son privados y finales, se validan al
 * construir el objeto y no existen métodos que los modifiquen. Esto permite
 * compartir la misma instancia entre la lista original y las listas filtradas
 * u ordenadas sin riesgo de que una operación altere a la otra.
 */
public final class Producto {

    private final String nombre;
    private final String categoria;
    private final double precio;

    /**
     * Crea un producto validando sus datos.
     *
     * @param nombre    nombre del producto; no puede ser nulo ni estar en blanco
     * @param categoria categoría a la que pertenece; no puede ser nula ni estar en blanco
     * @param precio    precio del producto; no puede ser negativo ni NaN
     * @throws IllegalArgumentException si algún dato es inválido
     */
    public Producto(String nombre, String categoria, double precio) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }
        if (categoria == null || categoria.isBlank()) {
            throw new IllegalArgumentException("La categoría del producto no puede estar vacía");
        }
        if (Double.isNaN(precio) || precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo: " + precio);
        }
        this.nombre = nombre.trim();
        this.categoria = categoria.trim();
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public double getPrecio() {
        return precio;
    }

    /**
     * Indica si el producto pertenece a la categoría dada, ignorando
     * mayúsculas, minúsculas y espacios sobrantes.
     *
     * La regla de comparación vive aquí y no en el filtro, para que exista una
     * sola definición de qué significa "ser de esta categoría".
     *
     * @param categoriaBuscada categoría contra la que comparar
     * @return true si coincide; false si no coincide o si el argumento es nulo
     */
    public boolean perteneceA(String categoriaBuscada) {
        if (categoriaBuscada == null) {
            return false;
        }
        return this.categoria.equalsIgnoreCase(categoriaBuscada.trim());
    }

    @Override
    public String toString() {
        return String.format("Producto{nombre='%s', categoria='%s', precio=%.2f}",
                nombre, categoria, precio);
    }
}
