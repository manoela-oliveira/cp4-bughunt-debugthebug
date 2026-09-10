package br.com.fiap.streamfiap.model;

import jakarta.persistence.Entity;

@Entity
public class Filme extends Conteudo implements Promocionavel {

    private boolean estreia;

    public Filme() {
    }

    public Filme(String titulo, String categoria, int duracaoMinutos, int classificacaoEtaria, boolean disponivel, boolean estreia) {
        super(titulo, categoria, duracaoMinutos, classificacaoEtaria, disponivel);
        this.estreia = estreia;
    }

    @Override
    public double calcularPrecoAluguel() {
        return 9.90 + (estreia ? 5.00 : 0.0);
    }

    @Override
    public double aplicarPromocao(double preco) {
        return preco * 1.2;
    }

    public boolean isEstreia() { return estreia; }
    public void setEstreia(boolean estreia) { this.estreia = estreia; }
}
