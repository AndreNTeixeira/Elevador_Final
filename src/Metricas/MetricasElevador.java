package Metricas;

import java.io.Serializable;

public class MetricasElevador implements Serializable {                       // Classe responsável por armazenar métricas de desempenho do elevador
    private int energiaTotalGasta;                                           // Quantidade total de energia gasta
    private int tempoTotalMovimentacao;                                      // Tempo total em que o elevador esteve em movimento
    private int tempoTotalParado;                                            // Tempo total que o elevador ficou parado
    private int numeroViagens;                                               // Quantidade de viagens realizadas
    private int numeroPessoasTransportadas;                                  // Total de pessoas transportadas

    public MetricasElevador() {                                              // Construtor inicializa todos os valores com zero
        energiaTotalGasta = 0;
        tempoTotalMovimentacao = 0;
        tempoTotalParado = 0;
        numeroViagens = 0;
        numeroPessoasTransportadas = 0;
    }

    public void adicionarEnergiaGasta(int energia) {                         // Incrementa a energia gasta
        this.energiaTotalGasta += energia;
    }

    public void adicionarTempoMovimentacao(int tempo) {                      // Soma o tempo de movimentação
        this.tempoTotalMovimentacao += tempo;
    }

    public void adicionarTempoParado(int tempo) {                            // Soma o tempo parado
        this.tempoTotalParado += tempo;
    }

    public void incrementarViagens() {                                       // Incrementa o contador de viagens
        this.numeroViagens++;
    }

    public void incrementarPessoasTransportadas() {                          // Incrementa o contador de pessoas transportadas
        this.numeroPessoasTransportadas++;
    }

    public int getEnergiaTotalGasta() {                                      // Getter da energia gasta
        return energiaTotalGasta;
    }

    public void setEnergiaTotalGasta(int energiaTotalGasta) {                // Setter da energia gasta
        this.energiaTotalGasta = energiaTotalGasta;
    }

    public int getTempoTotalMovimentacao() {                                 // Getter do tempo de movimentação
        return tempoTotalMovimentacao;
    }

    public void setTempoTotalMovimentacao(int tempoTotalMovimentacao) {      // Setter do tempo de movimentação
        this.tempoTotalMovimentacao = tempoTotalMovimentacao;
    }

    public int getTempoTotalParado() {                                       // Getter do tempo parado
        return tempoTotalParado;
    }

    public void setTempoTotalParado(int tempoTotalParado) {                  // Setter do tempo parado
        this.tempoTotalParado = tempoTotalParado;
    }

    public int getNumeroViagens() {                                          // Getter do número de viagens
        return numeroViagens;
    }

    public void setNumeroViagens(int numeroViagens) {                        // Setter do número de viagens
        this.numeroViagens = numeroViagens;
    }

    public int getNumeroPessoasTransportadas() {                             // Getter do número de pessoas transportadas
        return numeroPessoasTransportadas;
    }

    public void setNumeroPessoasTransportadas(int numeroPessoasTransportadas) { // Setter do número de pessoas transportadas
        this.numeroPessoasTransportadas = numeroPessoasTransportadas;
    }
}
