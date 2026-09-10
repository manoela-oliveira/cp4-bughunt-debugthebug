package br.com.fiap.streamfiap.repository;

import br.com.fiap.streamfiap.model.Conteudo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConteudoRepository extends JpaRepository<Conteudo, Long> {

    List<Conteudo> findByCategoria(String categoria);
}
