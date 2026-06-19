# Mapeo Diagrama StarUML <-> Codigo Fuente

Este documento vincula cada clase del diagrama de clases conceptual (StarUML) con su implementacion real en el codigo.
El diagrama es intencionalmente simple para facilitar la comprension de la arquitectura.

---

## Modelo de Dominio

| Clase en StarUML | Archivo(s) en codigo | Notas |
|---|---|---|
| `Persona` | `src/main/java/com/aguasvital/model/Persona.java` | Clase abstracta. Base de la herencia de `Cliente` y `Empleado` |
| `Cliente` | `src/main/java/com/aguasvital/model/Cliente.java` | Hereda de `Persona`. Agrega `direccion`, `telefono`, `barrio` |
| `Empleado` | `src/main/java/com/aguasvital/model/Empleado.java` | Hereda de `Persona`. Usa el enum `Cargo` para el rol (OPERADORA, DISTRIBUIDOR, etc.) |
| `Cargo` (enum) | `src/main/java/com/aguasvital/model/Cargo.java` | Enum con 4 valores: OPERADORA, ENCARGADO_ADMINISTRATIVO, DISTRIBUIDOR, PRESIDENTE |
| `Barrio` | `src/main/java/com/aguasvital/model/Barrio.java` | Tiene atributo `zona: Zona` |
| `Zona` | `src/main/java/com/aguasvital/model/Zona.java` | Identificador `idZona` + `nombre` |
| `Pedido` | `src/main/java/com/aguasvital/model/Pedido.java` | Usa State (`estadoActual: EstadoPedido`) y Strategy (`metodoPago: MetodoPago`) |
| `DetallePedido` | `src/main/java/com/aguasvital/model/DetallePedido.java` | Tiene `producto: Producto`, `cantidad`, `precioUnitarioHistorico` |
| `Producto` | `src/main/java/com/aguasvital/model/Producto.java` | Contiene lista de `Precio` historicos |
| `Precio` | `src/main/java/com/aguasvital/model/Precio.java` | `valor`, `fechaDesde`, `fechaHasta` (nullable) |
| `Envase` | `src/main/java/com/aguasvital/model/Envase.java` | `tipoEnvase` (Bidon/Sifon) + `capacidad` (6/10/12/20/2 litros) |
| `Factura` | `src/main/java/com/aguasvital/model/Factura.java` | **Opcional — para futura implementacion.** No se genera actualmente |
| `DetalleFactura` | `src/main/java/com/aguasvital/model/DetalleFactura.java` | **Opcional — para futura implementacion.** Soporte para Factura |

---

## Patrones de Diseno

### State Pattern
| Rol | Clase en diagrama | Clase en codigo |
|---|---|---|
| Interface | `EstadoPedido` | `src/main/java/com/aguasvital/state/EstadoPedido.java` |
| Estado Pendiente | `EstadoPendiente` | `src/main/java/com/aguasvital/state/EstadoPendiente.java` |
| Estado Entregado | `EstadoEntregado` | `src/main/java/com/aguasvital/state/EstadoEntregado.java` |
| Estado No Entregado | `EstadoNoEntregado` | `src/main/java/com/aguasvital/state/EstadoNoEntregado.java` |
| Contexto | `Pedido` | `Pedido.estadoActual` controla las transiciones |

**Implementado en codigo:** `Pedido.marcarEntregado()` y `marcarNoEntregado()` delegan al estado actual, que valida si la transicion es valida. Un pedido entregado no puede volver a pendiente.

### Strategy Pattern
| Rol | Clase en diagrama | Clase en codigo |
|---|---|---|
| Interface | `MetodoPago` | `src/main/java/com/aguasvital/strategy/MetodoPago.java` |
| Pago contado | `PagoContado` | `src/main/java/com/aguasvital/strategy/PagoContado.java` |
| Pago electronico | `PagoElectronico` | `src/main/java/com/aguasvital/strategy/PagoElectronico.java` |
| Contexto | `Pedido` | `Pedido.metodoPago` delega el procesamiento |

**Implementado en codigo:** `Pedido.procesarPago()` llama a `metodoPago.procesarPago(total)`. Cada estrategia implementa su propia logica de validacion.

---

## Infraestructura (no modelada en StarUML)

Estas clases son parte de la arquitectura pero no aparecen en el diagrama conceptual para mantenerlo simple.

### Capa de Persistencia
| Clase | Funcion | Patron |
|---|---|---|
| `dao/DatabaseManager.java` | Conexion Singleton a SQLite. Carga el esquema al iniciar la app | **Singleton** |
| `dao/Dao.java` | Interfaz generica con CRUD: `buscar`, `listarTodos`, `insertar`, `modificar`, `borrar` | **DAO generico** |
| `dao/FabricaDAO.java` | Crea la implementacion DAO adecuada segun el tipo de DTO. Ej: `fabricar(PedidoDTO.class)` devuelve `PedidoDAOImpl` | **Factory Method** |
| `dao/ClienteDAOImpl.java` | Implementa `Dao<ClienteDTO>` contra SQLite | DAO concreto |
| `dao/PedidoDAOImpl.java` | Implementa `Dao<PedidoDTO>` con transacciones (pedido + batch de detalles) | DAO concreto |
| `dto/ClienteDTO.java` | DTO para transporte de datos de cliente entre capas | **DTO** |
| `dto/PedidoDTO.java` | DTO para transporte de pedido + detalles | DTO |
| `dto/DetallePedidoDTO.java` | DTO para cada linea de detalle (usado dentro de PedidoDTO) | DTO |

### Capa de Negocio
| Clase | Funcion | Patron |
|---|---|---|
| `repository/DatosSistema.java` | Singleton que carga toda la BD en memoria (zonas, barrios, productos, precios, clientes, empleados, pedidos) y los expone a los controllers via getters. Tambien persiste cambios via DAOs | **Singleton** |
| `repository/PedidoRepository.java` | Wrapper sobre DatosSistema para operaciones con pedidos | Repository |
| `service/PedidoService.java` | Orquesta el registro de pedidos con validaciones de negocio | **Service Layer** |
| `service/RendicionService.java` | Genera la rendicion diaria separando pagos contado y electronicos | Service Layer |
| `service/RendicionDiaria.java` | Clase de datos que encapsula el resultado de una rendicion | DTO de salida |

### Capa de Presentacion (MVC)
| Componente | Archivos | Funcion |
|---|---|---|
| **Vistas (FXML)** | 11 archivos en `resources/com/aguasvital/view/` | Interfaces de usuario construidas con SceneBuilder |
| **Controladores** | 10 clases en `controller/` (Login, MenuPrincipal, RegistrarPedido, ActualizarPedido, CancelarPedido, RegistrarCliente, RegistrarPendientes, EfectuarTraslado, GenerarRendicion, GestionarPrecios, ConsultarPedidos) | Manejan eventos de UI y orquestan la logica |
| **Utilidades** | `util/Navegador.java` — cambio de vistas; `util/SesionUsuario.java` — sesion del usuario logueado | Soporte transversal |

---

## Resumen de Patrones

| Patron | Donde se implementa | Estado |
|---|---|---|
| **State** | `state/*.java` + `Pedido.estadoActual` | Implementado en codigo |
| **Strategy** | `strategy/*.java` + `Pedido.metodoPago` | Implementado en codigo |
| **Singleton** | `DatabaseManager`, `DatosSistema`, `SesionUsuario` | Implementado en codigo |
| **DAO** | `Dao<T>` + `ClienteDAOImpl` + `PedidoDAOImpl` | Implementado en codigo |
| **DTO** | `dto/*.java` | Implementado en codigo |
| **Factory Method** | `FabricaDAO.fabricar()` | Implementado en codigo |
| **MVC** | FXML (View) + Controller + Model | Implementado en codigo |
