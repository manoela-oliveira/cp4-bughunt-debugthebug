package br.com.fiap.streamfiap.model;

/**
 * Contrato de promoção do StreamFIAP.
 * Toda classe que implementa esta interface deve aplicar
 * 20% de desconto sobre o preço informado.
 */
public interface Promocionavel {

    double aplicarPromocao(double preco);
}
