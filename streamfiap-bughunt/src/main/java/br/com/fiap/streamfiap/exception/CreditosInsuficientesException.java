package br.com.fiap.streamfiap.exception;

public class CreditosInsuficientesException extends RuntimeException {

    public CreditosInsuficientesException(String mensagem) {
        super(mensagem);
    }
}
