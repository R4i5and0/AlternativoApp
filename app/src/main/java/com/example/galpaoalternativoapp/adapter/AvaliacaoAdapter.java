// Arquivo: adapter/AvaliacaoAdapter.java - VERSÃO FINAL E COMPLETA
package com.example.galpaoalternativoapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.galpaoalternativoapp.R;
import com.example.galpaoalternativoapp.model.Avaliacao;
import java.util.List;

public class AvaliacaoAdapter extends RecyclerView.Adapter<AvaliacaoAdapter.AvaliacaoViewHolder> {

    // --- Interface para comunicação com a Activity ---
    public interface OnItemActionClickListener {
        void onEditClick(Avaliacao avaliacao);
        void onDeleteClick(int position, int avaliacaoId);
    }

    private final List<Avaliacao> avaliacoes;
    private final int idUsuarioLogado;
    private final OnItemActionClickListener actionListener;

    public AvaliacaoAdapter(List<Avaliacao> avaliacoes, int idUsuarioLogado, OnItemActionClickListener listener) {
        this.avaliacoes = avaliacoes;
        this.idUsuarioLogado = idUsuarioLogado;
        this.actionListener = listener;
    }

    @NonNull
    @Override
    public AvaliacaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_avaliacao, parent, false);
        return new AvaliacaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvaliacaoViewHolder holder, int position) {
        Avaliacao avaliacao = avaliacoes.get(position);

        // Preenche todos os campos do layout com os dados da avaliação
        holder.tvNomeUsuario.setText(avaliacao.getNomeUsuario());
        holder.tvComentario.setText(avaliacao.getComentario());
        holder.ratingBar.setRating(avaliacao.getNota());
        holder.tvTipo.setText("Tipo: " + avaliacao.getTipo());

        // Mostra ou esconde os botões de acordo com o dono da avaliação
        if (avaliacao.getUsuarioId() == idUsuarioLogado) {
            holder.layoutBotoes.setVisibility(View.VISIBLE);
        } else {
            holder.layoutBotoes.setVisibility(View.GONE);
        }

        // Configura os cliques dos botões
        holder.btnEditar.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onEditClick(avaliacao);
            }
        });

        holder.btnExcluir.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onDeleteClick(position, avaliacao.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return avaliacoes.size();
    }

    // ViewHolder encontra todos os componentes do layout item_avaliacao.xml
    public static class AvaliacaoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomeUsuario, tvComentario, tvTipo;
        RatingBar ratingBar;
        LinearLayout layoutBotoes;
        Button btnEditar, btnExcluir;

        public AvaliacaoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNomeUsuario = itemView.findViewById(R.id.tvNomeUsuarioAvaliacao);
            tvComentario = itemView.findViewById(R.id.tvComentarioAvaliacao);
            ratingBar = itemView.findViewById(R.id.ratingBarSmall);
            layoutBotoes = itemView.findViewById(R.id.layoutBotoes);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnExcluir = itemView.findViewById(R.id.btnExcluir);
            tvTipo = itemView.findViewById(R.id.tvTipoAvaliacao); // Encontrando o TextView do tipo
        }
    }
}