package br.com.fiap.mercadoexpress.services;

import br.com.fiap.mercadoexpress.models.Produto;
import br.com.fiap.mercadoexpress.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    // Lista intermediária sugerida para manipular os dados antes do commit no banco
    private List<Produto> listaTemporaria = new ArrayList<>();

    public Produto salvar(Produto produto) {
        // Envia para a lista no Programa desenvolvido
        listaTemporaria.add(produto);

        // Recupera a informação da lista
        Produto produtoParaCommit = listaTemporaria.get(listaTemporaria.size() - 1);

        // Envia ao BD Oracle para o Commit (inserção/atualização)
        Produto produtoSalvo = repository.save(produtoParaCommit);

        // Limpa a memória após persistir
        listaTemporaria.remove(produto);

        return produtoSalvo;
    }

    public List<Produto> listarTodos() {
        return repository.findAll();
    }

    public Optional<Produto> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public void excluir(Long id) {
        // Exclusão do BD pelo ID
        repository.deleteById(id);
    }
}
