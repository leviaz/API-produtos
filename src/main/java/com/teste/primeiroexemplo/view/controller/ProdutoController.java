package com.teste.primeiroexemplo.view.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.teste.primeiroexemplo.model.Produto;
import com.teste.primeiroexemplo.service.ProdutoService;
import com.teste.primeiroexemplo.shared.ProdutoDTO;
import com.teste.primeiroexemplo.view.model.ProdutoRequest;
import com.teste.primeiroexemplo.view.model.ProdutoResponse;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

  @Autowired
  private ProdutoService produtoService;

  @GetMapping
  public ResponseEntity<List<ProdutoResponse>> obterTodos() {
    List<ProdutoDTO> produtos = produtoService.obterTodos();
    // converter para response
    ModelMapper mapper = new ModelMapper();

    // converte os objetos em dto para o tipo response para que seja retornado
    List<ProdutoResponse> resposta = produtos.stream().map(produtoDto -> mapper.map(produtoDto, ProdutoResponse.class))
        .collect(Collectors.toList());
    return new ResponseEntity<>(resposta, HttpStatus.OK); // retorna diretamente a lista e o status ok 200

  }

  @PostMapping
  public ResponseEntity<ProdutoResponse> adicionar(@RequestBody ProdutoRequest produtoReq) {

    // converter para dto para envio para service
    ModelMapper mapper = new ModelMapper();

    ProdutoDTO dto = mapper.map(produtoReq, ProdutoDTO.class);

    produtoService.adicionar(dto);

    // devolver um response

    return new ResponseEntity<>(mapper.map(dto, ProdutoResponse.class), HttpStatus.CREATED);

  }

  // id é direcionado pelo pathvariable para que vá para o parametro o controller
  @GetMapping("/{id}")
  public ResponseEntity<Optional<ProdutoResponse>> obteProdutoPorId(@PathVariable Integer id) {
    // try{

    Optional<ProdutoDTO> dto = produtoService.obteProdutoPorId(id);
    ModelMapper mapper = new ModelMapper();
    ProdutoResponse resposta = mapper.map(dto.get(), ProdutoResponse.class);
    return new ResponseEntity<>(Optional.of(resposta), HttpStatus.OK);

    // }catch (Exception e){
    // return new ResponseEntity<>(HttpStatus.NO_CONTENT); É POSSIVEL DEVOLVER CORPO
    // VAZIO APENAS COM O STATUS CODE
    // }

  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deletarProduto(@PathVariable Integer id) {
    produtoService.deletarProduto(id);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProdutoResponse> atualizarProduto(@RequestBody ProdutoRequest produtoReq,
      @PathVariable Integer id) {
    ModelMapper mapper = new ModelMapper();
    ProdutoDTO dto = mapper.map(produtoReq, ProdutoDTO.class);
    dto = produtoService.atualizarProduto(id, dto);
    ProdutoResponse resposta = mapper.map(dto, ProdutoResponse.class);
    return new ResponseEntity<>(resposta, HttpStatus.OK);

  }

  @GetMapping("/Order")
  public ResponseEntity<List<ProdutoResponse>> ordernarProduto() {
    List<ProdutoDTO> DtoOrder = produtoService.OrdenarPreco();

    ModelMapper mapper = new ModelMapper();

    List<ProdutoResponse> resposta = DtoOrder.stream().map(produtoDto -> mapper.map(produtoDto, ProdutoResponse.class))
        .collect(Collectors.toList());
    return new ResponseEntity<>(resposta, HttpStatus.ACCEPTED);

  }

}
