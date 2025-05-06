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

    public Object desenfileirar() {
        if (inicio == null) return null;
        Object elemento = inicio.getElemento();
        inicio = inicio.getProximo();
        if (inicio == null) fim = null;
        return elemento;
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
}
