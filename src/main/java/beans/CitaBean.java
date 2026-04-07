package beans;

import data.Cita;
import data.Paciente;
import database.ConexionBD;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named(value = "citaBean")
@ViewScoped
public class CitaBean implements Serializable {

    private List<Cita> listaCitas;
    private List<Paciente> listaPacientes; // Para llenar el combo box
    private Cita nuevaCita;
    private Cita citaSeleccionada;
    private ConexionBD conexion;

    public CitaBean() {
        this.conexion = new ConexionBD();
        this.nuevaCita = new Cita();
    }

    @PostConstruct
    public void init() {
        listarTodo();
    }

    public void listarTodo() {
        this.listaCitas = conexion.consultarCitas();
        this.listaPacientes = conexion.consultarPacientes(); // Necesitamos los pacientes para el formulario
    }

    public void guardar() {
        try {
            // Verificamos que se haya seleccionado un paciente
            if (nuevaCita.getPaciente().getId() == 0) {
                throw new Exception("Debe seleccionar un paciente");
            }

            conexion.crearCita(nuevaCita);
            listarTodo();
            nuevaCita = new Cita(); // Reset del formulario

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Cita programada correctamente"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    public void resolverCita() {
        if (citaSeleccionada != null) {
            conexion.actualizarEstadoCita(citaSeleccionada.getIdCita(), true);
            listarTodo();
        }
    }

    public List<Cita> getListaCitas() { return listaCitas; }
    public List<Paciente> getListaPacientes() { return listaPacientes; }
    public Cita getNuevaCita() { return nuevaCita; }
    public void setNuevaCita(Cita nuevaCita) { this.nuevaCita = nuevaCita; }
    public Cita getCitaSeleccionada() { return citaSeleccionada; }
    public void setCitaSeleccionada(Cita citaSeleccionada) { this.citaSeleccionada = citaSeleccionada; }
}