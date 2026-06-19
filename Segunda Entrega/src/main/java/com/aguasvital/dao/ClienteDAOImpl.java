package com.aguasvital.dao;

import com.aguasvital.dto.ClienteDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteDAOImpl implements Dao<ClienteDTO> {

    @Override
    public Optional<ClienteDTO> buscar(int id) {
        return Optional.empty();
    }

    public Optional<ClienteDTO> buscarPorDni(String dni) {
        String sql = "SELECT c.nro_doc, c.nombre, c.apellido, c.razon_social, " +
                "c.direccion, c.telefono, c.id_barrio " +
                "FROM cliente c WHERE c.nro_doc = ?";
        try (Connection conn = DatabaseManager.getInstancia().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(filaADTO(rs));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar cliente por DNI", e);
        }
        return Optional.empty();
    }

    @Override
    public List<ClienteDTO> listarTodos() {
        String sql = "SELECT c.nro_doc, c.nombre, c.apellido, c.razon_social, " +
                "c.direccion, c.telefono, c.id_barrio FROM cliente c";
        List<ClienteDTO> lista = new ArrayList<>();
        try (Connection conn = DatabaseManager.getInstancia().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(filaADTO(rs));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al listar clientes", e);
        }
        return lista;
    }

    @Override
    public void insertar(ClienteDTO dto) {
        String sql = "INSERT INTO cliente (nro_doc, nombre, apellido, razon_social, direccion, telefono, id_barrio) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getInstancia().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dto.getNroDoc());
            ps.setString(2, dto.getNombre());
            ps.setString(3, dto.getApellido());
            ps.setString(4, dto.getRazonSocial());
            ps.setString(5, dto.getDireccion());
            ps.setString(6, dto.getTelefono());
            ps.setString(7, dto.getIdBarrio());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error al insertar cliente", e);
        }
    }

    @Override
    public void modificar(ClienteDTO dto) {
        String sql = "UPDATE cliente SET nombre=?, apellido=?, razon_social=?, direccion=?, telefono=?, id_barrio=? " +
                "WHERE nro_doc=?";
        try (Connection conn = DatabaseManager.getInstancia().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dto.getNombre());
            ps.setString(2, dto.getApellido());
            ps.setString(3, dto.getRazonSocial());
            ps.setString(4, dto.getDireccion());
            ps.setString(5, dto.getTelefono());
            ps.setString(6, dto.getIdBarrio());
            ps.setString(7, dto.getNroDoc());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error al modificar cliente", e);
        }
    }

    @Override
    public void borrar(int id) {
    }

    private ClienteDTO filaADTO(ResultSet rs) throws Exception {
        ClienteDTO dto = new ClienteDTO();
        dto.setNroDoc(rs.getString("nro_doc"));
        dto.setNombre(rs.getString("nombre"));
        dto.setApellido(rs.getString("apellido"));
        dto.setRazonSocial(rs.getString("razon_social"));
        dto.setDireccion(rs.getString("direccion"));
        dto.setTelefono(rs.getString("telefono"));
        dto.setIdBarrio(rs.getString("id_barrio"));
        return dto;
    }
}
