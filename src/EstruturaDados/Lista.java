package EstruturaDados;

import Simulacao.CentralDeControle;

public class Lista {                                       // Implementação de uma lista encadeada simples
    private Ponteiro inicio;                               // Ponteiro para o primeiro elemento da lista
    private Ponteiro fim;                                  // Ponteiro para o último elemento da lista

    public int tamanho() {                                 // Retorna o número de elementos na lista
        int count = 0;
        Ponteiro p = getInicio();
        while (p != null) {
            count++;                                       // Incrementa o contador para cada nó encontrado
            p = p.getProximo();                            // Avança para o próximo nó
        }
        return count;                                      // Retorna o total de elementos
    }

    public Lista() {                                       // Construtor: inicializa a lista vazia
        inicio = null;
        fim = null;
    }

    public void inserirFim(Object elemento) {              // Insere um novo elemento no final da lista
        Ponteiro novo = new Ponteiro(elemento);            // Cria novo nó com o elemento fornecido
        if (inicio == null) {                              // Caso a lista esteja vazia
            inicio = novo;                                 // Novo elemento se torna o início
        } else {
            fim.setProximo(novo);                          // Liga o antigo fim ao novo nó
        }
        fim = novo;                                        // Atualiza o ponteiro de fim para o novo nó
    }

    public Object getElementoNaPosicao(int posicao) {      // Retorna o elemento na posição indicada (base 0)
        int contador = 0;
        Ponteiro atual = inicio;

        while (atual != null) {
            if (contador == posicao) {                     // Quando a posição for encontrada
                return atual.getElemento();                // Retorna o elemento do nó
            }
            atual = atual.getProximo();                    // Vai para o próximo nó
            contador++;                                    // Incrementa o contador de posição
        }

        return null;                                       // Retorna null se a posição não existir na lista
    }

    public Ponteiro getInicio() {                          // Getter para o primeiro nó da lista
        return inicio;
    }

    public void setInicio(Ponteiro novoInicio) {           // Setter para redefinir o início da lista
        this.inicio = novoInicio;
    }

    public void setFim(Ponteiro novoFim) {                 // Setter para redefinir o fim da lista
        this.fim = novoFim;
    }

    public void setCentral(CentralDeControle centralDeControle) {
    }
}
