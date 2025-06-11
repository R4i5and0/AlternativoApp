// Arquivo: adapter/EventoAdapter.java - VERSÃO FINAL
package com.example.galpaoalternativoapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.galpaoalternativoapp.R;
import com.example.galpaoalternativoapp.model.Evento;
import java.util.List;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.ViewHolder> {

    private final List<Evento> eventos;

    public EventoAdapter(List<Evento> eventos) {
        this.eventos = eventos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Usa o nosso novo layout de item com foto
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_evento, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Pega o evento da posição atual
        Evento evento = eventos.get(position);

        // Preenche todos os campos do layout com os dados do evento
        holder.tvNome.setText(evento.getNome());
        holder.tvDescricao.setText(evento.getDescricao());
        holder.tvData.setText(evento.getData());
        holder.imgEvento.setImageResource(evento.getImagem()); // <-- COLOCA A IMAGEM
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    // ViewHolder atualizado para encontrar a ImageView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome, tvDescricao, tvData;
        ImageView imgEvento; // <-- ADICIONADO

        public ViewHolder(View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNomeEvento);
            tvDescricao = itemView.findViewById(R.id.tvDescricaoEvento);
            tvData = itemView.findViewById(R.id.tvDataEvento);
            imgEvento = itemView.findViewById(R.id.imgEvento); // <-- ADICIONADO
        }
    }
}