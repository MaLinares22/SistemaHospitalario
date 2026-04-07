package beans;

import data.Paciente;
import database.ConexionBD;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("HospitalBean")
@ViewScoped
public class HospitalBean implements Serializable {

    private List<Paciente> listaPacientes;
    private Paciente pacienteActual;
    private Paciente seleccionado;
    private ConexionBD db;
    private String textoBoton;

    public HospitalBean() {
        db = new ConexionBD();
        this.pacienteActual = new Paciente();
        this.textoBoton = "Registrar Paciente";
        cargarDatos();
    }

    public void cargarDatos() {
        this.listaPacientes = db.consultarPacientes();
    }

    public void guardar() {
        if (pacienteActual.getNombre() == null || pacienteActual.getNombre().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El nombre es obligatorio"));
            return;
        }

        if (this.seleccionado == null) {
            db.crearPaciente(pacienteActual);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Paciente registrado correctamente"));
        } else {
            db.actualizarPaciente(pacienteActual);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Actualizado", "Datos del paciente actualizados"));
        }

        limpiarFormulario();
        cargarDatos(); // Refrescar la tabla
    }

    public void prepararEdicion() {
        if (seleccionado != null) {
            this.pacienteActual = seleccionado.copiar();
            this.textoBoton = "Actualizar Datos";
        }
    }

    public void eliminar() {
        if (seleccionado != null) {
            db.eliminarPaciente(seleccionado);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Eliminado", "El paciente ha sido inactivado"));
            cargarDatos();
            this.seleccionado = null;
        }
    }

    public void limpiarFormulario() {
        this.pacienteActual = new Paciente();
        this.seleccionado = null;
        this.textoBoton = "Registrar Paciente";
    }

    public List<Paciente> getListaPacientes() { return listaPacientes; }
    public Paciente getPacienteActual() { return pacienteActual; }
    public void setPacienteActual(Paciente pacienteActual) { this.pacienteActual = pacienteActual; }
    public Paciente getSeleccionado() { return seleccionado; }
    public void setSeleccionado(Paciente seleccionado) { this.seleccionado = seleccionado; }
    public String getTextoBoton() { return textoBoton; }
}