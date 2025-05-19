package Base;

import java.io.Serializable;

public class Pessoa implements Serializable {
    private int id;
    private boolean prioridade;
    private int andarOrigem;
    private int andarDestino;
    private boolean dentroElevador;
    private int tempoChegada;
    private int tempoEntradaFila;
    private int tempoSaidaFila;
    private int tempoEntradaElevador;
    private int tempoSaidaElevador;


    public Pessoa(int id, int origem, int destino) {
        this.id = id;
        this.andarOrigem = origem;
        this.andarDestino = destino;
        this.dentroElevador = false;
    }
    public void registrarTempoEntradaFila(int minutoAtual){
        this.tempoEntradaFila = minutoAtual;
    }
    public void registrarTempoSaidaFila(int minutoAtual){
        this.tempoSaidaFila = minutoAtual;
    }
    public void registrarTempoEntradaElevador(int minutoAtual){
        this.tempoEntradaElevador = minutoAtual;
    }
    public void registrarTempoSaidaElevador(int minutoAtual){
        this.tempoSaidaElevador = minutoAtual;
    }
    public int getTempoEntradaFila() {
        return tempoSaidaFila - tempoEntradaFila;
    }
    public int getTempoNoElevador(){
        return tempoSaidaElevador - tempoEntradaElevador;
    }
    public int getTempoTotalSistema(){
        return tempoSaidaElevador - tempoEntradaFila;
    }

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

    public int getId() {
        return id;
    }

    public int getAndarOrigem() {
        return andarOrigem;
    }
    
    public void setAndarOrigem(int andarOrigem) {
        this.andarOrigem = andarOrigem;
    }

    public void setAndarDestino(int andarDestino) {
        this.andarDestino = andarDestino;
    }

    public int getAndarDestino() {
        return andarDestino;
    }

    public boolean estaDentroDoElevador() {
        return dentroElevador;
    }

    public void entrarElevador() {
        this.dentroElevador = true;
    }

    public void sairElevador() {
        this.dentroElevador = false;
    }

    public int getTempoChegada() {
        return tempoChegada;
    }

    public void setTempoChegada(int tempoChegada) {
        this.tempoChegada = tempoChegada;
    }

    public void registrarTempoChegada(int minutoAtual) {
        this.tempoChegada = minutoAtual;
    }
}