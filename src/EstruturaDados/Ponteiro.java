package EstruturaDados;

public class Ponteiro {
    private Object elemento;
    private Ponteiro proximo;

    public Ponteiro(Object elemento) {
        this.elemento = elemento;
        this.proximo = null;
    }

    public Object getElemento() {
        return elemento;
    }

    public Ponteiro getProximo() {
        return proximo;
    }

    public void setProximo(Ponteiro proximo) {
        this.proximo = proximo;
    }
}
