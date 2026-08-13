package br.com.fiap.mercadoexpress.dtos;

public record ProdutoRequestDTO(
        String nome,
        String tipo,
        String setor,
        String tamanho,
        Double preco
) {
}