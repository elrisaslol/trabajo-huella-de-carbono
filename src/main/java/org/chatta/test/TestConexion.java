package org.chatta.test;

import org.chatta.Connection.connection;
import org.hibernate.Session;

public class TestConexion {
    public static void main(String[] args) {
        System.out.println("Intentando abrir sesión con Hibernate...");
        try (Session session = connection.getSessionFactory().openSession()) {
            System.out.println("✅ Conexión a la base de datos establecida correctamente.");
        } catch (Exception e) {
            System.err.println("❌ Error al conectar con la base de datos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            connection.shutdown();
        }
    }
}
