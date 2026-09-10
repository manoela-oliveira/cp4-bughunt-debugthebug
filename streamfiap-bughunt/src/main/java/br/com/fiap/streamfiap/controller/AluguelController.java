package br.com.fiap.streamfiap.controller;

import br.com.fiap.streamfiap.exception.ClassificacaoIndicativaException;
import br.com.fiap.streamfiap.exception.ConteudoNaoEncontradoException;
import br.com.fiap.streamfiap.model.Conteudo;
import br.com.fiap.streamfiap.model.Usuario;
import br.com.fiap.streamfiap.repository.ConteudoRepository;
import br.com.fiap.streamfiap.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alugueis")
public class AluguelController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ConteudoRepository conteudoRepository;

    // POST /api/alugueis?usuarioId=1&conteudoId=2 - Alugar um conteúdo
    @PostMapping
    public ResponseEntity<Usuario> alugar(@RequestParam Long usuarioId, @RequestParam Long conteudoId)
            throws ClassificacaoIndicativaException {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + usuarioId));
        Conteudo conteudo = conteudoRepository.findById(conteudoId)
                .orElseThrow(() -> new ConteudoNaoEncontradoException("Conteúdo não encontrado: " + conteudoId));

        Usuario usuarioAtualizado = usuario.alugar(conteudo);

        conteudoRepository.save(conteudo);
        return ResponseEntity.ok(usuarioRepository.save(usuarioAtualizado));
    }
}
