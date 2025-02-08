package org.chatta.Dao;

import org.chatta.Entities.Actividad;
import org.chatta.Entities.Habito;
import org.chatta.Connection.connection;
import org.chatta.Entities.HabitoId;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.Instant;
import java.util.List;

public class HabitoDAO {

    public void save(Habito habito) {
        Transaction transaction = null;
        try (Session session = connection.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(habito);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void update(Habito habito) {
        Transaction transaction = null;
        try (Session session = connection.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(habito);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void updateHabitoConNuevaActividad(Integer idUsuario, Integer idActividadAntigua, Integer idActividadNueva, Integer frecuencia, String tipo, Instant ultimaFecha) {
        Transaction transaction = null;
        try (Session session = connection.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Buscar el hábito existente
            HabitoId habitoIdAntiguo = new HabitoId(idUsuario, idActividadAntigua);
            Habito habitoExistente = session.get(Habito.class, habitoIdAntiguo);

            if (habitoExistente != null) {
                // Eliminar el hábito anterior
                session.delete(habitoExistente);
                session.flush(); // Asegurar que se elimina antes de insertar el nuevo
            }

            // Crear el nuevo hábito con la nueva actividad
            HabitoId habitoIdNuevo = new HabitoId(idUsuario, idActividadNueva);
            Actividad nuevaActividad = session.get(Actividad.class, idActividadNueva); // Obtener la nueva actividad
            Habito nuevoHabito = new Habito();
            nuevoHabito.setId(habitoIdNuevo);
            nuevoHabito.setIdActividad(nuevaActividad);
            nuevoHabito.setFrecuencia(frecuencia);
            nuevoHabito.setTipo(tipo);
            nuevoHabito.setUltimaFecha(ultimaFecha);

            // Guardar el nuevo hábito
            session.save(nuevoHabito);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }


    public void delete(Habito habito) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = connection.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.delete(habito);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback(); // ✅ Solo hace rollback si la transacción está activa
            }
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close(); // ✅ Asegura que la sesión se cierre correctamente
            }
        }
    }


    public List<Object[]> getHabitosConNombreActividad(int idUsuario) {
        try (Session session = connection.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT h.id.idActividad, a.nombre, h.frecuencia, h.tipo, h.ultimaFecha " +
                                    "FROM Habito h " +
                                    "JOIN Actividad a ON h.id.idActividad = a.id " +
                                    "WHERE h.id.idUsuario = :idUsuario", Object[].class)
                    .setParameter("idUsuario", idUsuario)
                    .list();
        }
    }

    public Habito getById(Integer idUsuario, Integer idActividad) {
        Habito habito = null;
        try (Session session = connection.getSessionFactory().openSession()) {
            habito = session.get(Habito.class, new HabitoId(idUsuario, idActividad));
        }
        if (habito == null) {
            throw new IllegalArgumentException("No se encontró el hábito con usuario: " + idUsuario + " y actividad: " + idActividad);
        }
        return habito;
    }

    public List<Actividad> obtenerTodasLasActividades() {
        try (Session session = connection.getSessionFactory().openSession()) {
            return session.createQuery("FROM Actividad", Actividad.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
