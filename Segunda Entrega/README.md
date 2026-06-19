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

**Paso 1 — Inicializar vista**
`RegistrarPedidoController.java:38` — `initialize()`
- Configura columnas de la `TableView` con `PropertyValueFactory`
- Carga `ComboBox<Producto>` desde `DatosSistema.getProductos()` (Singleton con datos en memoria)
- Setea `DatePicker` con `LocalDate.now()`

**Paso 2 — Buscar cliente por DNI**
`RegistrarPedidoController.java:55` — `buscarCliente()`
- Toma el DNI del `TextField`
- `DatosSistema.buscarClientePorDni(dni)` recorre la lista en memoria con `stream().filter()`
- Si existe → muestra nombre, teléfono, dirección, barrio/zona
- Si no existe → error, y desde el menú se puede registrar uno nuevo (UC aparte)

**Paso 3 — Agregar productos**
`RegistrarPedidoController.java:87` — `agregarProducto()`
- `new DetallePedido(producto, cantidad)` — el constructor **congela** el precio actual en `precioUnitarioHistorico` (inmutable, aunque el producto cambie de precio después)
- Se agrega a `ObservableList<DetallePedido>` vinculada a la `TableView`
- `actualizarTotal()` suma todos los subtotales: `stream().mapToDouble(DetallePedido::getSubtotal).sum()`

**Paso 4 — Confirmar pedido**
`RegistrarPedidoController.java:119` — `confirmarPedido()`
Validaciones:
1. `clienteActual == null` → "Busque y seleccione un cliente"
2. `listaItems.isEmpty()` → "Agregue al menos un producto"
3. `barrio == null` → "El cliente no tiene barrio/zona"
4. `fechaEntrega == null` → "Seleccione fecha de entrega"

Luego:
```java
PedidoDAOImpl pedidoDAO = new PedidoDAOImpl();
int nroPedido = pedidoDAO.generarNroPedido();       // SELECT MAX(nro_pedido)+1

Pedido nuevo = new Pedido(nroPedido, LocalDate.now(), fechaEntrega,
        clienteActual, listaItems);                  // estado = EstadoPendiente

PedidoService service = new PedidoService();
service.registrarPedido(nuevo);
```

**Paso 5 — PedidoService valida y persiste**
`PedidoService.java:27`
- Valida: `cliente == null` → exception; `detalles null/empty` → exception
- `pedidoRepository.insertar(pedido)` → `DatosSistema.insertarPedido()`

**Paso 6 — DatosSistema convierte a DTO y delega en DAO**
`DatosSistema.java:246`
- `convertirAPedidoDTOCompleto(pedido)` mapea `Pedido` → `PedidoDTO` (datos planos)
- `pedidoDAO.insertar(dto)` → `PedidoDAOImpl`

**Paso 7 — PedidoDAOImpl ejecuta transacción SQL**
`PedidoDAOImpl.java:71`
```sql
BEGIN TRANSACTION;
INSERT INTO pedido (nro_pedido, fecha_emision, fecha_entrega, estado, total,
                    nro_doc_cliente, metodo_pago, pago_procesado, observaciones)
VALUES (?, ?, ?, 'PENDIENTE', ?, ?, NULL, 0, '');
INSERT INTO detalle_pedido (nro_pedido, cod_producto, cantidad, precio_unitario)
VALUES (?, ?, ?, ?);  -- en batch por cada producto
COMMIT;
```
Si falla → `ROLLBACK`.

**Paso 8 — Feedback al usuario**
- `Alert("Pedido #N registrado correctamente")`
- `limpiarFormulario()` — reinicia todos los campos

---

### UC08 — Generar Rendicion Diaria (Administrativo)

**Paso 1 — Inicializar vista**
`GenerarRendicionController.java:38` — `initialize()`
- `dpFecha.setValue(LocalDate.now())`
- Configura columnas con `PropertyValueFactory` (nroPedido, nombreCliente, barrioCliente, zonaCliente, estadoNombre, metodoPagoDescripcion, totalFormateado)
- Llama automáticamente a `generarRendicion()` para mostrar el día actual

**Paso 2 — Controller delega en Service**
`GenerarRendicionController.java:51`
```java
RendicionDiaria rendicion = rendicionService.generar(fecha);
```

**Paso 3 — RendicionService.generar()**
`RendicionService.java:22`

**3a. Filtrar por fecha de entrega:**
```java
pedidoRepository.listarTodos().stream()
    .filter(pedido -> pedido.getFechaEntrega().equals(fecha))
```
Trae TODOS los pedidos desde la BD (`PedidoDAOImpl.listarTodos()` con JOIN a cliente, barrio, zona) y filtra por fecha.

**3b. Contar estados usando el State Pattern:**
```java
long entregados   = stream().filter(Pedido::estaEntregado).count();
long noEntregados = stream().filter(Pedido::estaNoEntregado).count();
long pendientes   = stream().filter(Pedido::estaPendiente).count();
```
Cada método usa `instanceof` contra el estado concreto (`EstadoEntregado`, `EstadoNoEntregado`, `EstadoPendiente`).

**3c. Separar ingresos por método de pago usando el Strategy Pattern:**
```java
double contado = stream()
    .filter(this::generaIngreso)        // entregado + metodoPago != null + pagoProcesado
    .filter(p -> p.getMetodoPago() instanceof PagoContado)
    .mapToDouble(Pedido::getTotal).sum();

double electronico = stream()
    .filter(this::generaIngreso)
    .filter(p -> p.getMetodoPago() instanceof PagoElectronico)
    .mapToDouble(Pedido::getTotal).sum();
```
`generaIngreso()` filtra solo pedidos que generaron ingreso real:
`pedido.estaEntregado() && pedido.getMetodoPago() != null && pedido.isPagoProcesado()`

**3d. Crear objeto inmutable:**
```java
return new RendicionDiaria(fecha, pedidosFecha, entregados, noEntregados,
    pendientes, contado + electronico, contado, electronico);
```
`RendicionDiaria` tiene todos sus campos `final` — solo getters.

**Paso 4 — Controller actualiza la UI**
`GenerarRendicionController.java:54`
```java
tblPedidos.setItems(FXCollections.observableArrayList(rendicion.getPedidos()));
lblEntregados.setText(String.valueOf(rendicion.getEntregados()));
lblNoEntregados.setText(String.valueOf(rendicion.getNoEntregados()));
lblPendientes.setText(String.valueOf(rendicion.getPendientes()));
lblTotalGeneral.setText(formato(rendicion.getTotalGeneralIngresos()));
lblTotalContado.setText(formato(rendicion.getTotalPagosContado()));
lblTotalElectronico.setText(formato(rendicion.getTotalPagosElectronicos()));
```

---

### Resumen visual del flujo

```
UC01 REGISTRAR PEDIDO:
  Controller → DatosSistema.buscarClientePorDni(dni) → cliente
  Controller → new DetallePedido(producto, cantidad) → congelar precio
  Controller → new Pedido(nro, hoy, fechaEntrega, cliente, items) → EstadoPendiente
  Controller → PedidoService.registrarPedido(pedido)
                 → PedidoRepository.insertar(pedido)
                   → DatosSistema.insertarPedido(pedido)
                     → PedidoDAOImpl.insertar(PedidoDTO) ← transacción SQL

UC08 GENERAR RENDICIÓN:
  Controller → RendicionService.generar(fecha)
                 → PedidoDAOImpl.listarTodos()  ← SELECT con JOINs
                 → stream().filter(fecha).count(estados via instanceof)
                 → stream().filter(generaIngreso)
                          .filter(instanceof PagoContado/Electronico)
                          .sum()
                 → new RendicionDiaria(fecha, entregados, ...)
  Controller → 7 labels actualizados
```

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
