public class Predio extends EntidadeSimulavel {
    private CentralDeControle central;
    private Lista andares;
    private int proximoIdPessoa = 1;

    public Predio(int quantidadeAndares, int quantidadeElevadores) {
        andares = new Lista();
        central = new CentralDeControle(quantidadeElevadores, quantidadeAndares);

        for (int i = 0; i <= quantidadeAndares; i++) {
            andares.inserirFim(new Andar(i));
        }

        central.setAndares(andares); // conecta os andares à central
    }

    @Override
    public void atualizar(int minutoSimulado) {
        Ponteiro p = andares.getInicio();
        while (p != null) {
            Andar a = (Andar) p.getElemento();
            a.processarRetornoParaTerreo(minutoSimulado);
            p = p.getProximo();
        }

        central.atualizar(minutoSimulado);
    }

    public CentralDeControle getCentral() {
        return central;
    }

    public Lista getAndares() {
        return andares;
    }

    public Andar getAndar(int numero) {
        Ponteiro p = andares.getInicio();
        while (p != null) {
            Andar a = (Andar) p.getElemento();
            if (a.getNumero() == numero) return a;
            p = p.getProximo();
        }
        return null;
    }
}
