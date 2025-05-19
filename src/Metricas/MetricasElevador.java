package Metricas;

import java.io.Serializable;

public class MetricasElevador implements Serializable {
    private int energiaTotalGasta;
    private int tempoTotalMovimentacao;
    private int tempoTotalParado;
    private int numeroViagens;
    private int numeroPessoasTransportadas;

    public MetricasElevador() {
        energiaTotalGasta = 0;
        tempoTotalMovimentacao = 0;
        tempoTotalParado = 0;
        numeroViagens = 0;
        numeroPessoasTransportadas = 0;
    }
    public void adicionarEnergiaGasta(int energia) {
        this.energiaTotalGasta += energia;
    }
    public void adicionarTempoMovimentacao(int tempo){
        this.tempoTotalMovimentacao += tempo;
    }
    public void adicionarTempoParado(int tempo){
        this.tempoTotalParado += tempo;
    }
    public void incrementarViagens(){
        this.numeroViagens++;
    }
    public void incrementarPessoasTransportadas(){
        this.numeroPessoasTransportadas++;
    }

    public int getEnergiaTotalGasta() {
        return energiaTotalGasta;
    }

    public void setEnergiaTotalGasta(int energiaTotalGasta) {
        this.energiaTotalGasta = energiaTotalGasta;
    }

    public int getTempoTotalMovimentacao() {
        return tempoTotalMovimentacao;
    }

    public void setTempoTotalMovimentacao(int tempoTotalMovimentacao) {
        this.tempoTotalMovimentacao = tempoTotalMovimentacao;
    }

    public int getTempoTotalParado() {
        return tempoTotalParado;
    }

    public void setTempoTotalParado(int tempoTotalParado) {
        this.tempoTotalParado = tempoTotalParado;
    }

    public int getNumeroViagens() {
        return numeroViagens;
    }

    public void setNumeroViagens(int numeroViagens) {
        this.numeroViagens = numeroViagens;
    }

    public int getNumeroPessoasTransportadas() {
        return numeroPessoasTransportadas;
    }

    public void setNumeroPessoasTransportadas(int numeroPessoasTransportadas) {
        this.numeroPessoasTransportadas = numeroPessoasTransportadas;
    }
}
