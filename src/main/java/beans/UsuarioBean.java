package beans;

import data.Usuario;
import database.ConexionBD;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named(value = "usuarioBean")
@ViewScoped
public class UsuarioBean implements Serializable {

    private List<Usuario> listaUsuarios;
    private Usuario nuevoUsuario;
    private Usuario usuarioSeleccionado;
    private ConexionBD conexion;

    public UsuarioBean() {
        this.conexion = new ConexionBD();
        this.nuevoUsuario = new Usuario();
    }

    @PostConstruct
    public void init() {
        listar();
    }

    public void listar() {
        this.listaUsuarios = conexion.consultarUsuarios();
    }

    public void guardar() {
        try {
            conexion.crearUsuario(nuevoUsuario);
            listar(); // Refrescar la tabla
            nuevoUsuario = new Usuario(); // Limpiar el formulario
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Usuario creado correctamente"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo crear el usuario"));
        }
    }

    public void eliminar() {
        if (usuarioSeleccionado != null) {
            conexion.eliminarUsuario(usuarioSeleccionado.getId());
            listar();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Eliminado", "Usuario removido del sistema"));
        }
    }

    // --- GETTERS Y SETTERS ---

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public Usuario getNuevoUsuario() {
        return nuevoUsuario;
    }

    public void setNuevoUsuario(Usuario nuevoUsuario) {
        this.nuevoUsuario = nuevoUsuario;
    }

    public Usuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }
}