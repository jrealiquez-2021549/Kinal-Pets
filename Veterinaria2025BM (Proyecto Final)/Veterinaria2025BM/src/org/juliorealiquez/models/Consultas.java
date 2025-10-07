
package org.juliorealiquez.models;
import java.sql.Date;

public class Consultas {
    private int codigoConsulta;
    private java.sql.Date fechaConsulta;
    private String motivo;
    private String diagnostico;
    private String observaciones;
    private int codigoMascota;
    private int codigoVeterinario;
    
    public Consultas() {
        
    }
    
    public Consultas(int codigoConsulta, Date fechaConsulta, String motivo, String diagnostico, String observaciones, int codigoMascota, int codigoVeterinario) {
        this.codigoConsulta = codigoConsulta;
        this.fechaConsulta = fechaConsulta;
        this.motivo = motivo;
        this.diagnostico = diagnostico;
        this.observaciones = observaciones;
        this.codigoMascota = codigoMascota;
        this.codigoVeterinario = codigoVeterinario;
    }

    public int getCodigoConsulta() {
        return codigoConsulta;
    }

    public void setCodigoConsulta(int codigoConsulta) {
        this.codigoConsulta = codigoConsulta;
    }

    public java.sql.Date getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(java.sql.Date fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
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

    public int getCodigoVeterinario() {
        return codigoVeterinario;
    }

    public void setCodigoVeterinario(int codigoVeterinario) {
        this.codigoVeterinario = codigoVeterinario;
    }
    
}
