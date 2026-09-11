package br.com.fiap.streamfiap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.streamfiap.exception.ConteudoNaoEncontradoException;
import br.com.fiap.streamfiap.model.Conteudo;
import br.com.fiap.streamfiap.model.Documentario;
import br.com.fiap.streamfiap.model.Filme;
import br.com.fiap.streamfiap.model.Serie;
import br.com.fiap.streamfiap.repository.ConteudoRepository;

@RestController
@RequestMapping("/api/conteudos")
public class ConteudoController {

    @Autowired
    private ConteudoRepository conteudoRepository;

    // GET /api/conteudos - Listar todos
    @GetMapping
    public List<Conteudo> listarTodos() {
        return conteudoRepository.findAll();
    }

    // GET /api/conteudos/{id} - Buscar por ID
    @GetMapping("/{id}")
    // Correção: removendo try-catch que engolia a excecao e ajustando retorno conforme padrão Spring
    public ResponseEntity<Conteudo> buscarPorId(@PathVariable Long id) {
        Conteudo conteudo = conteudoRepository.findById(id)
                .orElseThrow(() -> new ConteudoNaoEncontradoException("Conteúdo não encontrado: " + id));
        return ResponseEntity.ok(conteudo);
    }

    // GET /api/conteudos/categoria/{categoria} - Buscar por categoria
    @GetMapping("/categoria/{categoria}")
    public List<Conteudo> listarPorCategoria(@PathVariable String categoria) {
        return conteudoRepository.findByCategoria(categoria); // Correção: utilizando método já existente para otimização
    }

    // GET /api/conteudos/{id}/preco-promocional - Preço com promoção
    @GetMapping("/{id}/preco-promocional")
    public double precoPromocional(@PathVariable Long id) {
        Conteudo conteudo = conteudoRepository.findById(id)
                .orElseThrow(() -> new ConteudoNaoEncontradoException("Conteúdo não encontrado: " + id));
        return conteudo.calcularPrecoPromocional();
    }

    // Correção: utilizando getter para respeitar e possibilitar o uso correto de encapsulamento -> getDuracaoMinutos()

    // POST /api/conteudos/filme - cadastra um filme (cria nova instância sem o id vindo do cliente)
    @PostMapping("/filme")
    public ResponseEntity<Filme> cadastrarFilme(@RequestBody Filme filme) {
        Filme novo = new Filme(filme.getTitulo(), filme.getCategoria(), filme.getDuracaoMinutos(),
            filme.getClassificacaoEtaria(), filme.isDisponivel(), filme.isEstreia());
        return ResponseEntity.status(201).body(conteudoRepository.save(novo));
    }

    // POST /api/conteudos/serie - cadastra uma série
    @PostMapping("/serie")
    public ResponseEntity<Serie> cadastrarSerie(@RequestBody Serie serie) {
        Serie nova = new Serie(serie.getTitulo(), serie.getCategoria(), serie.getDuracaoMinutos(),
                serie.getClassificacaoEtaria(), serie.getNumeroTemporadas());
        return ResponseEntity.status(201).body(conteudoRepository.save(nova));
    }

    // POST /api/conteudos/documentario - cadastra um documentário
    @PostMapping("/documentario")
    public ResponseEntity<Documentario> cadastrarDocumentario(@RequestBody Documentario documentario) {
        Documentario novo = new Documentario(documentario.getTitulo(), documentario.getCategoria(),
                documentario.getDuracaoMinutos(), documentario.getClassificacaoEtaria(),
                documentario.isDisponivel(), documentario.getTema());
        return ResponseEntity.status(201).body(conteudoRepository.save(novo));
    }

    // Correção: removendo comentários desnecessários e métodos obsoletos
}
