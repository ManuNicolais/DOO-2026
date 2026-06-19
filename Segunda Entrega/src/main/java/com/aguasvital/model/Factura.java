package com.aguasvital.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Opcional - para futura implementacion.
 * Actualmente el sistema registra pedidos y pagos sin generar facturas.
 * Esta clase esta lista para usarse cuando se requiera emision de comprobantes.
 */
public class Factura {
    private final String nroFactura;
    private final Pedido pedido;
    private final LocalDate fechaEmision;
    private final List<DetalleFactura> detalle = new ArrayList<>();

    public Factura(String nroFactura, Pedido pedido) {
        this.nroFactura = nroFactura;
        this.pedido = pedido;
        this.fechaEmision = LocalDate.now();
        pedido.getDetalles().forEach(detallePedido -> detalle.add(new DetalleFactura(detallePedido)));
    }

    public String getNroFactura() {
        return nroFactura;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public List<DetalleFactura> getDetalle() {
        return detalle;
    }
}
