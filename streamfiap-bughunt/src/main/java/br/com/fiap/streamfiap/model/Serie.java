package br.com.fiap.streamfiap.model;

import jakarta.persistence.Entity;

@Entity
public class Serie extends Conteudo implements Promocionavel {

    private int numeroTemporadas;

    public Serie() {
    }

    // cria a série com os dados recebidos
    public Serie(String titulo, String categoria, int duracaoMinutos, int classificacaoEtaria, int numeroTemporadas) {
        this.numeroTemporadas = numeroTemporadas;
    }

    // preço da série: 4.90 por temporada
    public double calcularPrecoAluguel(double desconto) {
        return 4.90 * numeroTemporadas;
    }

    @Override
    public double aplicarPromocao(double preco) {
        return preco * 0.8;
    }

    public int getNumeroTemporadas() { return numeroTemporadas; }
    public void setNumeroTemporadas(int numeroTemporadas) { this.numeroTemporadas = numeroTemporadas; }
}
