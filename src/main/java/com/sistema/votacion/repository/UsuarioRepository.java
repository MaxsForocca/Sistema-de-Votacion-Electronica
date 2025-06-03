/* Interfaz de comunicacion directa con la DB para Usuarios */

package com.sistema.votacion.repository;
import com.sistema.votacion.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Aquí puedes definir métodos personalizados si es necesario
    // Por ejemplo, para buscar usuarios por nombre de usuario o correo electrónico
    Optional<Usuario> findByUsernameAndPassword(String username, String password);
}
