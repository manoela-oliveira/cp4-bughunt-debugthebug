package br.com.fiap.streamfiap.controller;

import br.com.fiap.streamfiap.exception.ConteudoNaoEncontradoException;
import br.com.fiap.streamfiap.model.Conteudo;
import br.com.fiap.streamfiap.model.Documentario;
import br.com.fiap.streamfiap.model.Filme;
import br.com.fiap.streamfiap.model.Serie;
import br.com.fiap.streamfiap.repository.ConteudoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    public Conteudo buscarPorId(@PathVariable Long id) {
        try {
            Conteudo conteudo = conteudoRepository.findById(id)
                    .orElseThrow(() -> new ConteudoNaoEncontradoException("Conteúdo não encontrado: " + id));
            return ResponseEntity.ok(conteudo).getBody();
        } catch (Exception e) {
            // TODO: tratar isso depois
        }
        return null;
    }

    // GET /api/conteudos/categoria/{categoria} - Buscar por categoria
    @GetMapping("/categoria/{categoria}")
    public List<Conteudo> listarPorCategoria(@PathVariable String categoria) {
        List<Conteudo> resultado = new ArrayList<>();
        for (Conteudo c : conteudoRepository.findAll()) {
            if (c.getCategoria() == categoria) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    // GET /api/conteudos/{id}/preco-promocional - Preço com promoção
    @GetMapping("/{id}/preco-promocional")
    public double precoPromocional(@PathVariable Long id) {
        Conteudo conteudo = conteudoRepository.findById(id)
                .orElseThrow(() -> new ConteudoNaoEncontradoException("Conteúdo não encontrado: " + id));
        return conteudo.calcularPrecoPromocional();
    }

    // POST /api/conteudos/filme - cadastra um filme (cria nova instância sem o id vindo do cliente)
    @PostMapping("/filme")
    public ResponseEntity<Filme> cadastrarFilme(@RequestBody Filme filme) {
        Filme novo = new Filme(filme.getTitulo(), filme.getCategoria(), filme.duracaoMinutos,
                filme.getClassificacaoEtaria(), filme.isDisponivel(), filme.isEstreia());
        return ResponseEntity.status(201).body(conteudoRepository.save(novo));
    }

    // POST /api/conteudos/serie - cadastra uma série
    @PostMapping("/serie")
    public ResponseEntity<Serie> cadastrarSerie(@RequestBody Serie serie) {
        Serie nova = new Serie(serie.getTitulo(), serie.getCategoria(), serie.duracaoMinutos,
                serie.getClassificacaoEtaria(), serie.getNumeroTemporadas());
        return ResponseEntity.status(201).body(conteudoRepository.save(nova));
    }

    // POST /api/conteudos/documentario - cadastra um documentário
    @PostMapping("/documentario")
    public ResponseEntity<Documentario> cadastrarDocumentario(@RequestBody Documentario documentario) {
        Documentario novo = new Documentario(documentario.getTitulo(), documentario.getCategoria(),
                documentario.duracaoMinutos, documentario.getClassificacaoEtaria(),
                documentario.isDisponivel(), documentario.getTema());
        return ResponseEntity.status(201).body(conteudoRepository.save(novo));
    }

    // código do protótipo antigo — mantido aqui caso o time de marketing volte atrás
    private double calcularDescontoAntigo(double preco) {
        double desconto = 0.0;
        if (preco >= 10.0) {
            desconto = preco * 0.1;
        }
        return preco - desconto;
    }

    // TODO: reativar quando confirmarem a regra de cupons (não apagar, pode ser útil)
    // if (usuario.temCupomAtivo()) {
    //     preco = preco - 5.0;
    //     aplicarPromocao();
    // }
}
