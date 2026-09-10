package br.com.fiap.streamfiap.exception;

public class ConteudoNaoEncontradoException extends RuntimeException {

    public ConteudoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
