public class Lista {
    private Ponteiro inicio;
    private Ponteiro fim;

    private int contarLista(Lista lista) { //fazer a contagem do numero de pessoas nos andares
        int count = 0;
        Ponteiro p = lista.getInicio();
        while (p != null) {
            count++;
            p = p.getProximo();
        }
        return count;
    }

    public Lista() {
        inicio = null;
        fim = null;
    }

    public void inserirFim(Object elemento) {
        Ponteiro novo = new Ponteiro(elemento);
        if (inicio == null) {
            inicio = novo;
        } else {
            fim.setProximo(novo);
        }
        fim = novo;
    }

    public Ponteiro getInicio() {
        return inicio;
    }

    public void setInicio(Ponteiro novoInicio) {
        this.inicio = novoInicio;
    }

    public void setFim(Ponteiro novoFim) {
        this.fim = novoFim;
    }
}
