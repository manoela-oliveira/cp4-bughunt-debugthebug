package br.com.fiap.streamfiap.repository;

import br.com.fiap.streamfiap.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
