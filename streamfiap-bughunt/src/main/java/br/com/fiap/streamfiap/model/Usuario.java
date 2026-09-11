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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private int idade;
    private double creditos;

    public Usuario() {
    }

    public Usuario(String nome, int idade, double creditos) {
        this.nome = nome;
        this.idade = idade;
        this.creditos = creditos;
    }

    public boolean temCreditosSuficientes(double preco) {
        return this.creditos >= preco;
    }

    public void debitarCreditos(double valor) {
        this.creditos = this.creditos - valor;
    }

    public Usuario alugar(Conteudo conteudo) throws ClassificacaoIndicativaException {
        if (!conteudo.isDisponivel()) {
            throw new ConteudoIndisponivelException("O conteúdo " + conteudo.getTitulo() + " não está disponível para aluguel.");
        }

        if (this.idade < conteudo.getClassificacaoEtaria()) {
            throw new ClassificacaoIndicativaException("Usuário de " + this.idade
                    + " anos não pode assistir a " + conteudo.getTitulo()
                    + " (classificação " + conteudo.getClassificacaoEtaria() + " anos)");
        }

        double precoAluguel = conteudo.calcularPrecoAluguel();

        if (!temCreditosSuficientes(precoAluguel)) {
            throw new CreditosInsuficientesException("Créditos insuficientes para alugar " + conteudo.getTitulo());
        }

        debitarCreditos(precoAluguel);
        conteudo.setDisponivel(false);

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
