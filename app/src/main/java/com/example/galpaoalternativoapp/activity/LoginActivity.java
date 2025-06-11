package com.example.galpaoalternativoapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
//import android.util.Log; // <<< ADICIONE ESTA LINHA <<<

import androidx.appcompat.app.AppCompatActivity;
import com.example.galpaoalternativoapp.R;
import com.example.galpaoalternativoapp.controller.DBHelper;


public class LoginActivity extends AppCompatActivity {

    private EditText edtEmailLogin, edtSenhaLogin;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Log.d("MeuAppGeral", "LoginActivity iniciada. Isso é um teste de log."); // <<< ADICIONE ESTA LINHA AQUI <<<

        dbHelper = new DBHelper(this);

        edtEmailLogin = findViewById(R.id.edtEmailLogin);
        edtSenhaLogin = findViewById(R.id.edtSenhaLogin);
        Button btnEntrar = findViewById(R.id.btnEntrar);


        btnEntrar.setOnClickListener(v -> realizarLogin());

        Button btnCadastrarLogin = findViewById(R.id.btnCadastrarLogin);
        btnCadastrarLogin.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
            startActivity(intent);
        });


    }

    private void realizarLogin() {
        String email = edtEmailLogin.getText().toString().trim();
        String senha = edtSenhaLogin.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        // AGORA, dbHelper.verificarUsuario retorna o ID do usuário (ou -1)
        int userId = dbHelper.verificarUsuario(email, senha);

        if (userId != -1) { // Se userId for diferente de -1, o login foi bem-sucedido
            Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();

            Intent intent;
            if (email.equals("admin@admin.com")) { // Checa se é o admin
                intent = new Intent(this, AdminActivity.class);
            } else {
                intent = new Intent(this, HomeActivity.class);
                // PASSA O ID DO USUÁRIO PARA A HOMEACTIVITY AQUI!
                intent.putExtra("ID_DO_USUARIO", userId);
            }
            startActivity(intent);
            finish(); // Finaliza a LoginActivity para que o usuário não possa voltar para ela
        } else {
            Toast.makeText(this, "Email ou senha incorretos!", Toast.LENGTH_SHORT).show();
        }
    }
}