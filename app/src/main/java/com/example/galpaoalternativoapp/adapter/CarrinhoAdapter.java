package com.example.galpaoalternativoapp.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galpaoalternativoapp.R;
import com.example.galpaoalternativoapp.model.ItemCarrinho; // ALTERADO para ItemCarrinho

import java.util.List;

public class CarrinhoAdapter extends RecyclerView.Adapter<CarrinhoAdapter.CarrinhoViewHolder> {

    private final List<ItemCarrinho> listaCarrinho; // ALTERADO para List<ItemCarrinho>
    private OnCarrinhoInteractionListener listener;

    public interface OnCarrinhoInteractionListener {
        void onAumentarClick(int position);
        void onDiminuirClick(int position);
        void onRemoverClick(int position);
    }

    public CarrinhoAdapter(List<ItemCarrinho> listaCarrinho, OnCarrinhoInteractionListener listener) {
        this.listaCarrinho = listaCarrinho;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CarrinhoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_carrinho, parent, false);
        return new CarrinhoViewHolder(itemView);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull CarrinhoViewHolder holder, int position) {
        ItemCarrinho item = listaCarrinho.get(position); // ALTERADO para ItemCarrinho

        holder.nome.setText(item.getNome());
        holder.preco.setText(String.format("R$ %.2f", item.getPreco()));
        holder.quantidade.setText(String.valueOf(item.getQuantidade()));

        holder.btnMais.setOnClickListener(v -> {
            if (listener != null) listener.onAumentarClick(holder.getAdapterPosition());
        });
        holder.btnMenos.setOnClickListener(v -> {
            if (listener != null) listener.onDiminuirClick(holder.getAdapterPosition());
        });
        holder.btnRemover.setOnClickListener(v -> {
            if (listener != null) listener.onRemoverClick(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return listaCarrinho.size();
    }

    public static class CarrinhoViewHolder extends RecyclerView.ViewHolder {
        TextView nome, preco, quantidade;
        ImageButton btnMenos, btnMais, btnRemover;

        public CarrinhoViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.textNomeCarrinho);
            preco = itemView.findViewById(R.id.textPrecoCarrinho);
            quantidade = itemView.findViewById(R.id.textViewQuantidade);
            btnMenos = itemView.findViewById(R.id.buttonMenos);
            btnMais = itemView.findViewById(R.id.buttonMais);
            btnRemover = itemView.findViewById(R.id.buttonRemover);
        }
    }
}