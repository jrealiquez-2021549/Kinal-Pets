
package org.juliorealiquez.models;

public class Recetas {
    private int codigoReceta;
    private String dosis;
    private String frecuencia;
    private int duracionDias;
    private String indicaciones;
    private int codigoConsulta;
    private int codigoMedicamento;
    
    public Recetas() {
        
    }
    
    public Recetas(int codigoReceta, String dosis, String frecunecia, int duracionDias, String indicaciones, int codigoConsulta, int codigoMedicamento) {
        this.codigoReceta = codigoReceta;
        this.dosis = dosis;
        this.frecuencia = frecunecia;
        this.duracionDias = duracionDias;
        this.indicaciones = indicaciones;
        this.codigoConsulta = codigoConsulta;
        this.codigoMedicamento = codigoMedicamento;
    }

    public int getCodigoReceta() {
        return codigoReceta;
    }

    public void setCodigoReceta(int codigoReceta) {
        this.codigoReceta = codigoReceta;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public int getDuracionDias() {
        return duracionDias;
    }

    public void setDuracionDias(int duracionDias) {
        this.duracionDias = duracionDias;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    public int getCodigoConsulta() {
        return codigoConsulta;
    }

    public void setCodigoConsulta(int codigoConsulta) {
        this.codigoConsulta = codigoConsulta;
    }

    public int getCodigoMedicamento() {
        return codigoMedicamento;
    }

    public void setCodigoMedicamento(int codigoMedicamento) {
        this.codigoMedicamento = codigoMedicamento;
    }
    
}
