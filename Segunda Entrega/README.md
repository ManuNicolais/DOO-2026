# Aguas Vital SA — Segunda Entrega

Sistema de gestion de pedidos, distribucion y rendicion para una empresa de fraccionamiento y comercializacion de agua mineral.

## Tecnologias

- Java 21
- JavaFX 21.0.5
- Maven
- SQLite
- JUnit 5
- SceneBuilder (vistas FXML)

## Arquitectura

```
Vista (FXML) → Controlador → Service → Repository → DatosSistema → DAO → SQLite
                                                                    ↑
                                                        (Singleton + Factory Method)
```

Patron **MVC** en capas. Cada capa tiene responsabilidad unica y se comunica con la siguiente mediante interfaces o clases abstractas.

## Patrones de Diseno (7 en total)

### State Pattern
`EstadoPedido` define la interfaz. `EstadoPendiente`, `EstadoEntregado`, `EstadoNoEntregado` implementan las transiciones. `Pedido` delega en `estadoActual`.

**Beneficio:** Agregar un nuevo estado solo requiere crear una nueva clase que implemente `EstadoPedido`, sin modificar `Pedido` (OCP).

### Strategy Pattern
`MetodoPago` define la interfaz. `PagoContado` y `PagoElectronico` implementan el procesamiento. `Pedido` inyecta la estrategia en `seleccionarMetodoPago()`.

**Beneficio:** Agregar `PagoCheque` o `PagoTransferencia` solo requiere una nueva clase (OCP).

### Singleton Pattern
- `DatabaseManager` — conexion unica a SQLite
- `DatosSistema` — cache de datos en memoria
- `SesionUsuario` — sesion del usuario logueado

### DAO Pattern
Interfaz generica `Dao<T>` con `ClienteDAOImpl` y `PedidoDAOImpl` para SQLite. Separa la logica de persistencia del modelo de negocio.

### DTO Pattern
`PedidoDTO`, `ClienteDTO`, `DetallePedidoDTO` transportan datos entre capas sin exponer la logica de negocio del modelo.

### Factory Method
`FabricaDAO.fabricar(Class)` devuelve la implementacion DAO correcta segun el tipo de DTO.

### MVC
11 vistas FXML + 11 controladores + modelo de dominio. `Navegador` maneja transiciones entre pantallas.

## Diagrama de Clases (Diseno con Patrones)
FOTO DEL DIAGRAMA DE CLASES
## Casos de Uso Implementados

### UC01 — Registrar Pedido (Operadora)
1. Operadora busca cliente por DNI
2. Si no existe, lo registra
3. Agrega productos con cantidad al pedido
4. Confirma pedido — se persiste con estado Pendiente

### UC08 — Generar Rendicion Diaria (Administrativo)
1. Selecciona fecha
2. El sistema filtra pedidos de esa fecha, cuenta estados (Entregado/NoEntregado/Pendiente)
3. Separa ingresos por metodo de pago (Contado vs Electronico)
4. Muestra resumen

## Ejecucion

```bash
mvn clean compile
mvn javafx:run
```

## Tests

```bash
mvn test
```

21 tests: Estado, Strategy, Service, Rendicion, FXML load, FXML structure.

## Credenciales de Prueba

| Usuario | Password | Rol |
|---|---|---|
| operadora | 1234 | OPERADORA |
| administrativo | 1234 | ENCARGADO_ADMINISTRATIVO |
| distribuidor | 1234 | DISTRIBUIDOR |
| presidente | 1234 | PRESIDENTE |

## Estructura del Proyecto

```
src/main/java/com/aguasvital/
├── controller/       # 11 controladores (uno por vista)
├── dao/              # DatabaseManager (Singleton), FabricaDAO (Factory), Dao<T>, impls
├── dto/              # PedidoDTO, ClienteDTO, DetallePedidoDTO
├── model/            # Persona, Cliente, Empleado, Pedido, Producto, etc.
├── repository/       # DatosSistema (Singleton), PedidoRepository, UsuarioSistema
├── service/          # PedidoService, RendicionService, RendicionDiaria
├── state/            # EstadoPedido (interface) + 3 estados concretos
├── strategy/         # MetodoPago (interface) + 2 implementaciones
├── util/             # Navegador, SesionUsuario
├── view/             # 11 archivos FXML
└── resources/sql/    # esquema.sql (9 tablas + seed data)

src/test/java/com/aguasvital/
├── fxml/             # FxmlLoadTest, FxmlStructureTest
├── service/          # PedidoServiceTest, RendicionServiceTest
├── state/            # EstadoPedidoTest
└── strategy/         # MetodoPagoTest
```

## Archivos Entregados

- Codigo fuente completo (proyecto Maven)
- `diagrama de clase con patron diseno.mdj` — StarUML con diagrama de clases conceptual
- `MAPEO.md` — trazabilidad entre diagrama StarUML y codigo fuente
- `aguas_vital.db` — base de datos SQLite con datos de prueba precargados
- `pom.xml` — configuracion Maven con dependencias
- `esquema.sql` — DDL de las 9 tablas + seed data
