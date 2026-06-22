# Instrucciones de Refactorización Avanzada: Migración a Base de Datos y Buenas Prácticas

Este documento contiene las directrices técnicas y de arquitectura para transformar `src/App.java` de una aplicación con estado en memoria a una aplicación conectada a una base de datos relacional MySQL a través de la capa DAO (`dao/*`).

---

## 1. Mapeo Histórico de Modelos (Memoria vs. Base de Datos)

Para evitar errores de compilación, se debe mapear la lógica anterior a las nuevas entidades del paquete `model/*`:

| Componente Anterior | Nueva Entidad / Tabla | Regla de Negocio Crítica |
| :--- | :--- | :--- |
| `MateriaPrima` | `Insumo` | El `id` cambia de `int` a `String` (ej: "BOT-001"). |
| `MaterialPorPrenda` | `InsumoPrenda` | Mapea la relación intermedia. El campo `cantidad` ahora es `cantidadInsumo`. |
| `listaUsuarios` | `Usuario` | Reemplazar la búsqueda en lista por una consulta directa a través de `UsuarioDAO`. |
| `Prenda` | `Prenda` | El `id` en la base de datos es `int NOT NULL AUTO_INCREMENT`. |
| `Conjunto` | `Conjunto` | Su existencia física depende de las existencias mínimas de las prendas que lo componen en la tabla `prendaconjunto`. |

---

## 2. Limpieza de Memoria Estática y Declaración de DAOs

### ELIMINAR de forma estricta:
Remover las variables globales que actúan como base de datos volátil:
* `private ObservableList<MateriaPrima> listaMateriaPrima = ...`
* `private ObservableList<MaterialPorPrenda> listaMaterialesPorPrenda = ...`
* `private ObservableList<Usuario> listaUsuarios = ...`
* `private ObservableList<Prenda> listaPrendas = ...`
* `private ObservableList<Conjunto> listaConjuntos = ...`
* `private ObservableList<PrendaVendida> listaPrendasVendidas = ...`
* `private ObservableList<ConjuntoVendido> listaConjuntosVendidos = ...`

### INYECTAR Campos de Capa de Datos (DAOs):
Agregar como atributos privados de la clase `App`:
```java
private PrendaDAO prendaDAO = new PrendaDAO();
private InsumoDAO insumoDAO = new InsumoDAO(); 
private ConjuntoDAO conjuntoDAO = new ConjuntoDAO();
private InsumoPrendaDAO insumoPrendaDAO = new InsumoPrendaDAO();
private VentaDAO ventaDAO = new VentaDAO();
private UsuarioDAO usuarioDAO = new UsuarioDAO();