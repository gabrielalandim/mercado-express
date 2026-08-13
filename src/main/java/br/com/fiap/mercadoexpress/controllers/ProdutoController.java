package br.com.fiap.mercadoexpress.controllers;

import br.com.fiap.mercadoexpress.models.Produto;
import br.com.fiap.mercadoexpress.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/mercado") // Endpoint principal exigido
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    // READ - GET All
    @GetMapping
    public ResponseEntity<List<Produto>> getAll() {
        List<Produto> produtos = service.listarTodos();
        if (produtos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        // Adicionando links HATEOAS para cada item da lista
        for (Produto produto : produtos) {
            long id = produto.getId();
            produto.add(linkTo(methodOn(ProdutoController.class).getById(id)).withSelfRel());
        }
        return new ResponseEntity<>(produtos, HttpStatus.OK);
    }

    // READ - GET by ID (Consulta específica exigida)
    @GetMapping("/{id}")
    public ResponseEntity<Produto> getById(@PathVariable Long id) {
        Optional<Produto> produtoO = service.buscarPorId(id);
        if (produtoO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Produto produto = produtoO.get();
        // HATEOAS Nível 3: Link para ele mesmo e link para a lista completa
        produto.add(linkTo(methodOn(ProdutoController.class).getById(id)).withSelfRel());
        produto.add(linkTo(methodOn(ProdutoController.class).getAll()).withRel("Lista de Produtos"));

        // Retorna o resultado da consulta com as informações do produto solicitado
        return new ResponseEntity<>(produto, HttpStatus.OK);
    }

    // CREATE - POST
    @PostMapping
    public ResponseEntity<Produto> create(@RequestBody Produto produto) {
        Produto novoProduto = service.salvar(produto);
        // Retorna HATEOAS no POST também
        novoProduto.add(linkTo(methodOn(ProdutoController.class).getById(novoProduto.getId())).withSelfRel());
        return new ResponseEntity<>(novoProduto, HttpStatus.CREATED);
    }

    // UPDATE - PUT (Atualização completa)
    @PutMapping("/{id}")
    public ResponseEntity<Produto> updatePut(@PathVariable Long id, @RequestBody Produto produtoAtualizado) {
        Optional<Produto> produtoO = service.buscarPorId(id);
        if (produtoO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        produtoAtualizado.setId(id);
        Produto salvo = service.salvar(produtoAtualizado);
        salvo.add(linkTo(methodOn(ProdutoController.class).getById(salvo.getId())).withSelfRel());
        return new ResponseEntity<>(salvo, HttpStatus.OK);
    }

    // UPDATE - PATCH (Atualização parcial)
    @PatchMapping("/{id}")
    public ResponseEntity<Produto> updatePatch(@PathVariable Long id, @RequestBody Produto produtoAtualizado) {
        Optional<Produto> produtoO = service.buscarPorId(id);
        if (produtoO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Produto existente = produtoO.get();
        // Lógica simples de merge para PATCH
        if (produtoAtualizado.getNome() != null) existente.setNome(produtoAtualizado.getNome());
        if (produtoAtualizado.getTipo() != null) existente.setTipo(produtoAtualizado.getTipo());
        if (produtoAtualizado.getSetor() != null) existente.setSetor(produtoAtualizado.getSetor());
        if (produtoAtualizado.getTamanho() != null) existente.setTamanho(produtoAtualizado.getTamanho());
        if (produtoAtualizado.getPreco() != null) existente.setPreco(produtoAtualizado.getPreco());

        Produto salvo = service.salvar(existente);
        salvo.add(linkTo(methodOn(ProdutoController.class).getById(salvo.getId())).withSelfRel());
        return new ResponseEntity<>(salvo, HttpStatus.OK);
    }

    // DELETE - Exclusão do BD pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Produto> produtoO = service.buscarPorId(id);
        if (produtoO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        service.excluir(id); // Realiza a exclusão do BD pelo ID
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}