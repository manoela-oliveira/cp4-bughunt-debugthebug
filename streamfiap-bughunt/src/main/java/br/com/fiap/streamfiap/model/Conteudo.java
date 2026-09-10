package br.com.fiap.streamfiap.model;

import jakarta.persistence.*;

@Entity
@Table(name = "conteudos")
public abstract class Conteudo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String categoria;

    public int duracaoMinutos;

    private int classificacaoEtaria;
    private boolean disponivel;

    protected Conteudo() {
    }

    protected Conteudo(String titulo, String categoria, int duracaoMinutos, int classificacaoEtaria, boolean disponivel) {
        this.titulo = titulo;
        this.categoria = categoria;
        this.duracaoMinutos = duracaoMinutos;
        this.classificacaoEtaria = classificacaoEtaria;
        this.disponivel = disponivel;
    }

    public double calcularPrecoAluguel() {
        return 9.90;
    }

    public double calcularPrecoPromocional() {
        if (this instanceof Promocionavel) {
            Promocionavel promocionavel = (Promocionavel) this;
            return promocionavel.aplicarPromocao(calcularPrecoAluguel());
        }
        return calcularPrecoAluguel();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getDuracaoMinutos() { return duracaoMinutos; }
    public void setDuracaoMinutos(int duracaoMinutos) { this.duracaoMinutos = duracaoMinutos; }

    public int getClassificacaoEtaria() { return classificacaoEtaria; }
    public void setClassificacaoEtaria(int classificacaoEtaria) { this.classificacaoEtaria = classificacaoEtaria; }

    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }
}
