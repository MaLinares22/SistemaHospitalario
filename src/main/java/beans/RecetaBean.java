package beans;

import database.ConexionBD;
import data.Medicamento;
import data.Cita;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.annotation.PostConstruct;

@Named
@SessionScoped
public class RecetaBean implements Serializable {

    private int idCitaSeleccionada;
    private String nombrePaciente; // Para mostrar en el encabezado de la factura
    private Date fechaImpresion;   // Fecha actual para el reporte
    private Medicamento nuevoMed = new Medicamento();
    private List<Medicamento> listaTemporal = new ArrayList<>();
    private double totalGeneral = 0.0;
    private List<Cita> listaCitas = new ArrayList<>();

    private ConexionBD db = new ConexionBD();

    @PostConstruct
    public void init() {
        cargarCitas();
        this.fechaImpresion = new Date();
    }

    public void cargarCitas() {
        this.listaCitas = db.consultarCitasPendientes();
    }

    /**
     * Busca en la BD los medicamentos ya recetados para esa cita
     * y actualiza el nombre del paciente para la vista.
     */
    public void buscarRecetaPorCita() {
        if (idCitaSeleccionada <= 0) {
            enviarMensaje(FacesMessage.SEVERITY_WARN, "Atención", "Seleccione una cita válida");
            return;
        }

        // 1. Buscamos el nombre del paciente en nuestra lista cargada
        for (Cita c : listaCitas) {
            if (c.getIdCita() == idCitaSeleccionada) {
                this.nombrePaciente = c.getPaciente().getNombre();
                break;
            }
        }

        // 2. Traemos los medicamentos de la BD
        this.listaTemporal = db.consultarMedicamentosPorCita(idCitaSeleccionada);
        this.fechaImpresion = new Date(); // Actualizamos a la hora de consulta
        recalcularTotal();

        if (listaTemporal.isEmpty()) {
            enviarMensaje(FacesMessage.SEVERITY_INFO, "Nota", "No hay medicamentos previos. Puede agregar nuevos.");
        }
    }

    public void agregarMedicamento() {
        if (nuevoMed.getNombre() == null || nuevoMed.getNombre().trim().isEmpty()) {
            enviarMensaje(FacesMessage.SEVERITY_ERROR, "Error", "El nombre es obligatorio");
            return;
        }
        if (nuevoMed.getPrecio() <= 0) {
            enviarMensaje(FacesMessage.SEVERITY_WARN, "Atención", "El precio debe ser mayor a 0");
            return;
        }
        if (nuevoMed.getCantidad() <= 0) {
            enviarMensaje(FacesMessage.SEVERITY_WARN, "Atención", "La cantidad debe ser al menos 1");
            return;
        }

        listaTemporal.add(new Medicamento(nuevoMed.getNombre(), nuevoMed.getPrecio(), nuevoMed.getCantidad()));
        recalcularTotal();

        nuevoMed = new Medicamento(); // Se resetea (cantidad vuelve a 1 por constructor de Medicamento)
        enviarMensaje(FacesMessage.SEVERITY_INFO, "Agregado", "Medicamento añadido a la lista");
    }

    private void recalcularTotal() {
        totalGeneral = listaTemporal.stream()
                .mapToDouble(m -> m.getPrecio() * m.getCantidad())
                .sum();
    }

    public void guardarFacturaYReceta() {
        if (idCitaSeleccionada <= 0 || listaTemporal.isEmpty()) {
            enviarMensaje(FacesMessage.SEVERITY_ERROR, "Error", "Faltan datos para guardar");
            return;
        }

        try {
            db.crearRecetas(idCitaSeleccionada, listaTemporal);
            enviarMensaje(FacesMessage.SEVERITY_INFO, "Éxito", "Cambios guardados correctamente");

            // Refrescar lista por si se cerró la cita y limpiar
            cargarCitas();
        } catch (Exception e) {
            enviarMensaje(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage());
        }
    }

    public void limpiarFormularioCompleto() {
        listaTemporal.clear();
        totalGeneral = 0.0;
        idCitaSeleccionada = 0;
        nombrePaciente = "";
        nuevoMed = new Medicamento();
    }

    private void enviarMensaje(FacesMessage.Severity severidad, String resumen, String detalle) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severidad, resumen, detalle));
    }

    // --- GETTERS Y SETTERS ---
    public int getIdCitaSeleccionada() { return idCitaSeleccionada; }
    public void setIdCitaSeleccionada(int idCitaSeleccionada) { this.idCitaSeleccionada = idCitaSeleccionada; }
    public String getNombrePaciente() { return nombrePaciente; }
    public Date getFechaImpresion() { return fechaImpresion; }
    public Medicamento getNuevoMed() { return nuevoMed; }
    public void setNuevoMed(Medicamento nuevoMed) { this.nuevoMed = nuevoMed; }
    public List<Medicamento> getListaTemporal() { return listaTemporal; }
    public double getTotalGeneral() { return totalGeneral; }
    public List<Cita> getListaCitas() { return listaCitas; }
}