package com.example.calculoiptu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.example.calculoiptu.R;
import com.example.calculoiptu.data.AppDatabase;
import com.example.calculoiptu.data.IptuRecord;
import com.example.calculoiptu.model.IptuCalculador;

public class CalculoIptuActivity extends AppCompatActivity {
    private EditText etCpf;
    private EditText etNome;
    private EditText etValorOriginal;
    private EditText etMesesAtraso;
    private Button btnCalcular;
    private Button btnVoltar;
    private LinearLayout layoutResultado;
    private TextView tvMulta;
    private TextView tvJuros;
    private TextView tvTotal;
    private AppDatabase database;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculo_iptu);

        database = AppDatabase.getInstance(this);
        etCpf = findViewById(R.id.etCpf);
        etNome = findViewById(R.id.etNome);
        etValorOriginal = findViewById(R.id.etValorOriginal);
        etMesesAtraso = findViewById(R.id.etMesesAtraso);
        btnCalcular = findViewById(R.id.btnCalcular);
        layoutResultado = findViewById(R.id.layoutResultado);
        tvMulta = findViewById(R.id.tvMulta);
        tvJuros = findViewById(R.id.tvJuros);
        tvTotal = findViewById(R.id.tvTotal);
        btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(v -> voltarParaLogin());

        btnCalcular.setOnClickListener(v -> calcularESalvar());
    }

    private void calcularESalvar() {
        String cpf = etCpf.getText().toString().trim();
        String nome = etNome.getText().toString().trim();
        String valorStr = etValorOriginal.getText().toString().trim();
        String mesesStr = etMesesAtraso.getText().toString().trim();

        if (cpf.isEmpty() || nome.isEmpty() || valorStr.isEmpty() || mesesStr.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!cpfValido(cpf)) {
            Toast.makeText(this, "CPF inválido. Verifique o número informado.", Toast.LENGTH_SHORT).show();
            return;
        }

        double valorOriginal;
        int mesesAtraso;

        try {
            valorOriginal = Double.parseDouble(valorStr.replace(",", "."));
            mesesAtraso = Integer.parseInt(mesesStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Valores numéricos inválidos.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (valorOriginal <= 0) {
            Toast.makeText(this, "O valor do IPTU deve ser maior que zero.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mesesAtraso < 0) {
            Toast.makeText(this, "Meses em atraso não pode ser negativo.", Toast.LENGTH_SHORT).show();
            return;
        }

        IptuCalculador calculador = new IptuCalculador(valorOriginal, mesesAtraso);

        double multa = calculador.calcularMulta();
        double juros = calculador.calcularJuros();
        double total = calculador.calcularTotal();

        tvMulta.setText(String.format("Multa (5%%): R$ %.2f", multa));
        tvJuros.setText(String.format("Juros (%d%% = %d meses × 1%%): R$ %.2f", Math.min(mesesAtraso, 15), Math.min(mesesAtraso, 15), juros));
        tvTotal.setText(String.format("Total a Pagar: R$ %.2f", total));
        layoutResultado.setVisibility(View.VISIBLE);

        IptuRecord record = new IptuRecord(cpf, nome, valorOriginal, mesesAtraso, multa, juros, total);
        executor.execute(() -> {
            database.iptuDao().inserir(record);
        });
    }
    private boolean cpfValido(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", "");
        if (cpf.length() != 11) return false;
        if (cpf.matches("(\\d)\\1{10}")) return false;
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int primeiroDigito = 11 - (soma % 11);
        if (primeiroDigito >= 10) primeiroDigito = 0;
        if (primeiroDigito != Character.getNumericValue(cpf.charAt(9))) return false;
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        int segundoDigito = 11 - (soma % 11);
        if (segundoDigito >= 10) segundoDigito = 0;
        return segundoDigito == Character.getNumericValue(cpf.charAt(10));
    }

    private void voltarParaLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
