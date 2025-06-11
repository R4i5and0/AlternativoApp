package com.example.galpaoalternativoapp.model;
import java.util.ArrayList;
import java.util.List;
public class CarrinhoSingleton {
    private static final CarrinhoSingleton instance = new CarrinhoSingleton();
    private final List<ItemCardapio> carrinho = new ArrayList<>();
    private CarrinhoSingleton() {}
    public static CarrinhoSingleton getInstance() {
        return instance;
    }
    public void adicionarItem(ItemCardapio item) {
        carrinho.add(item);
    }
    public List<ItemCardapio> getCarrinho() {
        return carrinho;
    }
    public void limpar() {
        carrinho.clear();
    }
}
