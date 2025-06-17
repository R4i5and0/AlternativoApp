package com.example.galpaoalternativoapp.model;

/**
 * Esta classe representa um item DENTRO do carrinho de compras.
 * Ela serve como um "envelope" para um ItemCardapio, adicionando
 * a funcionalidade essencial de controlar a QUANTIDADE.
 */
public class ItemCarrinho {

    // Guarda a referência ao produto original do cardápio.
    private final ItemCardapio itemCardapio;

    // Guarda a quantidade deste item no carrinho.
    private int quantidade;

    /**
     * Construtor que cria um item de carrinho a partir de um item de cardápio.
     * A quantidade inicial é sempre 1.
     * @param itemCardapio O produto original que está sendo adicionado.
     */
    public ItemCarrinho(ItemCardapio itemCardapio) {
        this.itemCardapio = itemCardapio;
        this.quantidade = 1; // Todo item adicionado começa com quantidade 1.
    }

    // --- MÉTODOS DE ACESSO AOS DADOS DO PRODUTO ORIGINAL ---

    public int getId() {
        return this.itemCardapio.getId();
    }

    public String getNome() {
        return this.itemCardapio.getNome();
    }

    public double getPreco() {
        return this.itemCardapio.getPreco();
    }


    // --- MÉTODOS PARA CONTROLAR A QUANTIDADE ---

    public int getQuantidade() {
        return this.quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    /**
     * Método de ajuda para facilmente aumentar a quantidade em 1.
     */
    public void incrementarQuantidade() {
        this.quantidade++;
    }
}
