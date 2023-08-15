package com.teste.primeiroexemplo.repository;

import java.util.ArrayList;
//import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;

//import org.springframework.stereotype.Repository;

import com.teste.primeiroexemplo.model.Produto;
import com.teste.primeiroexemplo.model.exception.ResourceNotFoundExcepetion;

//@Repository
public class ProdutoRepositoryOld {

  private ArrayList<Produto> produtos = new ArrayList<Produto>();
  private Integer ultimoId = 0;

  /**
   * retornar uma lista de produtos
   *
   * @return lista de produtos
   */
  public List<Produto> obterTodos() {
    return produtos;
  }

  /**
   * metodo que retorna produto com base com id caso seja encontrado
   *
   * @param id
   * @return produto com id correspondente
   */
  public Optional<Produto> obteProdutoPorId(Integer id) {

    return produtos
        .stream()
        .filter(produto -> produto.getId() == id)
        .findFirst();
  }

  /**
   * método para adicionar um novo produto na lista recebendo o id
   *
   * @param produto que será adicionado
   * @return produto que foi adicionado
   */
  public Produto adicionar(Produto produto) {
    ultimoId = ultimoId + 1;

    produto.setId(ultimoId);
    produtos.add(produto);

    return produto;
  }

  /**
   * método para deletar produto pelo id recebido
   *
   * @param Id do produto
   */
  public String deletarProduto(Integer id) {
    if (produtos.stream().filter(produto -> produto.getId() == id).findFirst().isEmpty()) {
      throw new ResourceNotFoundExcepetion("Produto não foi encontrado portanto não pode ser deletado.");
    } else {
      produtos.removeIf(produto -> produto.getId() == id);
      return ("Produto com id: " + id + " foi deletado com sucesso");
    }
  }

  /**
   * método que irá encontrar um optional de produto de acordo com id, após isso
   * irá deletar o produto da lista e após atualiza inserindo na lista, todos os
   * processo usam o mesmo id por ser estático e único
   *
   * @param produto com dados atualizados
   * @return produto com dados atualizados
   */
  public Produto atualizarProduto(Produto produto) {
    // encontrar o produto
    Optional<Produto> produtoEncontrado = obteProdutoPorId(produto.getId());
    // remover o produto antigo, id será mantido
    if (produtoEncontrado.isEmpty()) {
      throw new ResourceNotFoundExcepetion("Produto não foi encontrado portanto não pode ser atualizado.");
    }
    deletarProduto(produto.getId());
    // inserir a atualização na lista
    produtos.add(produto);

    return produto;
  }
}
