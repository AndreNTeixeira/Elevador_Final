import java.io.Serializable;

public class Pessoa implements Serializable {
    private int id;
    private int andarOrigem;
    private int andarDestino;
    private boolean dentroElevador;
    private int tempoChegada;


    public Pessoa(int id, int origem, int destino) {
        this.id = id;
        this.andarOrigem = origem;
        this.andarDestino = destino;
        this.dentroElevador = false;
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