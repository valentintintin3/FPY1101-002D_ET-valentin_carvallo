package cl.duoc.ecopark.dao;

import cl.duoc.ecopark.model.Estadia;
import cl.duoc.ecopark.model.EstadiaAuto;
import cl.duoc.ecopark.model.EstadiaMoto;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * DAO MYSQL: usa JDBC para leer y escribir en la tabla estadias.
 * Controller y Repository no necesitan conocer instrucciones SQL.
 */
public class MySqlEstadiaDAO implements EstadiaDAO {

    // La configuración queda separada del código fuente Java.
    private static final Path CONFIG = Path.of("config", "db.properties");

    /**
     * CONEXIÓN: abre una conexión nueva para cada operación.
     * try-with-resources cerrará Connection, PreparedStatement y ResultSet.
     */
    private Connection conectar() throws IOException, SQLException {
        Properties propiedades = new Properties();

        // Lee URL, usuario y contraseña desde config/db.properties.
        try (InputStream entrada = Files.newInputStream(CONFIG)) {
            propiedades.load(entrada);
        } catch (IOException e) {
            throw new IOException("No se pudo leer " + CONFIG
                    + ". Revisa la carpeta de trabajo del proyecto.", e);
        }

        String url = propiedades.getProperty("db.url", "").trim();
        String usuario = propiedades.getProperty("db.user", "").trim();
        String clave = propiedades.getProperty("db.password", "");

        if (!url.startsWith("jdbc:mysql://") || url.contains("NOMBRE_DE_TU_BD")) {
            throw new IOException("Configura db.url con el nombre real de la BD en " + CONFIG);
        }
        if (usuario.isBlank()) {
            throw new IOException("Configura db.user en " + CONFIG);
        }
        return DriverManager.getConnection(url, usuario, clave);
    }

    /**
     * SELECT: lee las filas y crea objetos Java del tipo correcto.
     * No se almacena el costo, porque calcularCosto() ya lo resuelve.
     */
    @Override
    public List<Estadia> cargar() throws IOException {
        List<Estadia> lista = new ArrayList<>();
        String sql = "SELECT patente, tipo, horas, convenio FROM estadias ORDER BY patente";

        try (Connection conexion = conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet filas = sentencia.executeQuery()) {

            while (filas.next()) { // rs.next(): avanza a la siguiente fila.
                String patente = filas.getString("patente");
                String tipo = filas.getString("tipo");
                int horas = filas.getInt("horas");
                boolean convenio = filas.getBoolean("convenio");

                // Convertimos cada fila a EstadiaAuto o EstadiaMoto.
                try {
                    if ("Auto".equals(tipo)) {
                        lista.add(new EstadiaAuto(patente, horas));
                    } else if ("Moto".equals(tipo)) {
                        lista.add(new EstadiaMoto(patente, horas, convenio));
                    } else {
                        throw new IOException("Tipo desconocido en la base: " + tipo);
                    }
                } catch (IllegalArgumentException e) {
                    throw new IOException("Datos inválidos en MySQL para " + patente, e);
                }
            }
        } catch (SQLException e) {
            throw new IOException("No fue posible consultar la tabla estadias en MySQL: "
                    + e.getMessage(), e);
        }
        return lista;
    }

    /**
     * INSERT: PreparedStatement evita concatenar datos del usuario en SQL.
     * Los signos ? representan los parámetros que asignamos con setString, etc.
     */
    @Override
    public void registrar(Estadia estadia) throws IOException {
        if (estadia == null) {
            throw new IllegalArgumentException("La estadía no puede ser nula.");
        }

        String sql = "INSERT INTO estadias (patente, tipo, horas, convenio) VALUES (?, ?, ?, ?)";

        try (Connection conexion = conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, estadia.getPatente());
            sentencia.setString(2, estadia.getTipo());
            sentencia.setInt(3, estadia.getHoras());

            // Convenio solo tiene sentido en Moto; para Auto se guarda false.
            boolean convenio = estadia instanceof EstadiaMoto moto && moto.isConvenio();
            sentencia.setBoolean(4, convenio);

            int filasAfectadas = sentencia.executeUpdate();
            if (filasAfectadas != 1) {
                throw new IOException("MySQL no confirmó el registro de la estadía.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new IOException("La patente " + estadia.getPatente()
                    + " ya existe o no cumple las restricciones de MySQL.", e);
        } catch (SQLException e) {
            throw new IOException("No se pudo registrar en MySQL: " + e.getMessage(), e);
        }
    }

    /**
     * DELETE: utiliza la patente como clave primaria.
     * executeUpdate() devuelve la cantidad de filas eliminadas.
     */
    @Override
    public void eliminar(String patente) throws IOException {
        String sql = "DELETE FROM estadias WHERE patente = ?";

        try (Connection conexion = conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, patente);
            int eliminadas = sentencia.executeUpdate();
            if (eliminadas == 0) {
                throw new IOException("La patente ya no se encuentra registrada en MySQL.");
            }
        } catch (SQLException e) {
            throw new IOException("No se pudo eliminar en MySQL: " + e.getMessage(), e);
        }
    }
}
