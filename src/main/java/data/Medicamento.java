package data;

import java.io.Serializable;

public class Medicamento implements Serializable {
    private int idCita; // Para mantener la relación con el Maestro (Cita)
    private String nombre;
    private double precio;
    private int cantidad;

    public Medicamento() {
        this.cantidad = 1;
    }

    public Medicamento(String nombre, double precio, int cantidad) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return this.precio * this.cantidad;
    }

    // --- Getters y Setters ---
    public int getIdCita() { return idCita; }
    public void setIdCita(int idCita) { this.idCita = idCita; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}