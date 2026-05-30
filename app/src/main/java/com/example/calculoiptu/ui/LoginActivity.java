package com.example.calculoiptu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.calculoiptu.R;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsuario;
    private EditText etSenha;
    private Button btnEntrar;
    private TextView tvSobre;

    // Credenciais fixas para fins acadêmicos
    private static final String USUARIO_VALIDO = "admin";
    private static final String SENHA_VALIDA = "fatec123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsuario = findViewById(R.id.etUsuario);
        etSenha = findViewById(R.id.etSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        tvSobre = findViewById(R.id.tvSobre);

        btnEntrar.setOnClickListener(v -> realizarLogin());
        tvSobre.setOnClickListener(v -> abrirSobre());
    }

    private void realizarLogin() {
        String usuario = etUsuario.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();

        if (usuario.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha usuário e senha.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (usuario.equals(USUARIO_VALIDO) && senha.equals(SENHA_VALIDA)) {
            Intent intent = new Intent(this, CalculoIptuActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Usuário ou senha inválidos.", Toast.LENGTH_SHORT).show();
        }
    }

    private void abrirSobre() {
        Intent intent = new Intent(this, SobreActivity.class);
        startActivity(intent);
    }
}
