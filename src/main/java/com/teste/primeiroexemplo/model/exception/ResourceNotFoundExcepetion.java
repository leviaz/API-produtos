package com.teste.primeiroexemplo.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND) // CODIGO PARA NÃO ENCONTRADO
public class ResourceNotFoundExcepetion extends RuntimeException {

  public ResourceNotFoundExcepetion(String mensagem) {
    super(mensagem); // criando uma exceção personalizada que irá extender da classe do java
                     // RunTimeException
  }

}
