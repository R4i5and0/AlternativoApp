package com.example.galpaoalternativoapp.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galpaoalternativoapp.R;
import com.example.galpaoalternativoapp.model.ItemCardapio;

import java.util.List;

public class CarrinhoAdapter extends RecyclerView.Adapter<CarrinhoAdapter.CarrinhoViewHolder> {

    private final List<ItemCardapio> listaCarrinho;

    public CarrinhoAdapter(List<ItemCardapio> listaCarrinho) {
        this.listaCarrinho = listaCarrinho;
    }

    @NonNull
    @Override
    public CarrinhoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_carrinho, parent, false);
        return new CarrinhoViewHolder(itemView);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull CarrinhoViewHolder holder, int position) {
        ItemCardapio item = listaCarrinho.get(position);
        holder.nome.setText(item.getNome());
        holder.preco.setText(String.format("R$ %.2f", item.getPreco()));
    }

    @Override
    public int getItemCount() {
        return listaCarrinho.size();
    }

    public static class CarrinhoViewHolder extends RecyclerView.ViewHolder {
        TextView nome, preco;

        public CarrinhoViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.textNomeCarrinho);
            preco = itemView.findViewById(R.id.textPrecoCarrinho);
        }
    }
}
