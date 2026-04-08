package database;

import data.Paciente;
import data.Usuario;
import data.Cita;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class ConexionBD {
    private Connection conexion;

    public ConexionBD() {
        this.conexion = null;
    }

    private PreparedStatement conectar(String operacionSQL) {
        PreparedStatement operacion = null;
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }


            String userDir = System.getProperty("user.dir");
            String path = "";

            if (userDir.endsWith("bin")) {
                path = userDir.replace("bin", "webapps") + "/Hospital/bd/bdHospital1.accdb";
            } else {
                path = userDir + "/bd/bdHospital1.accdb";
            }

            java.io.File archivo = new java.io.File(path);
            if (!archivo.exists()) {
                path = "C:/proyects/prograweb2/Hospital/bd/bdHospital1.accdb";
            }

            String url = "jdbc:ucanaccess://" + path;
            this.conexion = DriverManager.getConnection(url);
            operacion = this.conexion.prepareStatement(operacionSQL);

        } catch (Exception error) {
            System.err.println("Error de conexión: " + error.getMessage());
        }
        return operacion;
    }

    public List<Paciente> consultarPacientes() {
        List<Paciente> lista = new ArrayList<>();
        try {

            PreparedStatement operacion = conectar("SELECT * FROM pacientes WHERE activo = true");
            ResultSet rs = operacion.executeQuery();
            while (rs.next()) {
                Paciente p = new Paciente();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setIdentidad(rs.getString("identidad"));
                p.setHistorialClinico(rs.getString("historial_clinico"));
                p.setActivo(rs.getBoolean("activo"));
                p.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
                lista.add(p);
            }
            conexion.close();
        } catch (Exception error) {
            System.err.println("Error al consultar pacientes: " + error.getMessage());
        }
        return lista;
    }

    public void crearPaciente(Paciente nuevo) {
        try {
            String sql = "INSERT INTO pacientes (nombre, identidad, historial_clinico, fecha_registro, activo, fecha_nacimiento) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement operacion = conectar(sql);

            if (operacion != null) {
                operacion.setString(1, nuevo.getNombre());

                operacion.setString(2, nuevo.getIdentidad());

                operacion.setString(3, nuevo.getHistorialClinico());

                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
                cal.set(java.util.Calendar.MINUTE, 0);
                cal.set(java.util.Calendar.SECOND, 0);
                cal.set(java.util.Calendar.MILLISECOND, 0);
                java.sql.Date fechaHoy = new java.sql.Date(cal.getTimeInMillis());
                operacion.setDate(4, fechaHoy);


                operacion.setBoolean(5, true);


                if (nuevo.getFechaNacimiento() != null) {
                    java.sql.Date fechaNac = new java.sql.Date(nuevo.getFechaNacimiento().getTime());
                    operacion.setDate(6, fechaNac);
                } else {
                    operacion.setNull(6, java.sql.Types.DATE);
                }

                operacion.executeUpdate();


                if (this.conexion != null && !this.conexion.isClosed()) {
                    this.conexion.close();
                }

                System.out.println("¡Paciente guardado correctamente!");
            }
        } catch (Exception error) {
            System.err.println("Error al insertar paciente: " + error.getMessage());
            error.printStackTrace();
        }
    }

    public void actualizarPaciente(Paciente existente) {
        try {
            PreparedStatement operacion = conectar("UPDATE pacientes SET nombre = ?, identidad = ?, historial_clinico = ? WHERE id = ?");
            operacion.setString(1, existente.getNombre());
            operacion.setString(2, existente.getIdentidad());
            operacion.setString(3, existente.getHistorialClinico());
            operacion.setInt(4, existente.getId());

            operacion.executeUpdate();
            conexion.close();
        } catch (Exception error) {
            System.err.println("Error al actualizar paciente: " + error.getMessage());
        }
    }

    public void eliminarPaciente(Paciente existente) {
        try {

            PreparedStatement operacion = conectar("UPDATE pacientes SET activo = false WHERE id = ?");
            operacion.setInt(1, existente.getId());

            operacion.executeUpdate();
            conexion.close();
        } catch (Exception error) {
            System.err.println("Error al inactivar paciente: " + error.getMessage());
        }
    }

    public boolean validarUsuario(String user, String pass) {
        boolean loginExitoso = false;
        try {
            PreparedStatement operacion = conectar("SELECT * FROM usuarios WHERE username = ? AND password = ?");
            operacion.setString(1, user);
            operacion.setString(2, pass);
            ResultSet rs = operacion.executeQuery();

            if (rs.next()) {
                loginExitoso = true;
            }
            conexion.close();
        } catch (Exception error) {
            System.err.println("Error en login: " + error.getMessage());
        }
        return loginExitoso;
    }

    public List<Usuario> consultarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        try {
            PreparedStatement operacion = conectar("SELECT * FROM usuarios");
            ResultSet rs = operacion.executeQuery();
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                lista.add(u);
            }
            conexion.close();
        } catch (Exception e) {
            System.err.println("Error al consultar usuarios: " + e.getMessage());
        }
        return lista;
    }

    public void crearUsuario(Usuario nuevo) {
        try {
            String sql = "INSERT INTO usuarios (username, password) VALUES (?, ?)";
            PreparedStatement operacion = conectar(sql);
            if (operacion != null) {
                operacion.setString(1, nuevo.getUsername());
                operacion.setString(2, nuevo.getPassword());
                operacion.executeUpdate();
                conexion.close();
            }
        } catch (Exception e) {
            System.err.println("Error al crear usuario: " + e.getMessage());
        }
    }

    public void eliminarUsuario(int idUsuario) {
        try {
            PreparedStatement operacion = conectar("DELETE FROM usuarios WHERE id = ?");
            operacion.setInt(1, idUsuario);
            operacion.executeUpdate();
            conexion.close();
        } catch (Exception e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
        }
    }

    public List<Cita> consultarCitas() {
        List<Cita> lista = new ArrayList<>();
        try {

            String sql = "SELECT c.*, p.nombre FROM citas c " +
                    "INNER JOIN pacientes p ON c.id_paciente = p.id " +
                    "ORDER BY c.fecha_cita DESC";

            PreparedStatement operacion = conectar(sql);
            ResultSet rs = operacion.executeQuery();
            while (rs.next()) {
                Cita c = new Cita();
                c.setIdCita(rs.getInt("id_cita"));
                c.setSintomas(rs.getString("sintomas"));
                c.setFechaCita(rs.getDate("fecha_cita"));
                c.setUrgente(rs.getBoolean("es_urgente"));
                c.setResuelta(rs.getBoolean("resuelta"));

                c.getPaciente().setId(rs.getInt("id_paciente"));
                c.getPaciente().setNombre(rs.getString("nombre"));

                lista.add(c);
            }
            conexion.close();
        } catch (Exception e) {
            System.err.println("Error al consultar citas: " + e.getMessage());
        }
        return lista;
    }

    public void crearCita(Cita nueva) {
        try {
            String sql = "INSERT INTO citas (id_paciente, sintomas, fecha_cita, es_urgente, resuelta) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement operacion = conectar(sql);
            if (operacion != null) {
                operacion.setInt(1, nueva.getPaciente().getId());
                operacion.setString(2, nueva.getSintomas());

                java.sql.Date fechaSQL = new java.sql.Date(nueva.getFechaCita().getTime());
                operacion.setDate(3, fechaSQL);

                operacion.setBoolean(4, nueva.isUrgente());
                operacion.setBoolean(5, false); // Siempre inicia como no resuelta

                operacion.executeUpdate();
                conexion.close();
            }
        } catch (Exception e) {
            System.err.println("Error al crear cita: " + e.getMessage());
        }
    }

    public void actualizarEstadoCita(int idCita, boolean resuelta) {
        try {

            PreparedStatement operacion = conectar("UPDATE citas SET resuelta = ? WHERE id_cita = ?");
            operacion.setBoolean(1, resuelta);
            operacion.setInt(2, idCita);
            operacion.executeUpdate();
            conexion.close();
        } catch (Exception e) {
            System.err.println("Error al actualizar cita: " + e.getMessage());
        }
    }

    public void eliminarCita(int idCita) {
        try {
            PreparedStatement operacion = conectar("DELETE FROM citas WHERE id_cita = ?");
            operacion.setInt(1, idCita);
            operacion.executeUpdate();
            conexion.close();
        } catch (Exception e) {
            System.err.println("Error al eliminar cita: " + e.getMessage());
        }
    }

    // Agrega esto al final de tu clase ConexionBD.java

    public void crearRecetas(int idCita, List<data.Medicamento> listaMedicamentos) {
        try {
            // SQL para insertar los medicamentos vinculados a la cita
            String sql = "INSERT INTO Recetas (id_cita, nombre_medicamento, precio, cantidad) VALUES (?, ?, ?, ?)";

            // Usamos tu método conectar
            PreparedStatement operacion = conectar(sql);

            if (operacion != null) {
                for (data.Medicamento m : listaMedicamentos) {
                    operacion.setInt(1, idCita);
                    operacion.setString(2, m.getNombre());
                    operacion.setDouble(3, m.getPrecio());
                    operacion.setInt(4, m.getCantidad());
                    operacion.executeUpdate();
                }

                // Cerramos conexión como haces en tus otros métodos
                if (this.conexion != null && !this.conexion.isClosed()) {
                    this.conexion.close();
                }
                System.out.println("¡Receta guardada exitosamente!");
            }
        } catch (Exception error) {
            System.err.println("Error al insertar receta: " + error.getMessage());
        }
    }

    public List<Cita> consultarCitasPendientes() {
        List<Cita> lista = new ArrayList<>();
        try {
            // Traemos el ID de cita y el Nombre del paciente mediante un JOIN
            String sql = "SELECT c.id_cita, p.nombre FROM citas c " +
                    "INNER JOIN pacientes p ON c.id_paciente = p.id " +
                    "WHERE c.resuelta = false";

            PreparedStatement operacion = conectar(sql);
            ResultSet rs = operacion.executeQuery();
            while (rs.next()) {
                Cita c = new Cita();
                c.setIdCita(rs.getInt("id_cita"));
                c.getPaciente().setNombre(rs.getString("nombre"));
                lista.add(c);
            }
            conexion.close();
        } catch (Exception e) {
            System.err.println("Error al consultar citas pendientes: " + e.getMessage());
        }
        return lista;
    }

    public List<data.Medicamento> consultarMedicamentosPorCita(int idCita) {
        List<data.Medicamento> lista = new ArrayList<>();
        try {
            String sql = "SELECT nombre_medicamento, precio, cantidad FROM Recetas WHERE id_cita = ?";
            PreparedStatement operacion = conectar(sql);
            operacion.setInt(1, idCita);
            ResultSet rs = operacion.executeQuery();
            while (rs.next()) {
                data.Medicamento m = new data.Medicamento();
                m.setNombre(rs.getString("nombre_medicamento"));
                m.setPrecio(rs.getDouble("precio"));
                m.setCantidad(rs.getInt("cantidad"));
                lista.add(m);
            }
            conexion.close();
        } catch (Exception e) {
            System.err.println("Error al consultar receta: " + e.getMessage());
        }
        return lista;
    }
}

