# Filtrar y ordenar productos con expresiones lambda

Programa en Java que toma una lista de productos y, usando **expresiones lambda**, la filtra
por una categoría y ordena el resultado de mayor a menor precio.

## Estructura

El código está modularizado: cada clase tiene una sola responsabilidad y vive en el paquete
que le corresponde.

```
src/productos/
├── Main.java                        # Archivo principal de ejecución
├── PruebasServicioProductos.java    # Verificación de las operaciones
├── modelo/
│   └── Producto.java                # Los datos: nombre, categoría y precio
├── datos/
│   └── CatalogoProductos.java       # La fuente de datos (catálogo de ejemplo)
├── servicio/
│   └── ServicioProductos.java       # La lógica: filtrado y ordenación con lambdas
└── vista/
    └── ConsolaProductos.java        # La presentación por consola
```

| Paquete | Responsabilidad | Por qué está separado |
|---|---|---|
| `modelo` | Representar un producto y validar sus datos | El resto del programa trabaja sobre esta estructura sin conocer de dónde salió |
| `datos` | Entregar el catálogo | Si mañana los productos vinieran de un archivo o una base de datos, solo cambiaría esta clase |
| `servicio` | Filtrar y ordenar | Concentra la lógica del reto; no imprime nada, solo devuelve resultados |
| `vista` | Dar formato e imprimir | Mantiene al servicio libre de `System.out` |

## Cómo ejecutarlo

```bash
javac -d out $(find src -name "*.java")
java -cp out productos.Main
```

La categoría se puede pasar como argumento; si se omite, se consulta `Electrónica`:

```bash
java -cp out productos.Main Ropa
```

Para correr la verificación:

```bash
java -cp out productos.PruebasServicioProductos
```

## Salida

```
Catálogo completo (11 productos)
--------------------------------
  Laptop Pro 14              Electrónica       $4,299.99
  Audífonos inalámbricos     Electrónica         $189.50
  Monitor 27 pulgadas        Electrónica         $899.00
  Teclado mecánico           Electrónica         $320.75
  Mouse ergonómico           Electrónica         $189.50
  Camiseta de algodón        Ropa                 $79.90
  Chaqueta impermeable       Ropa                $249.00
  Zapatos deportivos         Ropa                $310.00
  Cafetera italiana          Hogar               $145.25
  Juego de sábanas           Hogar               $210.00
  Lámpara de escritorio      Hogar                $98.40

Categorías disponibles: Electrónica, Hogar, Ropa

Productos de la categoría "Electrónica" ordenados por precio (descendente)
--------------------------------------------------------------------------
  Laptop Pro 14              Electrónica       $4,299.99
  Monitor 27 pulgadas        Electrónica         $899.00
  Teclado mecánico           Electrónica         $320.75
  Audífonos inalámbricos     Electrónica         $189.50
  Mouse ergonómico           Electrónica         $189.50

Productos encontrados: 5 de 11
Más caro:   Laptop Pro 14 ($4,299.99)
Más barato: Mouse ergonómico ($189.50)
```

## Las expresiones lambda

Las dos operaciones que pide el reto se resuelven con lambdas, cada una asignada a una
interfaz funcional con nombre para que quede claro qué hace cada criterio.

### 1. Filtrado — `Predicate<Producto>`

```java
Predicate<Producto> esDeLaCategoria = producto -> producto.perteneceA(categoria);

return productos.stream()
        .filter(esDeLaCategoria)
        .collect(Collectors.toList());
```

La lambda recibe un producto y responde si debe conservarse. La regla de comparación vive
dentro de `Producto.perteneceA`, de modo que existe **una sola definición** de qué significa
"pertenecer a esta categoría" (sin distinguir mayúsculas ni espacios sobrantes), y no una
copia repetida en cada filtro.

### 2. Ordenación — `Comparator<Producto>`

```java
Comparator<Producto> porPrecioDescendente =
        (primero, segundo) -> Double.compare(segundo.getPrecio(), primero.getPrecio());

return productos.stream()
        .sorted(porPrecioDescendente)
        .collect(Collectors.toList());
```

Comparar el **segundo contra el primero** es lo que invierte el orden natural y produce el
descendente.

Se usa `Double.compare` y no una resta entre precios. La resta de dos `double` devuelve un
decimal, y al convertirse al `int` que exige el comparador se trunca: una diferencia de 0.50
se volvería 0 y el comparador trataría como iguales dos precios distintos.

### Operación combinada

`filtrarYOrdenar` encadena ambas lambdas sobre el mismo stream, que es lo que usa el
programa principal:

```java
return productos.stream()
        .filter(esDeLaCategoria)
        .sorted(porPrecioDescendente)
        .collect(Collectors.toList());
```

## Decisiones de diseño

**`Producto` es inmutable.** Sus atributos son privados y finales, se validan al construir el
objeto y no hay métodos que los modifiquen. Así la misma instancia puede compartirse entre la
lista original y las listas filtradas sin que una operación afecte a la otra.

**Ninguna operación modifica la lista recibida.** Todas devuelven una lista nueva, por lo que
el catálogo original queda intacto después de consultarlo. Hay una prueba que lo verifica.

**Las entradas inválidas devuelven una lista vacía, no un error.** Una lista nula, una
categoría nula o en blanco y una categoría inexistente producen el mismo resultado: cero
productos. Filtrar algo que no existe no es una falla del programa.

**Los datos inválidos sí lanzan excepción.** Un producto sin nombre, sin categoría o con
precio negativo no puede existir, así que el constructor lanza `IllegalArgumentException`
en lugar de crear un objeto en mal estado.

**El `Locale` del formato de precio está fijado.** `String.format(Locale.US, "$%,.2f", ...)`
produce la misma salida en cualquier equipo, sin depender de la configuración regional de
quien lo ejecute.

## Verificación

`PruebasServicioProductos` ejecuta 25 comprobaciones sin librerías externas. Termina con
código de salida distinto de cero si alguna falla.

Cubre, entre otros casos:

- Filtrado con coincidencias, sin coincidencias, con distinta capitalización y con espacios.
- Entradas nulas y en blanco.
- Orden descendente correcto y comportamiento ante **precios empatados** (el orden original
  se conserva, porque `sorted` es estable).
- Listas vacías y de un solo elemento.
- Que la lista de origen no se altere al consultarla.
- Las validaciones del constructor de `Producto`.

```
=== Resultado ===
Pruebas ejecutadas: 25
Pruebas fallidas:   0
```

## Requisitos

Java 17 o superior (probado con JDK 26). No usa dependencias externas.
