package org.chatta.Dao;

import org.chatta.Entities.Actividad;
import org.chatta.Entities.Categoria;
import org.chatta.Entities.Huella;
import org.chatta.Connection.connection;
import org.chatta.Entities.Recomendacion;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HuellaDAO {

    public void save(Huella huella) {
        Transaction transaction = null;
        try (Session session = connection.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(huella);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void update(Huella huella) {
        Transaction transaction = null;
        try (Session session = connection.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(huella);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void delete(Huella huella) {
        Transaction transaction = null;
        try (Session session = connection.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(huella);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public Huella getById(int id) {
        try (Session session = connection.getSessionFactory().openSession()) {
            return session.get(Huella.class, id);
        }
    }

    public List<Huella> getAll() {
        try (Session session = connection.getSessionFactory().openSession()) {
            return session.createQuery("from Huella", Huella.class).list();
        }
    }
    public List<Object[]> getHuellasConNombreActividad(int idUsuario) {
        try (Session session = connection.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT h.id, a.nombre, h.valor, h.unidad, h.fecha " +
                                    "FROM Huella h " +
                                    "JOIN Actividad a ON h.idActividad.id = a.id " +
                                    "WHERE h.usuario.id = :idUsuario", Object[].class)
                    .setParameter("idUsuario", idUsuario)
                    .list();
        }
    }

    public List<Actividad> obtenerTodasLasActividades() {
        try (Session session = connection.getSessionFactory().openSession()) {
            return session.createQuery("FROM Actividad", Actividad.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Recomendacion obtenerTipDeCategoriaMasRepetida(int idUsuario) {
        try (Session session = connection.getSessionFactory().openSession()) {
            // Consulta SQL o HQL para contar las huellas por categoría
            List<Object[]> categoriaCount = session.createQuery(
                            "SELECT h.idActividad.idCategoria, COUNT(h) " +
                                    "FROM Huella h " +
                                    "WHERE h.usuario.id = :idUsuario " +
                                    "GROUP BY h.idActividad.idCategoria " +
                                    "ORDER BY COUNT(h) DESC", Object[].class)
                    .setParameter("idUsuario", idUsuario)
                    .setMaxResults(1) // Solo necesitamos la categoría más repetida
                    .list();

            if (categoriaCount.isEmpty()) {
                return null; // No hay huellas
            }

            // Obtener la categoría más repetida (primer resultado de la consulta)
            Categoria categoria = (Categoria) categoriaCount.get(0)[0]; // Obtenemos el objeto Categoria

            Integer idCategoria = categoria.getId(); // Obtenemos el id de la categoría

            // Obtener la recomendación para esa categoría
            List<Recomendacion> recomendaciones = session.createQuery(
                            "FROM Recomendacion r WHERE r.idCategoria.id = :idCategoria", Recomendacion.class)
                    .setParameter("idCategoria", idCategoria)
                    .list();

            if (!recomendaciones.isEmpty()) {
                return recomendaciones.get(0); // Devolver la primera recomendación
            }

            return null; // Si no hay recomendaciones, devolver null
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




}
