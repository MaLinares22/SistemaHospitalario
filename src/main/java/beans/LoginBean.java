package beans;

import database.ConexionBD;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("LoginBean")
@SessionScoped
public class LoginBean implements Serializable {
    private String usuario;
    private String clave;
    private ConexionBD db = new ConexionBD();

    public String autenticar() {
        if (db.validarUsuario(usuario, clave)) {
            return "inicio?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Usuario o clave incorrectos"));
            return null;
        }
    }


    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public String salir() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index?faces-redirect=true";
    }
}
