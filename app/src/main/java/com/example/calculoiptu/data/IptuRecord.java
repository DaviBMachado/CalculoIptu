package com.example.calculoiptu.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "iptu_records")
public class IptuRecord {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String cpf;
    public String nome;
    public double valorOriginal;
    public int mesesAtraso;
    public double valorMulta;
    public double valorJuros;
    public double valorTotal;

    public IptuRecord(String cpf, String nome, double valorOriginal, int mesesAtraso,
                      double valorMulta, double valorJuros, double valorTotal) {
        this.cpf = cpf;
        this.nome = nome;
        this.valorOriginal = valorOriginal;
        this.mesesAtraso = mesesAtraso;
        this.valorMulta = valorMulta;
        this.valorJuros = valorJuros;
        this.valorTotal = valorTotal;
    }
}
