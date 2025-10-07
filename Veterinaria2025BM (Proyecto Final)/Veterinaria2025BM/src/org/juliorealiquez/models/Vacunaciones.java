
package org.juliorealiquez.models;
import java.sql.Date;

public class Vacunaciones {
    private int codigoVacunacion;
    private java.sql.Date fechaAplicacion;
    private String observaciones;
    private int codigoMascota;
    private int codigoVacuna;
    private int codigoVeterinario;
    
    public Vacunaciones() {
        
    }

    public Vacunaciones(int codigoVacunacion, Date fechaAplicacion, String observaciones, int codigoMascota, int codigoVacuna, int codigoVeterinario) {
        this.codigoVacunacion = codigoVacunacion;
        this.fechaAplicacion = fechaAplicacion;
        this.observaciones = observaciones;
        this.codigoMascota = codigoMascota;
        this.codigoVacuna = codigoVacuna;
        this.codigoVeterinario = codigoVeterinario;
    }

    public int getCodigoVacunacion() {
        return codigoVacunacion;
    }

    public void setCodigoVacunacion(int codigoVacunacion) {
        this.codigoVacunacion = codigoVacunacion;
    }

    public java.sql.Date getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(java.sql.Date fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getCodigoMascota() {
        return codigoMascota;
    }

    public void setCodigoMascota(int codigoMascota) {
        this.codigoMascota = codigoMascota;
    }

    public int getCodigoVacuna() {
        return codigoVacuna;
    }

    public void setCodigoVacuna(int codigoVacuna) {
        this.codigoVacuna = codigoVacuna;
    }

    public int getCodigoVeterinario() {
        return codigoVeterinario;
    }

    public void setCodigoVeterinario(int codigoVeterinario) {
        this.codigoVeterinario = codigoVeterinario;
    }
    
}
