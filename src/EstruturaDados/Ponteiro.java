package EstruturaDados;

public class Ponteiro {                                // Classe que representa um nó (ponteiro) de uma lista encadeada
    private Object elemento;                           // Elemento armazenado no nó
    private Ponteiro proximo;                          // Referência para o próximo nó na lista

    public Ponteiro(Object elemento) {                 // Construtor que inicializa o elemento e define o próximo como null
        this.elemento = elemento;
        this.proximo = null;
    }

    public Object getElemento() {                      // Retorna o elemento armazenado
        return elemento;
    }

    public Ponteiro getProximo() {                     // Retorna a referência para o próximo ponteiro
        return proximo;
    }

    public void setProximo(Ponteiro proximo) {         // Define o próximo ponteiro (liga o próximo nó)
        this.proximo = proximo;
    }

    public void setElemento(int i) {
    }
}
