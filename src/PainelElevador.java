import java.io.Serializable;

public class PainelElevador implements Serializable {
    private Lista destinosSolicitados = new Lista();

    public void registrarChamada(int destino) {
        destinosSolicitados.inserirFim(destino);
    }

    public void limparChamadas() {
        destinosSolicitados = new Lista();
    }

    public Lista getDestinosSolicitados() {
        return destinosSolicitados;
    }
}