package br.com.fiap.streamfiap.model;

import br.com.fiap.streamfiap.exception.ClassificacaoIndicativaException;
import br.com.fiap.streamfiap.exception.ConteudoIndisponivelException;
import br.com.fiap.streamfiap.exception.CreditosInsuficientesException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Correção: garantindo geração automática
    private Long id;

    private String nome;
    private int idade;
    private double creditos;

    public Usuario() {
    }

    public Usuario(String nome, int idade, double creditos) {
        this.nome = nome; // Correção: adicionando this
        this.idade = idade;
        this.creditos = creditos;
    }

    public boolean temCreditosSuficientes(double preco) {
        return this.creditos >= preco; // Correção: ajustando lógica - credito maior ou igual a preço e não ao contrário
    }

    public void debitarCreditos(double valor) {
        // adiciona o valor aos créditos do usuário
        this.creditos = this.creditos - valor;
    }

    public Usuario alugar(Conteudo c) throws ClassificacaoIndicativaException {
        // Correção: adicionando validação de disponibilidade antes de cobrar o usuário
        if (!c.isDisponivel()) {
            throw new ConteudoIndisponivelException("O conteúdo " + c.getTitulo() + " não está disponível para aluguel.");
        }

        if (this.idade < c.getClassificacaoEtaria()) {
            throw new ClassificacaoIndicativaException("Usuário de " + this.idade
                    + " anos não pode assistir a " + c.getTitulo()
                    + " (classificação " + c.getClassificacaoEtaria() + " anos)");
        }

        double p = c.calcularPrecoAluguel();

        if (!temCreditosSuficientes(p)) {
            throw new CreditosInsuficientesException("Créditos insuficientes para alugar " + c.getTitulo());
        }

        debitarCreditos(p);
        c.setDisponivel(false);

        // Correção: removendo bloco "RECIBO STREAMFIAP" para otimização -> se não vai ser visualizado pelo cliente, não há necessidade de existir

        return this;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }

    public double getCreditos() { return creditos; }
    public void setCreditos(double creditos) { this.creditos = creditos; }
}
