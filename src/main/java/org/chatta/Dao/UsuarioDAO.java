package org.chatta.Dao;

import org.chatta.Entities.Usuario;
import org.chatta.Connection.connection;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class UsuarioDAO {

    public void save(Usuario usuario) {
        Transaction transaction = null;
        try (Session session = connection.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(usuario);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void update(Usuario usuario) {
        Transaction transaction = null;
        try (Session session = connection.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(usuario);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void delete(Usuario usuario) {
        Transaction transaction = null;
        try (Session session = connection.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(usuario);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public Usuario getById(Long id) {
        try (Session session = connection.getSessionFactory().openSession()) {
            return session.get(Usuario.class, id);
        }
    }

    public Usuario getByNombre(String nombre) {
        try (Session session = connection.getSessionFactory().openSession()) {
            return session.createQuery("FROM Usuario WHERE nombre = :nombre", Usuario.class)
                    .setParameter("nombre", nombre)
                    .uniqueResult();
        }
    }


    public List<Usuario> getAll() {
        try (Session session = connection.getSessionFactory().openSession()) {
            return session.createQuery("from Usuario", Usuario.class).list();
        }
    }
}
