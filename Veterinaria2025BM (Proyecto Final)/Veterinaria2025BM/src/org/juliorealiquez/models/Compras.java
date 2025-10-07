
package org.juliorealiquez.models;
import java.sql.Date;

public class Compras {
    private int codigoCompra;
    private java.sql.Date fechaCompra;
    private int total;
    private String detalle;
    private int codigoProveedor;
    
    public Compras() {
        
    }
    
    public Compras(int codigoCompra, Date fechaCompra, int total, String detalle, int codigoProveedor) {
        this.codigoCompra = codigoCompra;
        this.fechaCompra = fechaCompra;
        this.total = total;
        this.detalle = detalle;
        this.codigoProveedor = codigoProveedor;
    }

    public int getCodigoCompra() {
        return codigoCompra;
    }

    public void setCodigoCompra(int codigoCompra) {
        this.codigoCompra = codigoCompra;
    }

    public java.sql.Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(java.sql.Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public int getCodigoProveedor() {
        return codigoProveedor;
    }

    public void setCodigoProveedor(int codigoProveedor) {
        this.codigoProveedor = codigoProveedor;
    }
    
}
