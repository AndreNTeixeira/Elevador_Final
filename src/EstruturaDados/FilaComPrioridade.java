package EstruturaDados;

public class FilaComPrioridade {

    private Fila filaVip   = new Fila(); // cadeirante/gestante/idoso
    private Fila filaNormal = new Fila(); // demais pessoas

    public void enfileirar(Object elemento, boolean prioridade) {
        if (prioridade) filaVip.enfileirar(elemento);
        else            filaNormal.enfileirar(elemento);
    }

    public Object desenfileirar() {
        if (!filaVip.estaVazia()) {
            Object e = filaVip.primeiro();
            filaVip.desenfileirar();
            return e;
        }
        if (!filaNormal.estaVazia()) {
            Object e = filaNormal.primeiro();
            filaNormal.desenfileirar();
            return e;
        }
        return null; // ambas vazias
    }

    public boolean estaVazia() {
        return filaVip.estaVazia() && filaNormal.estaVazia();
    }

    public int getTamanhoTotal() {
        return filaVip.getTamanho() + filaNormal.getTamanho();
    }

    public int getTamanhoPrioritarios() {
        return filaVip.getTamanho();
    }
}
