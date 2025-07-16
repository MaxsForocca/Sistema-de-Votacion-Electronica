/* Interfaz de comunicacion directa con la DB para Usuarios */

package com.sistema.auth.repository;
import com.sistema.auth.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    /**
     * Busca un usuario por su nombre de usuario y contraseña.
     *
     * @param username Nombre de usuario.
     * @param password Contraseña del usuario.
     * @return Un Optional que contiene el Usuario si se encuentra, o vacío si no.
     */
    Optional<Usuario> findById(Long id);

    Optional<Usuario> findByIdAndUsername(Long id, String username);

    Optional<Usuario> findByUsernameAndPassword(String username, String password);

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username Nombre de usuario.
     * @return Un Usuario si se encuentra, o null si no.
     */
    Optional<Usuario> findByUsername(String username);

    boolean existsByUsername(String username);
    boolean existsById(Long id);
}
