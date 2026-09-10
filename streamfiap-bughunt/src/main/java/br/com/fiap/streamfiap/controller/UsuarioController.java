package br.com.fiap.streamfiap.controller;

import br.com.fiap.streamfiap.model.Usuario;
import br.com.fiap.streamfiap.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // POST /api/usuarios - Cadastrar usuário (cria nova instância sem o id vindo do cliente)
    @PostMapping
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) {
        Usuario novo = new Usuario(usuario.getNome(), usuario.getIdade(), usuario.getCreditos());
        return ResponseEntity.status(201).body(usuarioRepository.save(novo));
    }

    // GET /api/usuarios/{id} - Buscar usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + id));
        return ResponseEntity.ok(usuario);
    }
}
