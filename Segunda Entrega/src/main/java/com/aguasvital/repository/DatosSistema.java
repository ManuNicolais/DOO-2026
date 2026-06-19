package com.aguasvital.repository;

import com.aguasvital.dao.ClienteDAOImpl;
import com.aguasvital.dao.DatabaseManager;
import com.aguasvital.dao.FabricaDAO;
import com.aguasvital.dao.PedidoDAOImpl;
import com.aguasvital.dto.*;
import com.aguasvital.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatosSistema {
    private static DatosSistema instancia;

    private final List<Zona> zonas = new ArrayList<>();
    private final List<Barrio> barrios = new ArrayList<>();
    private final List<Cliente> clientes = new ArrayList<>();
    private final List<Empleado> empleados = new ArrayList<>();
    private final List<Envase> envases = new ArrayList<>();
    private final List<Producto> productos = new ArrayList<>();
    private final List<Pedido> pedidos = new ArrayList<>();

    private final ClienteDAOImpl clienteDAO;
    private final PedidoDAOImpl pedidoDAO;

    private DatosSistema() {
        clienteDAO = (ClienteDAOImpl) FabricaDAO.fabricar(ClienteDTO.class);
        pedidoDAO = (PedidoDAOImpl) FabricaDAO.fabricar(PedidoDTO.class);
        cargarDatosDesdeBD();
    }

    public static DatosSistema getInstancia() {
        if (instancia == null) {
            instancia = new DatosSistema();
        }
        return instancia;
    }

    public static void reiniciarParaPruebas() {
        DatabaseManager.reiniciar();
        instancia = new DatosSistema();
    }

    private void cargarDatosDesdeBD() {
        DatabaseManager.getInstancia();
        cargarZonas();
        cargarBarrios();
        cargarEnvases();
        cargarProductos();
        cargarPrecios();
        cargarClientes();
        cargarEmpleados();
        cargarPedidos();
    }

    private void cargarZonas() {
        String sql = "SELECT id_zona, nombre FROM zona";
        try (Connection conn = DatabaseManager.getInstancia().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                zonas.add(new Zona(rs.getString("id_zona"), rs.getString("nombre")));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar zonas", e);
        }
    }

    private void cargarBarrios() {
        String sql = "SELECT b.id_barrio, b.nombre, b.id_zona FROM barrio b";
        try (Connection conn = DatabaseManager.getInstancia().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String idZona = rs.getString("id_zona");
                Zona zona = zonas.stream().filter(z -> z.getIdZona().equals(idZona)).findFirst().orElse(null);
                barrios.add(new Barrio(rs.getString("id_barrio"), rs.getString("nombre"), zona));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar barrios", e);
        }
    }

    private void cargarEnvases() {
        String sql = "SELECT tipo_envase, capacidad FROM envase";
        try (Connection conn = DatabaseManager.getInstancia().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                envases.add(new Envase(rs.getString("tipo_envase"), rs.getInt("capacidad")));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar envases", e);
        }
    }

    private void cargarProductos() {
        String sql = "SELECT codigo, nombre, tipo_envase, capacidad FROM producto";
        try (Connection conn = DatabaseManager.getInstancia().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String tipo = rs.getString("tipo_envase");
                int cap = rs.getInt("capacidad");
                Envase envase = envases.stream()
                        .filter(e -> e.getTipoEnvase().equals(tipo) && e.getCapacidad() == cap)
                        .findFirst().orElse(null);
                productos.add(new Producto(rs.getString("codigo"), rs.getString("nombre"), envase));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar productos", e);
        }
    }

    private void cargarPrecios() {
        String sql = "SELECT cod_producto, valor, fecha_desde, fecha_hasta FROM precio";
        try (Connection conn = DatabaseManager.getInstancia().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String cod = rs.getString("cod_producto");
                Producto prod = productos.stream().filter(p -> p.getCodigo().equals(cod)).findFirst().orElse(null);
                if (prod != null) {
                    String fechaHastaStr = rs.getString("fecha_hasta");
                    LocalDate fechaHasta = fechaHastaStr != null ? LocalDate.parse(fechaHastaStr) : null;
                    prod.agregarPrecio(new Precio(rs.getDouble("valor"),
                            LocalDate.parse(rs.getString("fecha_desde")), fechaHasta));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar precios", e);
        }
    }

    private void cargarClientes() {
        List<ClienteDTO> dtos = clienteDAO.listarTodos();
        for (ClienteDTO dto : dtos) {
            Barrio barrio = barrios.stream()
                    .filter(b -> b.getIdBarrio().equals(dto.getIdBarrio()))
                    .findFirst().orElse(null);
            clientes.add(new Cliente(dto.getNroDoc(), dto.getNombre(), dto.getApellido(),
                    dto.getRazonSocial(), dto.getDireccion(), dto.getTelefono(), barrio));
        }
    }

    private void cargarEmpleados() {
        String sql = "SELECT nro_doc, nombre, apellido, tipo_doc, razon_social, cargo, capacidad_maxima FROM empleado";
        try (Connection conn = DatabaseManager.getInstancia().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                empleados.add(new Empleado(rs.getString("nombre"), rs.getString("apellido"),
                        rs.getString("tipo_doc"), rs.getString("nro_doc"),
                        rs.getString("razon_social"),
                        Cargo.valueOf(rs.getString("cargo")), rs.getInt("capacidad_maxima")));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar empleados", e);
        }
    }

    private void cargarPedidos() {
        List<PedidoDTO> dtos = pedidoDAO.listarTodos();
        for (PedidoDTO dto : dtos) {
            Cliente cliente = clientes.stream()
                    .filter(c -> c.getNroDoc().equals(dto.getNroDocCliente()))
                    .findFirst().orElse(null);
            if (cliente == null) continue;

            List<DetallePedido> detalles = new ArrayList<>();
            if (dto.getDetalles() != null) {
                for (DetallePedidoDTO detDTO : dto.getDetalles()) {
                    Producto prod = productos.stream()
                            .filter(p -> p.getCodigo().equals(detDTO.getCodProducto()))
                            .findFirst().orElse(null);
                    if (prod != null) {
                        detalles.add(new DetallePedido(prod, detDTO.getCantidad(), detDTO.getPrecioUnitario()));
                    }
                }
            }

            Pedido pedido = new Pedido(dto.getNroPedido(), dto.getFechaEmision(),
                    dto.getFechaEntrega(), cliente, detalles);

            if (dto.getObservaciones() != null && !dto.getObservaciones().isEmpty()) {
                pedido.registrarObservaciones(dto.getObservaciones());
            }

            restaurarEstadoPedido(pedido, dto);
            pedidos.add(pedido);
        }
    }

    private void restaurarEstadoPedido(Pedido pedido, PedidoDTO dto) {
        String estado = dto.getEstado();
        boolean pagoOk = dto.isPagoProcesado();

        if ("ENTREGADO".equals(estado) && dto.getMetodoPago() != null) {
            String mp = dto.getMetodoPago();
            if (mp.startsWith("Efectivo")) {
                String[] partes = mp.split(" \\$");
                double monto = partes.length > 1 ? Double.parseDouble(partes[1]) : pedido.getTotal();
                pedido.seleccionarMetodoPago(new com.aguasvital.strategy.PagoContado(monto));
            } else {
                pedido.seleccionarMetodoPago(new com.aguasvital.strategy.PagoElectronico("OP-" + dto.getNroPedido(), mp));
            }
            if (pagoOk) pedido.marcarPagoProcesado(true);
            pedido.marcarEntregado();
        } else if ("NO_ENTREGADO".equals(estado)) {
            pedido.marcarNoEntregado();
        }
    }

    public Optional<Cliente> buscarClientePorDni(String dni) {
        return clientes.stream().filter(cliente -> cliente.getDni().equals(dni)).findFirst();
    }

    public Optional<Pedido> buscarPedido(int nroPedido) {
        return pedidos.stream().filter(pedido -> pedido.getNroPedido() == nroPedido).findFirst();
    }

    public Optional<UsuarioSistema> validarUsuario(String usuario, String password) {
        if (!"1234".equals(password)) {
            return Optional.empty();
        }
        return switch (usuario) {
            case "operadora" -> Optional.of(new UsuarioSistema(usuario, Cargo.OPERADORA));
            case "administrativo" -> Optional.of(new UsuarioSistema(usuario, Cargo.ENCARGADO_ADMINISTRATIVO));
            case "distribuidor" -> Optional.of(new UsuarioSistema(usuario, Cargo.DISTRIBUIDOR));
            case "presidente" -> Optional.of(new UsuarioSistema(usuario, Cargo.PRESIDENTE));
            default -> Optional.empty();
        };
    }

    public void persistirPedido(Pedido pedido) {
        PedidoDTO dto = convertirAPedidoDTO(pedido);
        pedidoDAO.modificar(dto);
    }

    public void insertarPedido(Pedido pedido) {
        PedidoDTO dto = convertirAPedidoDTOCompleto(pedido);
        pedidoDAO.insertar(dto);
        pedidos.add(pedido);
    }

    private PedidoDTO convertirAPedidoDTO(Pedido pedido) {
        PedidoDTO dto = new PedidoDTO();
        dto.setNroPedido(pedido.getNroPedido());
        dto.setFechaEmision(pedido.getFechaEmision());
        dto.setFechaEntrega(pedido.getFechaEntrega());
        dto.setEstado(pedido.getEstadoNombre());
        dto.setTotal(pedido.getTotal());
        dto.setNroDocCliente(pedido.getCliente().getNroDoc());
        dto.setNombreCliente(pedido.getNombreCliente());
        dto.setDireccionCliente(pedido.getDireccionCliente());
        dto.setBarrioCliente(pedido.getBarrioCliente());
        dto.setZonaCliente(pedido.getZonaCliente());
        dto.setMetodoPago(pedido.getMetodoPago() != null ? pedido.getMetodoPago().obtenerDescripcion() : null);
        dto.setPagoProcesado(pedido.isPagoProcesado());
        dto.setObservaciones(pedido.getObservaciones());
        return dto;
    }

    private PedidoDTO convertirAPedidoDTOCompleto(Pedido pedido) {
        PedidoDTO dto = convertirAPedidoDTO(pedido);
        List<DetallePedidoDTO> detallesDTO = new ArrayList<>();
        for (DetallePedido det : pedido.getDetalles()) {
            DetallePedidoDTO detDTO = new DetallePedidoDTO();
            detDTO.setNroPedido(pedido.getNroPedido());
            detDTO.setCodProducto(det.getProducto().getCodigo());
            detDTO.setNombreProducto(det.getNombreProducto());
            detDTO.setCantidad(det.getCantidad());
            detDTO.setPrecioUnitario(det.getPrecioUnitarioHistorico());
            detDTO.setSubtotal(det.getSubtotal());
            detallesDTO.add(detDTO);
        }
        dto.setDetalles(detallesDTO);
        return dto;
    }

    public List<Zona> getZonas() { return zonas; }
    public List<Barrio> getBarrios() { return barrios; }
    public List<Cliente> getClientes() { return clientes; }
    public List<Producto> getProductos() { return productos; }
    public List<Pedido> getPedidos() { return pedidos; }
    public List<Empleado> getEmpleados() { return empleados; }
}
