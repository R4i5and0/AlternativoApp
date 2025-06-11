// Arquivo: activity/EventoActivity.java - VERSÃO ATUALIZADA
package com.example.galpaoalternativoapp.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galpaoalternativoapp.R;
import com.example.galpaoalternativoapp.adapter.EventoAdapter;
import com.example.galpaoalternativoapp.model.Evento;
import java.util.ArrayList;
import java.util.List;

public class EventoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewEventos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Evento> listaEventos = new ArrayList<>();

        // Adicionando os eventos com suas respectivas imagens
        listaEventos.add(new Evento("🎸 Noite do Rock", "Bandas autorais ao vivo 🤘", "08/06 - 21h", R.drawable.noite_rock));
        listaEventos.add(new Evento("🎤 Karaokê Livre", "Cante no palco! 🎶", "10/06 - 20h", R.drawable.karaoke));
        listaEventos.add(new Evento("🎵 Sábado Acústico", "Clássicos em versão acústica 🎸", "15/06 - 22h", R.drawable.sabado_acustico));
        listaEventos.add(new Evento("🔥 Festa Punk", "Música e vibe alternativa 🖤", "20/06 - 23h", R.drawable.festa_punk));
        listaEventos.add(new Evento("🍻 Happy Hour", "Drinks especiais e DJ 🎧", "25/06 - 18h", R.drawable.happy_hour));
        listaEventos.add(new Evento("🎷 Jazz Night", "Improvisos e clima descontraído 🎺", "30/06 - 21h", R.drawable.jazz_night));

        recyclerView.setAdapter(new EventoAdapter(listaEventos));
    }
}