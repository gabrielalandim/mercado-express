package br.com.fiap.mercadoexpress.repositories;

import br.com.fiap.mercadoexpress.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
