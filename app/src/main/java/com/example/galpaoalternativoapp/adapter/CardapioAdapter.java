// Arquivo: CardapioAdapter.java - VERSÃO FINAL E CORRIGIDA
package com.example.galpaoalternativoapp.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.galpaoalternativoapp.R;
import com.example.galpaoalternativoapp.model.ItemCardapio;
import java.util.List;
import java.util.Locale;

public class CardapioAdapter extends RecyclerView.Adapter<CardapioAdapter.ViewHolder> {

    // Interface para o clique do botão "Adicionar"
    public interface OnItemClickListener {
        void onAdicionarClick(ItemCardapio item);
    }

    private final List<ItemCardapio> itens;
    private final OnItemClickListener listener;

    public CardapioAdapter(List<ItemCardapio> itens, OnItemClickListener listener) {
        this.itens = itens;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Usa o nosso novo layout de item com foto
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cardapio, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Pega o item da lista na posição atual
        ItemCardapio item = itens.get(position);

        // Preenche todos os campos do layout com os dados do item
        holder.tvNome.setText(item.getNome());
        holder.tvDescricao.setText(item.getDescricao());
        // Formata o preço para o formato R$ 00,00
        holder.tvPreco.setText(String.format(Locale.getDefault(), "R$ %.2f", item.getPreco()));
        // Coloca a imagem correta na ImageView
        holder.imgItemCardapio.setImageResource(item.getImagem());

        // Configura o clique do botão "Adicionar"
        holder.btnAdicionar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAdicionarClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    // O ViewHolder agora declara todos os componentes do novo layout
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItemCardapio;
        TextView tvNome, tvDescricao, tvPreco;
        Button btnAdicionar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Encontra todos os componentes pelos seus IDs corretos
            imgItemCardapio = itemView.findViewById(R.id.imgItemCardapio);
            tvNome = itemView.findViewById(R.id.tvNomeItem);
            tvDescricao = itemView.findViewById(R.id.tvDescricaoItem);
            tvPreco = itemView.findViewById(R.id.tvPrecoItem);
            btnAdicionar = itemView.findViewById(R.id.btnAdicionar);
        }
    }
}