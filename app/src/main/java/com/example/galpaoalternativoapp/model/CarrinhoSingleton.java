package com.example.galpaoalternativoapp.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe que gerencia o estado do carrinho de compras de forma global (Singleton).
 * Garante que só exista uma instância do carrinho em todo o app.
 */
public class CarrinhoSingleton {
    private static final CarrinhoSingleton instance = new CarrinhoSingleton();

    // Usamos um Map para agrupar itens pelo ID e evitar duplicatas, controlando a quantidade.
    private final Map<Integer, ItemCarrinho> carrinho = new LinkedHashMap<>();

    // Construtor privado para impedir a criação de novas instâncias.
    private CarrinhoSingleton() {}

    /**
     * Retorna a única instância do carrinho.
     */
    public static CarrinhoSingleton getInstance() {
        return instance;
    }

    /**
     * Adiciona um item ao carrinho. Se o item já existir, incrementa sua quantidade.
     * @param itemCardapio O item do cardápio a ser adicionado.
     */
    public void adicionarItem(ItemCardapio itemCardapio) {
        if (carrinho.containsKey(itemCardapio.getId())) {
            // Se o item já existe, apenas pega o ItemCarrinho correspondente e incrementa a quantidade.
            ItemCarrinho itemExistente = carrinho.get(itemCardapio.getId());
            if (itemExistente != null) {
                itemExistente.incrementarQuantidade();
            }
        } else {
            // Se é um item novo, cria um novo ItemCarrinho e o adiciona ao Map.
            carrinho.put(itemCardapio.getId(), new ItemCarrinho(itemCardapio));
        }
    }

    /**
     * Diminui a quantidade de um item no carrinho. Se a quantidade chegar a zero, remove o item.
     * @param itemCarrinho O item a ter sua quantidade diminuída.
     */
    public void diminuirItem(ItemCarrinho itemCarrinho) {
        ItemCarrinho itemExistente = carrinho.get(itemCarrinho.getId());
        if (itemExistente != null) {
            if (itemExistente.getQuantidade() > 1) {
                itemExistente.setQuantidade(itemExistente.getQuantidade() - 1);
            } else {
                // Se a quantidade atual é 1, diminuir significa remover o item completamente.
                removerItem(itemCarrinho);
            }
        }
    }

    /**
     * Remove um item completamente do carrinho, não importa a quantidade.
     * @param itemCarrinho O item a ser removido.
     */
    public void removerItem(ItemCarrinho itemCarrinho) {
        carrinho.remove(itemCarrinho.getId());
    }

    /**
     * Retorna uma lista com todos os itens atualmente no carrinho.
     * @return Uma nova lista contendo os objetos ItemCarrinho.
     */
    public List<ItemCarrinho> getItensDoCarrinho() {
        // Retorna uma nova ArrayList para evitar modificações externas na lista original.
        return new ArrayList<>(carrinho.values());
    }

    /**
     * Limpa completamente o carrinho, removendo todos os itens.
     * Usado após a finalização de um pedido.
     */
    public void limpar() {
        carrinho.clear();
    }

    /**
     * Verifica se o carrinho está vazio.
     * @return true se não houver itens no carrinho, false caso contrário.
     */
    public boolean isEmpty() {
        return carrinho.isEmpty();
    }
}
