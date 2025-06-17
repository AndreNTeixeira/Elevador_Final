package Base;

import java.io.Serializable;

// Classe que representa uma pessoa na simulação
public class Pessoa implements Serializable {
    private int id;                                         // Identificador único da pessoa
    private boolean prioridade;                             // Não utilizado diretamente (ver isPrioritaria)
    private int andarOrigem;                                // Andar de origem da pessoa
    private int andarDestino;                               // Andar de destino da pessoa
    private boolean dentroElevador;                         // Indica se a pessoa está dentro do elevador
    private int tempoChegada;                               // Minuto em que a pessoa chegou ao sistema
    private int tempoEntradaFila;                           // Minuto em que a pessoa entrou na fila
    private int tempoSaidaFila;                             // Minuto em que a pessoa saiu da fila
    private int tempoEntradaElevador;                       // Minuto em que a pessoa entrou no elevador
    private int tempoSaidaElevador;                         // Minuto em que a pessoa saiu do elevador
    private boolean idoso;                                  // Indica se a pessoa é idosa
    private boolean cadeirante;                             // Indica se a pessoa é cadeirante

    // Construtor da classe Pessoa
    public Pessoa(int id, int origem, int destino, boolean idoso, boolean cadeirante) {
        this.id = id;                                       // Define o ID da pessoa
        this.andarOrigem = origem;                          // Define o andar de origem
        this.andarDestino = destino;                        // Define o andar de destino
        this.idoso = idoso;                                 // Define se é idoso
        this.cadeirante = cadeirante;                       // Define se é cadeirante
        this.dentroElevador = false;                        // Inicialmente, a pessoa não está no elevador
    }

    // Registra o momento em que a pessoa entrou na fila
    public void registrarTempoEntradaFila(int minutoAtual) {
        this.tempoEntradaFila = minutoAtual;
    }

    // Registra o momento em que a pessoa saiu da fila
    public void registrarTempoSaidaFila(int minutoAtual) {
        this.tempoSaidaFila = minutoAtual;
    }

    // Registra o momento em que a pessoa entrou no elevador
    public void registrarTempoEntradaElevador(int minutoAtual) {
        this.tempoEntradaElevador = minutoAtual;
    }

    // Registra o momento em que a pessoa saiu do elevador
    public void registrarTempoSaidaElevador(int minutoAtual) {
        this.tempoSaidaElevador = minutoAtual;
    }

    // Verifica se a pessoa é idosa
    public boolean isIdoso() {
        return idoso;
    }

    // Verifica se a pessoa é cadeirante
    public boolean isCadeirante() {
        return cadeirante;
    }

    // Verifica se a pessoa tem prioridade (é idosa ou cadeirante)
    public boolean isPrioritaria() {
        return idoso || cadeirante;
    }

    // Retorna o tempo que a pessoa esperou na fila
    public int getTempoEntradaFila() {
        return tempoSaidaFila - tempoEntradaFila;
    }

    // Retorna o tempo que a pessoa permaneceu dentro do elevador
    public int getTempoNoElevador() {
        return tempoSaidaElevador - tempoEntradaElevador;
    }

    // Retorna o tempo total que a pessoa passou no sistema
    public int getTempoTotalSistema() {
        return tempoSaidaElevador - tempoEntradaFila;
    }

    // Setters e getters dos tempos (caso precise manipular diretamente)
    public void setTempoEntradaFila(int tempoEntradaFila) {
        this.tempoEntradaFila = tempoEntradaFila;
    }

    public int getTempoSaidaFila() {
        return tempoSaidaFila;
    }

    public void setTempoSaidaFila(int tempoSaidaFila) {
        this.tempoSaidaFila = tempoSaidaFila;
    }

    public int getTempoEntradaElevador() {
        return tempoEntradaElevador;
    }

    public void setTempoEntradaElevador(int tempoEntradaElevador) {
        this.tempoEntradaElevador = tempoEntradaElevador;
    }

    public int getTempoSaidaElevador() {
        return tempoSaidaElevador;
    }

    public void setTempoSaidaElevador(int tempoSaidaElevador) {
        this.tempoSaidaElevador = tempoSaidaElevador;
    }

    // Getter do ID da pessoa
    public int getId() {
        return id;
    }

    // Getter e setter do andar de origem
    public int getAndarOrigem() {
        return andarOrigem;
    }

    public void setAndarOrigem(int andarOrigem) {
        this.andarOrigem = andarOrigem;
    }

    // Getter e setter do andar de destino
    public void setAndarDestino(int andarDestino) {
        this.andarDestino = andarDestino;
    }

    public int getAndarDestino() {
        return andarDestino;
    }

    // Verifica se a pessoa está no elevador
    public boolean estaDentroDoElevador() {
        return dentroElevador;
    }

    // Marca a pessoa como tendo entrado no elevador
    public void entrarElevador() {
        this.dentroElevador = true;
    }

    // Marca a pessoa como tendo saído do elevador
    public void sairElevador() {
        this.dentroElevador = false;
    }

    // Getter e setter do tempo de chegada ao sistema
    public int getTempoChegada() {
        return tempoChegada;
    }

    public void setTempoChegada(int tempoChegada) {
        this.tempoChegada = tempoChegada;
    }

    // Registra o tempo de chegada com base no minuto atual
    public void registrarTempoChegada(int minutoAtual) {
        this.tempoChegada = minutoAtual;
    }
}
