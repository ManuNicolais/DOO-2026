package com.aguasvital.dao;

import com.aguasvital.dto.DetallePedidoDTO;
import com.aguasvital.dto.PedidoDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PedidoDAOImpl implements Dao<PedidoDTO> {

    @Override
    public Optional<PedidoDTO> buscar(int nroPedido) {
        String sql = "SELECT p.nro_pedido, p.fecha_emision, p.fecha_entrega, p.estado, p.total, " +
                "p.nro_doc_cliente, p.metodo_pago, p.pago_procesado, p.observaciones, " +
                "c.nombre AS cli_nombre, c.apellido AS cli_apellido, c.direccion AS cli_direccion, " +
                "b.nombre AS barrio_nombre, z.nombre AS zona_nombre " +
                "FROM pedido p " +
                "JOIN cliente c ON c.nro_doc = p.nro_doc_cliente " +
                "JOIN barrio b ON b.id_barrio = c.id_barrio " +
                "JOIN zona z ON z.id_zona = b.id_zona " +
                "WHERE p.nro_pedido = ?";
        try (Connection conn = DatabaseManager.getInstancia().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nroPedido);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PedidoDTO dto = filaAPedidoDTO(rs);
                    dto.setDetalles(buscarDetalles(conn, nroPedido));
                    return Optional.of(dto);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar pedido", e);
        }
        return Optional.empty();
    }

    @Override
    public List<PedidoDTO> listarTodos() {
        String sql = "SELECT p.nro_pedido, p.fecha_emision, p.fecha_entrega, p.estado, p.total, " +
                "p.nro_doc_cliente, p.metodo_pago, p.pago_procesado, p.observaciones, " +
                "c.nombre AS cli_nombre, c.apellido AS cli_apellido, c.direccion AS cli_direccion, " +
                "b.nombre AS barrio_nombre, z.nombre AS zona_nombre " +
                "FROM pedido p " +
                "JOIN cliente c ON c.nro_doc = p.nro_doc_cliente " +
                "JOIN barrio b ON b.id_barrio = c.id_barrio " +
                "JOIN zona z ON z.id_zona = b.id_zona " +
                "ORDER BY p.nro_pedido";
        List<PedidoDTO> lista = new ArrayList<>();
        try (Connection conn = DatabaseManager.getInstancia().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                PedidoDTO dto = filaAPedidoDTO(rs);
                dto.setDetalles(buscarDetalles(conn, dto.getNroPedido()));
                lista.add(dto);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al listar pedidos", e);
        }
        return lista;
    }

    @Override
    public void insertar(PedidoDTO dto) {
        String sqlPedido = "INSERT INTO pedido (nro_pedido, fecha_emision, fecha_entrega, estado, total, " +
                "nro_doc_cliente, metodo_pago, pago_procesado, observaciones) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_pedido (nro_pedido, cod_producto, cantidad, precio_unitario) " +
                "VALUES (?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = DatabaseManager.getInstancia().getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sqlPedido)) {
                ps.setInt(1, dto.getNroPedido());
                ps.setString(2, dto.getFechaEmision().toString());
                ps.setString(3, dto.getFechaEntrega().toString());
                ps.setString(4, dto.getEstado());
                ps.setDouble(5, dto.getTotal());
                ps.setString(6, dto.getNroDocCliente());
                ps.setString(7, dto.getMetodoPago());
                ps.setInt(8, dto.isPagoProcesado() ? 1 : 0);
                ps.setString(9, dto.getObservaciones() != null ? dto.getObservaciones() : "");
                ps.executeUpdate();
            }

            if (dto.getDetalles() != null) {
                try (PreparedStatement ps = conn.prepareStatement(sqlDetalle)) {
                    for (DetallePedidoDTO det : dto.getDetalles()) {
                        ps.setInt(1, dto.getNroPedido());
                        ps.setString(2, det.getCodProducto());
                        ps.setInt(3, det.getCantidad());
                        ps.setDouble(4, det.getPrecioUnitario());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }

            conn.commit();
        } catch (Exception e) {
            if (conn != null) {
                try { conn.rollback(); } catch (Exception ex) { /* silent */ }
            }
            throw new RuntimeException("Error al insertar pedido", e);
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (Exception e) { /* silent */ }
            }
        }
    }

    @Override
    public void modificar(PedidoDTO dto) {
        String sql = "UPDATE pedido SET fecha_entrega=?, estado=?, metodo_pago=?, " +
                "pago_procesado=?, observaciones=? WHERE nro_pedido=?";
        try (Connection conn = DatabaseManager.getInstancia().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dto.getFechaEntrega().toString());
            ps.setString(2, dto.getEstado());
            ps.setString(3, dto.getMetodoPago());
            ps.setInt(4, dto.isPagoProcesado() ? 1 : 0);
            ps.setString(5, dto.getObservaciones());
            ps.setInt(6, dto.getNroPedido());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error al modificar pedido", e);
        }
    }

    @Override
    public void borrar(int id) {
        String sqlDet = "DELETE FROM detalle_pedido WHERE nro_pedido=?";
        String sqlPed = "DELETE FROM pedido WHERE nro_pedido=?";
        try (Connection conn = DatabaseManager.getInstancia().getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sqlDet)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement(sqlPed)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
            conn.commit();
        } catch (Exception e) {
            throw new RuntimeException("Error al borrar pedido", e);
        }
    }

    public int generarNroPedido() {
        String sql = "SELECT COALESCE(MAX(nro_pedido), 0) + 1 FROM pedido";
        try (Connection conn = DatabaseManager.getInstancia().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            throw new RuntimeException("Error al generar nro de pedido", e);
        }
        return 1;
    }

    private List<DetallePedidoDTO> buscarDetalles(Connection conn, int nroPedido) throws Exception {
        String sql = "SELECT d.id, d.cod_producto, pr.nombre AS nom_producto, " +
                "d.cantidad, d.precio_unitario " +
                "FROM detalle_pedido d " +
                "JOIN producto pr ON pr.codigo = d.cod_producto " +
                "WHERE d.nro_pedido = ?";
        List<DetallePedidoDTO> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nroPedido);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetallePedidoDTO det = new DetallePedidoDTO();
                    det.setId(rs.getInt("id"));
                    det.setNroPedido(nroPedido);
                    det.setCodProducto(rs.getString("cod_producto"));
                    det.setNombreProducto(rs.getString("nom_producto"));
                    det.setCantidad(rs.getInt("cantidad"));
                    det.setPrecioUnitario(rs.getDouble("precio_unitario"));
                    det.setSubtotal(det.getCantidad() * det.getPrecioUnitario());
                    lista.add(det);
                }
            }
        }
        return lista;
    }

    private PedidoDTO filaAPedidoDTO(ResultSet rs) throws Exception {
        PedidoDTO dto = new PedidoDTO();
        dto.setNroPedido(rs.getInt("nro_pedido"));
        dto.setFechaEmision(LocalDate.parse(rs.getString("fecha_emision")));
        dto.setFechaEntrega(LocalDate.parse(rs.getString("fecha_entrega")));
        dto.setEstado(rs.getString("estado"));
        dto.setTotal(rs.getDouble("total"));
        dto.setNroDocCliente(rs.getString("nro_doc_cliente"));
        dto.setNombreCliente(rs.getString("cli_nombre") + " " + rs.getString("cli_apellido"));
        dto.setDireccionCliente(rs.getString("cli_direccion"));
        dto.setBarrioCliente(rs.getString("barrio_nombre"));
        dto.setZonaCliente(rs.getString("zona_nombre"));
        dto.setMetodoPago(rs.getString("metodo_pago"));
        dto.setPagoProcesado(rs.getInt("pago_procesado") == 1);
        dto.setObservaciones(rs.getString("observaciones"));
        return dto;
    }
}
