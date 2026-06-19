# Aguas Vital - Sistema de Gestion de Pedidos

Aplicacion JavaFX con persistencia SQLite para la gestion de pedidos de una distribuidora de agua. Desarrollada como segunda entrega de la materia **Diseno Orientado a Objetos**.

## Requisitos

- Java 21+
- Maven 3.9+
- JavaFX 21.0.5 (incluido via Maven)

## Como ejecutar

```bash
mvn clean javafx:run
```

## Como ejecutar tests

```bash
mvn test
```

## Credenciales de prueba

| Usuario       | Password | Rol                        |
|---------------|----------|----------------------------|
| operadora     | 1234     | OPERADORA                  |
| administrativo| 1234     | ENCARGADO_ADMINISTRATIVO   |
| distribuidor  | 1234     | DISTRIBUIDOR               |
| presidente    | 1234     | PRESIDENTE                 |

---

## Flujo de la aplicacion

### 1. Login
`LoginController` valida usuario/password contra `DatosSistema`. Si es correcto, almacena el usuario en `SesionUsuario` (Singleton) y redirige al menu principal con botones filtrados segun el rol.

### 2. Menu Principal
`MenuPrincipalController` carga los botones de acuerdo al cargo del usuario logueado. Usa el Singleton `SesionUsuario` para conocer el rol actual.

### 3. Registrar Pedido (UC01)
`RegistrarPedidoController` + `PedidoService`:
- Busca el cliente por numero de documento
- Agrega/quita productos al pedido
- Verifica que el cliente tenga barrio/zona registrados
- Confirma el pedido, lo persiste via `PedidoDAOImpl` (SQLite) y lo registra en `DatosSistema`

### 4. Consultar Pedidos
`ConsultarPedidosController` muestra todos los pedidos con filtros por estado. Los datos se cargan desde la base de datos via `DatosSistema`.

### 5. Registrar Pendientes
`RegistrarPendientesController` lista pedidos pendientes y permite reasignar distribuidor. Los distribuidores se cargan desde la BD.

### 6. Efectuar Traslado
`EfectuarTrasladoController` lista pedidos pendientes para que el distribuidor marque como entregado o no entregado.

### 7. Gestionar Precios
`GestionarPreciosController` permite modificar precios de productos. Los datos se cargan desde SQLite.

### 8. Generar Rendicion
`GenerarRendicionController` + `RendicionService` genera un reporte diario de ingresos separando pagos en efectivo y electronicos, excluyendo pedidos pendientes y no entregados.

### 9. Cancelar Pedido
`CancelarPedidoController` busca un pedido por numero y permite cancelarlo (funcionalidad en desarrollo).

### 10. Registrar Cliente
`RegistrarClienteController` carga zonas y barrios desde la BD y filtra barrios dinamicamente segun la zona seleccionada.

---

## Conceptos de Diseno Orientado a Objetos aplicados

### Principios SOLID

| Principio | Donde se aplica |
|-----------|-----------------|
| **S**ingle Responsibility | Cada clase tiene una unica responsabilidad: `PedidoService` solo orquesta reglas de negocio, `PedidoDAOImpl` solo persiste, `RegistrarPedidoController` solo maneja la UI |
| **O**pen/Closed | Las estrategias de pago (`MetodoPago`) estan abiertas a extension (nuevo metodo de pago) pero cerradas a modificacion |
| **L**iskov Substitution | `EstadoPendiente`, `EstadoEntregado`, `EstadoNoEntregado` pueden sustituir a `EstadoPedido` sin alterar el comportamiento del sistema |
| **I**nterface Segregation | `Dao<T>` es una interfaz pequena y generica con solo 5 metodos esenciales |
| **D**ependency Inversion | `PedidoService` depende de la abstraccion `PedidoRepository`, no de una implementacion concreta |

### Patrones de Diseno

#### Singleton
`DatosSistema` y `DatabaseManager` usan Singleton. Solo existe una instancia en toda la aplicacion, accesible via `getInstancia()`. `DatabaseManager` maneja la conexion unica a SQLite, y `DatosSistema` es la fuente central de datos en memoria.

#### State
El ciclo de vida de un `Pedido` se modela con State. `EstadoPedido` es la interfaz; `EstadoPendiente`, `EstadoEntregado` y `EstadoNoEntregado` son las implementaciones. Cada estado controla las transiciones: un pedido pendiente puede marcarse como entregado o no entregado, pero uno entregado no puede volver a pendiente.

#### Strategy
Los metodos de pago usan Strategy. `MetodoPago` es la interfaz; `PagoContado` y `PagoElectronico` implementan `procesarPago()`. El `Pedido` delega el procesamiento del pago a la estrategia seleccionada, permitiendo cambiar el algoritmo en tiempo de ejecucion.

#### MVC (Model-View-Controller)
La aplicacion sigue MVC:
- **Model**: Clases del paquete `model` (`Pedido`, `Cliente`, `Producto`, etc.)
- **View**: Archivos FXML en `resources/com/aguasvital/view/`
- **Controller**: Clases en `controller/` (`RegistrarPedidoController`, `LoginController`, etc.)

#### DAO (Data Access Object)
`Dao<T>` es la interfaz generica con CRUD (`buscar`, `listarTodos`, `insertar`, `modificar`, `borrar`). `ClienteDAOImpl` y `PedidoDAOImpl` implementan la capa de persistencia con SQLite, aislando la logica de negocio del acceso a datos.

#### DTO (Data Transfer Object)
Los DTOs (`ClienteDTO`, `PedidoDTO`, `DetallePedidoDTO`, etc.) separan los datos persistidos del modelo de dominio. Los DAOs trabajan con DTOs, y `DatosSistema` los convierte a objetos del modelo.

#### Factory Method
`FabricaDAO.fabricar(Class<T>)` es un Factory Method que crea la implementacion DAO adecuada segun la clase DTO recibida. Oculta la logica de creacion y permite centralizar cambios.

### Relaciones entre clases

- **Composicion**: `Pedido` contiene una lista de `DetallePedido`. Si el pedido se elimina, los detalles tambien.
- **Agregacion**: `DatosSistema` mantiene listas de `Zona`, `Barrio`, `Cliente`, `Producto`, `Pedido` y `Empleado`. Estos objetos existen independientemente del singleton.
- **Herencia**: `Cliente` y `Empleado` extienden `Persona`. Comparten atributos comunes (nombre, apellido, tipoDoc, nroDoc, razonSocial).
- **Interfaces**: `Dao<T>` es implementada por `ClienteDAOImpl` y `PedidoDAOImpl`. `EstadoPedido` es implementada por los tres estados concretos. `MetodoPago` es implementada por las dos estrategias de pago.

### Diagrama de clases

```mermaid
classDiagram
    class Persona {
        #String nombre
        #String apellido
        #String tipoDoc
        #String nroDoc
    }
    class Cliente {
        +String direccion
        +String telefono
        +Barrio barrio
    }
    class Empleado {
        +Cargo cargo
        +int capacidadMaxima
    }
    class Pedido {
        -int nroPedido
        -EstadoPedido estadoActual
        -MetodoPago metodoPago
        +marcarEntregado()
        +marcarNoEntregado()
        +procesarPago()
    }
    class DetallePedido {
        +int cantidad
        +double precioUnitarioHistorico
        +double subtotal
    }
    class Producto {
        +String codigo
        +String nombre
        +Precio precioVigente()
    }
    class Envase {
        +String tipoEnvase
        +int capacidad
    }
    class Precio {
        +double valor
        +LocalDate fechaDesde
        +LocalDate fechaHasta
    }
    class Barrio {
        +String idBarrio
        +String nombre
    }
    class Zona {
        +String idZona
        +String nombre
    }

    interface EstadoPedido {
        +marcarEntregado(Pedido)
        +marcarNoEntregado(Pedido)
        +obtenerNombre()
    }
    class EstadoPendiente
    class EstadoEntregado
    class EstadoNoEntregado

    interface MetodoPago {
        +procesarPago(double) boolean
        +obtenerDescripcion()
    }
    class PagoContado
    class PagoElectronico

    interface Dao~T~ {
        +buscar(int) Optional~T~
        +listarTodos() List~T~
        +insertar(T)
        +modificar(T)
        +borrar(int)
    }
    class ClienteDAOImpl
    class PedidoDAOImpl

    Persona <|-- Cliente
    Persona <|-- Empleado
    Pedido o-- DetallePedido
    Pedido o-- Cliente
    Pedido --> EstadoPedido
    Pedido --> MetodoPago
    DetallePedido --> Producto
    Producto --> Envase
    Producto o-- Precio
    Barrio --> Zona
    Cliente --> Barrio
    EstadoPedido <|.. EstadoPendiente
    EstadoPedido <|.. EstadoEntregado
    EstadoPedido <|.. EstadoNoEntregado
    MetodoPago <|.. PagoContado
    MetodoPago <|.. PagoElectronico
    Dao~T~ <|.. ClienteDAOImpl
    Dao~T~ <|.. PedidoDAOImpl
```

---
