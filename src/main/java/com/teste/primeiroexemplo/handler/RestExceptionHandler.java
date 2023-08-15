package com.teste.primeiroexemplo.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.teste.primeiroexemplo.model.error.ErrorMessage;
import com.teste.primeiroexemplo.model.exception.ResourceNotFoundExcepetion;

//controlador rest dentro da aplicação que escuta todas as exceptions e errors, recebe excption da classe criada
@ControllerAdvice
public class RestExceptionHandler {
  // classe HttpsStatus pode retornar os valores dos erros
  // escuta toda notFound action
  @ExceptionHandler(ResourceNotFoundExcepetion.class)
  public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundExcepetion ex) {
    ErrorMessage error = new ErrorMessage("Not Found", HttpStatus.NOT_FOUND.value(), ex.getMessage());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }
}
