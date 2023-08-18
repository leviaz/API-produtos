package com.teste.primeiroexemplo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.teste.primeiroexemplo.model.Produto;
import com.teste.primeiroexemplo.model.exception.ResourceNotFoundExcepetion;
import com.teste.primeiroexemplo.repository.ProdutoRepository;
import com.teste.primeiroexemplo.shared.ProdutoDTO;

@Service
public class ProdutoService {
  // autowired inverte controle
  @Autowired
  private ProdutoRepository produtoRepository;

  /**
   * método para obter todos os produtos diretamente do repositório
   *
   * @return lista de produtos do repostitório
   */
  public List<ProdutoDTO> obterTodos() {

    // retorna a lista conectada ao banco
    List<Produto> produtos = produtoRepository.findAll();

    // realiza a conversão para uma lista de DTOs usando o modelmapper
    return produtos.stream()
        .map(produto -> new ModelMapper().map(produto, ProdutoDTO.class))
        .collect(Collectors.toList());
    // return produtos.stream()
    // .map(produto -> new ModelMapper().map(produto,ProdutoDTO.class))
    // .collect( Collectors.toList());
  }

  /**
   * metodo que retorna produto com base com id caso seja encontrado
   *
   * @param id
   * @return produto com id correspondente
   */
  public Optional<ProdutoDTO> obteProdutoPorId(Integer id) {
    // otendo produto pelo id
    Optional<Produto> produto = produtoRepository.findById(id);

    // testagem de id para que o service valide o método
    if (produto.isEmpty()) {
      throw new ResourceNotFoundExcepetion("Produto com id:" + id + "não encontrado");
    }
    // conversão o optional para optional dto
    ProdutoDTO dto = new ModelMapper().map(produto.get(), ProdutoDTO.class);
    return Optional.of(dto);

  }

  /**
   * método para adicionar um novo produto na lista recebendo o id
   *
   * @param produto que será adicionado
   * @return produto que foi adicionado
   */
  public ProdutoDTO adicionar(ProdutoDTO produtoDto) {

    // remover id para cadastro
    produtoDto.setId(null);

    // criar um objeto de mapeamento
    ModelMapper mapper = new ModelMapper();

    // converter o produto dto em produto
    Produto produto = mapper.map(produtoDto, Produto.class);
    // salvar no banco
    produto = produtoRepository.save(produto); // retorna produto com id para o produto

    produtoDto.setId(produto.getId());
    // retornar o dto atualizado
    return produtoDto; // o dto enviado contem tudo exceto o id logo basta atualizar o id
  }

  /**
   * método para deletar produto pelo id recebido
   *
   * @param Id do produto
   */
  public void deletarProduto(Integer id) {
    // verificar se o id existe retorna optional pois não se sabe o retorno
    Optional<Produto> produto = produtoRepository.findById(id);
    if (produto.isEmpty()) {
      throw new ResourceNotFoundExcepetion("Produto com id: " + id + " não encontrado, logo não foi possível deletar");
    }
    // deletar diretamente
    produtoRepository.deleteById(id);

  }

  /**
   * método que irá encontrar um optional de produto de acordo com id, após isso
   * irá deletar o produto da lista e após atualiza inserindo na lista, todos os
   * processo usam o mesmo id por ser estático e único
   *
   * @param produto com dados atualizados
   * @return produto com dados atualizados
   */
  public ProdutoDTO atualizarProduto(Integer id, ProdutoDTO produtoDto) {
    // passar o id para o produto dto
    produtoDto.setId(id);
    // criar o mapper
    ModelMapper mapper = new ModelMapper();
    // converter o DTO em um produto atualizado
    Produto produto = mapper.map(produtoDto, Produto.class);
    // atualizar o produto convertido no banco
    produtoRepository.save(produto);

    return produtoDto;
  }

  public List<ProdutoDTO> OrdenarPreco() {
    List<Produto> order = produtoRepository.findAll(Sort.by(Sort.Direction.ASC, "valor"));
    return order.stream()
        .map(produto -> new ModelMapper().map(produto, ProdutoDTO.class))
        .collect(Collectors.toList());
  }

  public ProdutoDTO atualizarValor(Integer id, Double valor) {
    Optional<Produto> produto = produtoRepository.findById(id);
    if (produto.isEmpty()) {
      throw new ResourceNotFoundExcepetion("Produto com id: " + id + " não encontrado, logo não foi possível deletar");
    }

    Produto produtoOpen = produto.get();
    produtoOpen.setValor(valor);
    ModelMapper mapper = new ModelMapper();
    ProdutoDTO produtoOpenDto = mapper.map(produtoOpen, ProdutoDTO.class);
    atualizarProduto(id, produtoOpenDto);
    return produtoOpenDto;

  }

}
