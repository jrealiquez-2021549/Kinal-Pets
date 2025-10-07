
package org.juliorealiquez.models;
import java.sql.Date;

public class Veterinarios {
    private int codigoVeterinario;
    private String nombreVeterinario;
    private String apellidoVeterinario;
    private String especialidad;
    private String telefonoVeterinario;
    private String correoVeterinario;
    private java.sql.Date fechaIngreso;
    private String estado;
    
    public Veterinarios() {
        
    }

    public Veterinarios(int codigoVeterinario, String nombreVeterinario, String apellidoVeterinario, String especialidad, String telefonoVeterinario, String correoVeterinario, Date fechaIngreso, String estado) {
        this.codigoVeterinario = codigoVeterinario;
        this.nombreVeterinario = nombreVeterinario;
        this.apellidoVeterinario = apellidoVeterinario;
        this.especialidad = especialidad;
        this.telefonoVeterinario = telefonoVeterinario;
        this.correoVeterinario = correoVeterinario;
        this.fechaIngreso = fechaIngreso;
        this.estado = estado;
    }

    public int getCodigoVeterinario() {
        return codigoVeterinario;
    }

    public void setCodigoVeterinario(int codigoVeterinario) {
        this.codigoVeterinario = codigoVeterinario;
    }

    public String getNombreVeterinario() {
        return nombreVeterinario;
    }

    public void setNombreVeterinario(String nombreVeterinario) {
        this.nombreVeterinario = nombreVeterinario;
    }

    public String getApellidoVeterinario() {
        return apellidoVeterinario;
    }

    public void setApellidoVeterinario(String apellidoVeterinario) {
        this.apellidoVeterinario = apellidoVeterinario;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getTelefonoVeterinario() {
        return telefonoVeterinario;
    }

    public void setTelefonoVeterinario(String telefonoVeterinario) {
        this.telefonoVeterinario = telefonoVeterinario;
    }

    public String getCorreoVeterinario() {
        return correoVeterinario;
    }

    public void setCorreoVeterinario(String correoVeterinario) {
        this.correoVeterinario = correoVeterinario;
    }

    public java.sql.Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(java.sql.Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
}
