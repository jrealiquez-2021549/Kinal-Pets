
package org.juliorealiquez.models;
import java.sql.Date;

public class Facturas {
    private int codigoFactura;
    private java.sql.Date fechaEmision;
    private double total;
    private String metodoPago;
    private int codigoCliente;
    private int codigoEmpleado;
    
    public Facturas() {
        
    }
    
    public Facturas(int codigoFactura, Date fechaEmision, double total, String metodoPago, int codigoCliente, int codigoEmpleado) {
        this.codigoFactura = codigoFactura;
        this.fechaEmision = fechaEmision;
        this.total = total;
        this.metodoPago = metodoPago;
        this.codigoCliente = codigoCliente;
        this.codigoEmpleado = codigoEmpleado;
    }

    public int getCodigoFactura() {
        return codigoFactura;
    }

    public void setCodigoFactura(int codigoFactura) {
        this.codigoFactura = codigoFactura;
    }

    public java.sql.Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(java.sql.Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public int getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(int codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public int getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(int codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }
    
}
