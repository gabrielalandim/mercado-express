package br.com.fiap.mercadoexpress.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data // Lombok obrigatório gerando getters, setters, toString, etc.
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "TDS_TB_mercado") // Tabela especificada no documento
public class Produto extends RepresentationModel<Produto> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Coluna Id
    private String nome; // Coluna Nome
    private String tipo; // Coluna Tipo
    private String setor; // Coluna Setor
    private String tamanho; // Coluna Tamanho
    private Double preco; // Coluna Preco
}