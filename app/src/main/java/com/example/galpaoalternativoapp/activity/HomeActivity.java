package com.example.galpaoalternativoapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.AlertDialog;
import android.widget.Button;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.galpaoalternativoapp.R;
import com.example.galpaoalternativoapp.controller.DBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity {

    private int idDoUsuarioLogado = -1;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.d("HomeActivityDebug", "onCreate: HomeActivity iniciada.");

        dbHelper = new DBHelper(this);


        // RECEBE O ID DO USUÁRIO LOGADO DO LoginActivity
        idDoUsuarioLogado = getIntent().getIntExtra("ID_DO_USUARIO", -1);
        Log.d("HomeActivityDebug", "onCreate: ID_DO_USUARIO recebido: " + idDoUsuarioLogado);

        if (idDoUsuarioLogado == -1) {
            Log.e("HomeActivityDebug", "onCreate: ID_DO_USUARIO é -1. Erro de sessão.");
            Toast.makeText(this, "Erro de sessão. Por favor, faça login novamente.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        Log.d("HomeActivityDebug", "onCreate: ID do usuário válido, configurando UI.");

        CardView btnCardapio = findViewById(R.id.btnCardapio);
        CardView btnEventos = findViewById(R.id.btnEventos);
        CardView btnMural = findViewById(R.id.btnMural);
        CardView btnAvaliacao = findViewById(R.id.btnAvaliacao);
        FloatingActionButton btnVerPedido = findViewById(R.id.btnVerMeuPedido);

        if (btnCardapio == null) Log.e("HomeActivityDebug", "btnCardapio is null!");
        if (btnEventos == null) Log.e("HomeActivityDebug", "btnEventos is null!");
        if (btnMural == null) Log.e("HomeActivityDebug", "btnMural is null!");
        if (btnAvaliacao == null) Log.e("HomeActivityDebug", "btnAvaliacao is null!");
        if (btnVerPedido == null) Log.e("HomeActivityDebug", "btnVerMeuPedido is null!");
        Button btnLogout = findViewById(R.id.btnLogout);


        // CORREÇÃO: Passar o ID do usuário para a CardapioActivity
        btnCardapio.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CardapioActivity.class);
            intent.putExtra("ID_DO_USUARIO", idDoUsuarioLogado); // Passa o ID do usuário
            startActivity(intent);
        });

        btnEventos.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, EventoActivity.class)));

        btnMural.setOnClickListener(v -> {
            Intent intentMural = new Intent(HomeActivity.this, MuralActivity.class);
            intentMural.putExtra("USUARIO_ID", idDoUsuarioLogado);
            startActivity(intentMural);
        });

        btnAvaliacao.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AvaliacaoActivity.class);
            intent.putExtra("ID_DO_USUARIO", idDoUsuarioLogado);
            startActivity(intent);
        });

        btnVerPedido.setOnClickListener(v -> {
            Log.d("HomeActivityDebug", "btnVerMeuPedido clicado.");

            String detalhesDoPedido = dbHelper.getUltimoPedidoDoUsuario(idDoUsuarioLogado);
            Log.d("HomeActivityDebug", "Detalhes do pedido: " + (detalhesDoPedido != null ? detalhesDoPedido.substring(0, Math.min(detalhesDoPedido.length(), 100)) + "..." : "Nenhum"));

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            if (detalhesDoPedido != null) {
                builder.setTitle("✅ Seu Último Pedido");
                builder.setMessage(detalhesDoPedido);
            } else {
                builder.setTitle("🤔 Nenhum Pedido Encontrado");
                builder.setMessage("Você ainda não realizou nenhum pedido.");
            }

            builder.setPositiveButton("OK", null);
            builder.show();
        });

        btnLogout.setOnClickListener(v -> {
            // Limpa qualquer dado de sessão (se você tiver SharedPreferences para guardar o login, limpe-o aqui)
            // Exemplo: getSharedPreferences("login_prefs", MODE_PRIVATE).edit().clear().apply();

            // Redireciona para a LoginActivity
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            // Flags para limpar o back stack e evitar que o usuário volte para a HomeActivity
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Finaliza a HomeActivity
            Toast.makeText(this, "Logout realizado com sucesso!", Toast.LENGTH_SHORT).show();
        });

        Log.d("HomeActivityDebug", "onCreate: HomeActivity configuração completa.");
    }
}