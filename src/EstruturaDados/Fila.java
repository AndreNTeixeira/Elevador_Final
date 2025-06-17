package EstruturaDados;

public class Fila {
    private Ponteiro inicio;                          // Ponteiro para o primeiro elemento da fila
    private Ponteiro fim;                             // Ponteiro para o último elemento da fila

    public Fila() {                                   // Construtor: inicializa fila vazia
        inicio = null;
        fim = null;
    }

    public void enfileirar(Object elemento) {         // Adiciona um elemento ao final da fila (comum)
        Ponteiro novo = new Ponteiro(elemento);       // Cria novo nó com o elemento
        if (inicio == null) {                         // Se a fila estiver vazia
            inicio = novo;                            // Novo nó é o início
        } else {
            fim.setProximo(novo);                     // Liga o antigo fim ao novo nó
        }
        fim = novo;                                   // Atualiza o fim
    }

    public void enfileirarPrioritario(Object elemento) { // Adiciona elemento no início da fila (casos prioritários)
        Ponteiro novo = new Ponteiro(elemento);       // Cria novo nó
        if (inicio == null) {                         // Se a fila estiver vazia
            inicio = fim = novo;                      // Novo nó é início e fim
        } else {                                      // Se já há elementos
            novo.setProximo(inicio);                  // Novo nó aponta para o atual início
            inicio = novo;                            // Atualiza o início
        }
    }

    public void desenfileirar() {                     // Remove o primeiro elemento da fila
        if (inicio == null) {                         // Se fila vazia, nada a fazer
            return;
        }

        if (inicio == fim) {                          // Se há apenas um elemento
            inicio = null;
            fim = null;
        } else {
            Ponteiro atual = inicio;                  // Avança o início para o próximo (descarta o primeiro)
            inicio = inicio.getProximo();
        }
    }

    public boolean estaVazia() {                      // Verifica se a fila está vazia
        return inicio == null;
    }

    public Object primeiro() {                        // Retorna o primeiro elemento (sem remover)
        return (inicio != null) ? inicio.getElemento() : null;
    }

    public Ponteiro getInicio() {                     // Retorna o ponteiro de início da fila
        return inicio;
    }

    public int getTamanho() {                         // Retorna o tamanho atual da fila
        int count = 0;
        Ponteiro p = inicio;
        while (p != null) {
            count++;                                  // Conta nós enquanto percorre
            p = p.getProximo();
        }
        return count;
    }

    public void setInicio(Ponteiro proximo) {
    }

    public void setFim(Ponteiro anterior) {
    }
}
