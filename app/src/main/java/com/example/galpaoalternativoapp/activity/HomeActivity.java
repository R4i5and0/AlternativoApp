package com.example.galpaoalternativoapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

        // Este é o seu listener do botão. Substitua o conteúdo dele por isto:
        // Este é o seu listener do botão. Substitua o conteúdo dele por isto:
        btnVerPedido.setOnClickListener(v -> {
            Log.d("HomeActivityDebug", "btnVerMeuPedido clicado.");

            String detalhesDoPedido = dbHelper.getUltimoPedidoDoUsuario(idDoUsuarioLogado);
            Log.d("HomeActivityDebug", "Detalhes do pedido: " + (detalhesDoPedido != null ? "Encontrado" : "Nenhum"));

            // Carrega o layout customizado
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_ultimo_pedido, null);

            // Cria o AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView);
            final AlertDialog dialog = builder.create();

            // Remove o fundo padrão do sistema para que nossos cantos arredondados apareçam
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }

            // Pega os componentes de dentro do nosso layout
            TextView tvTitle = dialogView.findViewById(R.id.dialog_title);
            TextView tvMessage = dialogView.findViewById(R.id.dialog_message);
            ImageView ivIcon = dialogView.findViewById(R.id.dialog_icon);
            Button btnOk = dialogView.findViewById(R.id.dialog_button_ok);

            // Configura o conteúdo do diálogo
            if (detalhesDoPedido != null) {
                tvTitle.setText(" Seu Último Pedido");
                tvMessage.setText(detalhesDoPedido);
                ivIcon.setImageResource(R.drawable.ic_receipt); // Ícone de recibo (já deve existir)
            } else {
                // CORRIGIDO: Agora que o ícone ic_info existe, esta linha não vai mais travar o app.
                tvTitle.setText("🤔 Nenhum Pedido");
                tvMessage.setText("Você ainda não realizou nenhum pedido no nosso app.");
            }

            // Define a ação do botão OK para fechar o diálogo
            btnOk.setOnClickListener(view -> dialog.dismiss());

            // Mostra o diálogo customizado
            dialog.show();
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