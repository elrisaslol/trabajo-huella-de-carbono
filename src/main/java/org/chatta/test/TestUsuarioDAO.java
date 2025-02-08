package org.chatta.test;

import org.chatta.Dao.UsuarioDAO;
import org.chatta.Entities.Usuario;
import java.time.Instant;
import java.util.List;

public class TestUsuarioDAO {
    public static void main(String[] args) {
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        // 1️⃣ CREAR UN USUARIO
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre("Juan Pérez");
        nuevoUsuario.setEmail("juan.perez2@email.com");
        nuevoUsuario.setContraseña("123456");
        nuevoUsuario.setFechaRegistro(Instant.now());

        usuarioDAO.save(nuevoUsuario);
        System.out.println("✅ Usuario creado con ID: " + nuevoUsuario.getId());

        // 2️⃣ OBTENER EL USUARIO POR ID
        Usuario usuarioObtenido = usuarioDAO.getById((long) nuevoUsuario.getId());
        if (usuarioObtenido != null) {
            System.out.println("🔍 Usuario encontrado: " + usuarioObtenido.getNombre() + ", Email: " + usuarioObtenido.getEmail());
        } else {
            System.out.println("❌ Usuario no encontrado.");
        }

        // 3️⃣ ACTUALIZAR EL USUARIO
        usuarioObtenido.setNombre("Juan Carlos Pérez");
        usuarioDAO.update(usuarioObtenido);
        System.out.println("✏️ Usuario actualizado a: " + usuarioObtenido.getNombre());

        // 4️⃣ OBTENER TODOS LOS USUARIOS
        List<Usuario> usuarios = usuarioDAO.getAll();
        System.out.println("📋 Lista de usuarios:");
        for (Usuario u : usuarios) {
            System.out.println("🆔 " + u.getId() + " | Nombre: " + u.getNombre() + " | Email: " + u.getEmail());
        }

        // 5️⃣ ELIMINAR EL USUARIO
        usuarioDAO.delete(usuarioObtenido);
        System.out.println("🗑️ Usuario eliminado con éxito.");
    }
}
