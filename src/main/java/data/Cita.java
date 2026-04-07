package data;

import java.io.Serializable;
import java.util.Date;

public class Cita implements Serializable {
    private int idCita;
    private Paciente paciente; // Relación: Una cita pertenece a UN paciente
    private String sintomas;
    private Date fechaCita;
    private boolean urgente;
    private boolean resuelta;

    public Cita() {
        // Inicializamos el objeto paciente para evitar errores de "Null" en el formulario
        this.paciente = new Paciente();
        this.fechaCita = new Date(); // Fecha actual por defecto
    }

    // --- GETTERS Y SETTERS ---

    public int getIdCita() { return idCita; }
    public void setIdCita(int idCita) { this.idCita = idCita; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public String getSintomas() { return sintomas; }
    public void setSintomas(String sintomas) { this.sintomas = sintomas; }

    public Date getFechaCita() { return fechaCita; }
    public void setFechaCita(Date fechaCita) { this.fechaCita = fechaCita; }

    public boolean isUrgente() { return urgente; }
    public void setUrgente(boolean urgente) { this.urgente = urgente; }

    public boolean isResuelta() { return resuelta; }
    public void setResuelta(boolean resuelta) { this.resuelta = resuelta; }
}