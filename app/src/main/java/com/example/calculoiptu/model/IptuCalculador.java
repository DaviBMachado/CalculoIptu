package com.example.calculoiptu.model;

public class IptuCalculador {
    private static final double PERCENTUAL_MULTA = 0.05;
    private static final double PERCENTUAL_JUROS_MES = 0.01;
    private static final int MESES_MAXIMOS_JUROS = 15;

    private final double valorOriginal;
    private final int mesesAtraso;

    public IptuCalculador(double valorOriginal, int mesesAtraso) {
        if (valorOriginal < 0) throw new IllegalArgumentException("Valor original não pode ser negativo.");
        if (mesesAtraso < 0) throw new IllegalArgumentException("Meses em atraso não pode ser negativo.");
        this.valorOriginal = valorOriginal;
        this.mesesAtraso = mesesAtraso;
    }
    public double calcularMulta() {
        if (mesesAtraso == 0) return 0.0;
        return valorOriginal * PERCENTUAL_MULTA;
    }
    public double calcularJuros() {
        if (mesesAtraso == 0) return 0.0;
        int mesesParaCalculo = Math.min(mesesAtraso, MESES_MAXIMOS_JUROS);
        double percentualJuros = PERCENTUAL_JUROS_MES * mesesParaCalculo;
        return valorOriginal * percentualJuros;
    }

    public double calcularTotal() {
        return valorOriginal + calcularMulta() + calcularJuros();
    }

    public double getValorOriginal() {
        return valorOriginal;
    }

    public int getMesesAtraso() {
        return mesesAtraso;
    }
}
