package EstruturaDados;

public class Fila {
    private Ponteiro inicio;
    private Ponteiro fim;

    public Fila() {
        inicio = null;
        fim = null;
    }

    public void enfileirar(Object elemento) {
        Ponteiro novo = new Ponteiro(elemento);
        if (inicio == null) {
            inicio = novo;
        } else {
            fim.setProximo(novo);
        }
        fim = novo;
    }

    public void enfileirarPrioritario(Object elemento) {
        Ponteiro novo = new Ponteiro(elemento);
        if (inicio == null) {             // fila vazia
            inicio = fim = novo;
        } else {                          // insere na frente
            novo.setProximo(inicio);
            inicio = novo;
        }
    }

    public void desenfileirar() {
        if (inicio == null) {
            return; // fila já vazia
        }

        if (inicio == fim) { //1 elevemnto
            inicio = null;
            fim = null;
        } else {
            Ponteiro atual = inicio; //retira o primeiro da fila FIFO
            inicio = inicio.getProximo();
        }
    }

    public boolean estaVazia() {
        return inicio == null;
    }

    public Object primeiro() {
        return (inicio != null) ? inicio.getElemento() : null;
    }

    public Ponteiro getInicio() {
        return inicio;
    }

    public int getTamanho() {
        int count = 0;
        Ponteiro p = inicio;
        while (p != null) {
            count++;
            p = p.getProximo();
        }
        return count;
    }

    public void setInicio(Ponteiro proximo) {
    }

    public void setFim(Ponteiro anterior) {

    }
}
