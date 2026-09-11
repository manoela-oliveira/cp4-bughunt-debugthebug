package br.com.fiap.streamfiap.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConteudoNaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> handleConteudoNaoEncontrado(ConteudoNaoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
    }

    @ExceptionHandler(CreditosInsuficientesException.class)
    public ResponseEntity<Map<String, String>> handleCreditosInsuficientes(CreditosInsuficientesException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Map.of("erro", e.getMessage()));
    }

    // Correção: adicionando mensagem de erro para tratativas referentes a faixa etária
    @ExceptionHandler(ClassificacaoIndicativaException.class)
    public ResponseEntity<Map<String, String>> handleClassificacaoIndicativa(ClassificacaoIndicativaException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("erro", e.getMessage()));
    }

    @ExceptionHandler(ConteudoIndisponivelException.class)
    public ResponseEntity<Map<String, String>> handleConteudoIndisponivel(ConteudoIndisponivelException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("erro", e.getMessage()));
    }
}
