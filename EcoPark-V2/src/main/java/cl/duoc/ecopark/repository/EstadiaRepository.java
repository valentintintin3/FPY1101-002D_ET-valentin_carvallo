package cl.duoc.ecopark.repository;

import cl.duoc.ecopark.dao.EstadiaDAO;
import cl.duoc.ecopark.model.Estadia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * REPOSITORY: mantiene las estadías en memoria y delega al DAO.
 * La interfaz EstadiaDAO permite cambiar JSON por MySQL sin cambiar los modelos.
 */
public class EstadiaRepository {

    private final EstadiaDAO dao; // Dependencia recibida desde el Controller.
    private final List<Estadia> estadias = new ArrayList<>();

    public EstadiaRepository(EstadiaDAO dao) {
        this.dao = dao;
    }

    /** Carga los registros desde MySQL, reemplazando la lista SOLO si no hubo error. */
    public void cargar() throws IOException {
        List<Estadia> recuperadas = dao.cargar();
        estadias.clear();
        estadias.addAll(recuperadas);
    }

    /** Devuelve una copia: el Controller no puede alterar la lista interna. */
    public List<Estadia> listar() {
        return List.copyOf(estadias);
    }

    /**
     * REGISTRAR: valida patente duplicada y solicita INSERT al DAO.
     * Primero confirmamos MySQL y DESPUÉS cambiamos la lista en memoria.
     */
    public void registrar(Estadia estadia) throws IOException {
        if (estadia == null) {
            throw new IllegalArgumentException("Debe indicar una estadía válida.");
        }
        for (Estadia existente : estadias) {
            if (existente.getPatente().equalsIgnoreCase(estadia.getPatente())) {
                throw new IllegalArgumentException("La patente " + estadia.getPatente()
                        + " ya está registrada.");
            }
        }
        dao.registrar(estadia); // INSERT: puede fallar por conexión o patente repetida en BD.
        estadias.add(estadia); // Se añade solo si MySQL confirmó el registro.
    }

    /**
     * ELIMINAR: solicita DELETE y luego actualiza la lista.
     * Si MySQL falla, la tabla visual no perderá el registro.
     */
    public void eliminar(Estadia estadia) throws IOException {
        if (estadia == null) {
            throw new IllegalArgumentException("Seleccione una estadía para eliminar.");
        }
        if (!estadias.contains(estadia)) {
            throw new IllegalArgumentException("La estadía ya no existe en la lista.");
        }
        dao.eliminar(estadia.getPatente()); // DELETE de una fila en MySQL.
        estadias.remove(estadia); // Se quita después del DELETE exitoso.
    }
}
