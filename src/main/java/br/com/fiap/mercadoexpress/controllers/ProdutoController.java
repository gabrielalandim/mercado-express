package br.com.fiap.mercadoexpress.controllers;

import br.com.fiap.mercadoexpress.dtos.ProdutoRequestDTO;
import br.com.fiap.mercadoexpress.models.Produto;
import br.com.fiap.mercadoexpress.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/mercado")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @GetMapping
    public ResponseEntity<List<Produto>> getAll() {
        List<Produto> produtos = service.listarTodos();

        if (produtos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        for (Produto produto : produtos) {
            long id = produto.getId();
            produto.add(linkTo(methodOn(ProdutoController.class).getById(id)).withSelfRel());
        }

        return new ResponseEntity<>(produtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> getById(@PathVariable Long id) {
        Optional<Produto> produtoO = service.buscarPorId(id);

        if (produtoO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Produto produto = produtoO.get();
        produto.add(linkTo(methodOn(ProdutoController.class).getById(id)).withSelfRel());
        produto.add(linkTo(methodOn(ProdutoController.class).getAll()).withRel("Lista de Produtos"));

        return new ResponseEntity<>(produto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Produto> create(@RequestBody ProdutoRequestDTO dto) {
        Produto produto = new Produto();
        produto.setNome(dto.nome());
        produto.setTipo(dto.tipo());
        produto.setSetor(dto.setor());
        produto.setTamanho(dto.tamanho());
        produto.setPreco(dto.preco());

        Produto novoProduto = service.salvar(produto);
        novoProduto.add(linkTo(methodOn(ProdutoController.class).getById(novoProduto.getId())).withSelfRel());

        return new ResponseEntity<>(novoProduto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> updatePut(@PathVariable Long id, @RequestBody ProdutoRequestDTO dto) {
        Optional<Produto> produtoO = service.buscarPorId(id);

        if (produtoO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Produto produtoAtualizado = produtoO.get();
        produtoAtualizado.setNome(dto.nome());
        produtoAtualizado.setTipo(dto.tipo());
        produtoAtualizado.setSetor(dto.setor());
        produtoAtualizado.setTamanho(dto.tamanho());
        produtoAtualizado.setPreco(dto.preco());

        Produto salvo = service.salvar(produtoAtualizado);
        salvo.add(linkTo(methodOn(ProdutoController.class).getById(salvo.getId())).withSelfRel());

        return new ResponseEntity<>(salvo, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Produto> updatePatch(@PathVariable Long id, @RequestBody ProdutoRequestDTO dto) {
        Optional<Produto> produtoO = service.buscarPorId(id);

        if (produtoO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Produto existente = produtoO.get();

        if (dto.nome() != null) existente.setNome(dto.nome());
        if (dto.tipo() != null) existente.setTipo(dto.tipo());
        if (dto.setor() != null) existente.setSetor(dto.setor());
        if (dto.tamanho() != null) existente.setTamanho(dto.tamanho());
        if (dto.preco() != null) existente.setPreco(dto.preco());

        Produto salvo = service.salvar(existente);
        salvo.add(linkTo(methodOn(ProdutoController.class).getById(salvo.getId())).withSelfRel());

        return new ResponseEntity<>(salvo, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Produto> produtoO = service.buscarPorId(id);

        if (produtoO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        service.excluir(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}