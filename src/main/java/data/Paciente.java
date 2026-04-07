package data;

import java.util.Date;
import java.io.Serializable;

public class Paciente implements Serializable {
    private int id;
    private String nombre;
    private String identidad;
    private String historialClinico;
    private Date fechaNacimiento;
    private boolean activo;

    public Paciente() {
        this.id = 0;
        this.nombre = "";
        this.identidad = "";
        this.historialClinico = "";
        this.activo = true;
    }

    public Paciente(String nombre, String identidad, String historialClinico, boolean activo) {
        this.nombre = nombre;
        this.identidad = identidad;
        this.historialClinico = historialClinico;
        this.activo = activo;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getIdentidad() { return identidad; }
    public void setIdentidad(String identidad) { this.identidad = identidad; }

    public String getHistorialClinico() { return historialClinico; }
    public void setHistorialClinico(String historialClinico) { this.historialClinico = historialClinico; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public Date getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(Date fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }


    public Paciente copiar() {
        Paciente copia = new Paciente();
        copia.setId(this.id);
        copia.setNombre(this.nombre);
        copia.setIdentidad(this.identidad);
        copia.setHistorialClinico(this.historialClinico);
        copia.setFechaNacimiento(this.fechaNacimiento);
        copia.setActivo(this.activo);
        return copia;
    }
}