package EstruturaDados;

public class Lista {
    private Ponteiro inicio;
    private Ponteiro fim;

    public int tamanho() { //fazer a contagem do numero de pessoas nos andares
        int count = 0;
        Ponteiro p = getInicio();
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

    public Object getElementoNaPosicao(int posicao) {
        int contador = 0;
        Ponteiro atual = inicio;

        while (atual != null) {
            if (contador == posicao) {
                return atual.getElemento();
            }
            atual = atual.getProximo();
            contador++;
        }

        return null; // se não encontrar
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
