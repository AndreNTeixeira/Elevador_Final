public class CentralDeControle extends EntidadeSimulavel {
    private Lista elevadores;
    private Lista andares;
    private int andarMaximo;


    public CentralDeControle(int quantidadeElevadores, int andarMaximo) {
        this.andarMaximo = andarMaximo;
        elevadores = new Lista();
        for (int i = 0; i < quantidadeElevadores; i++) {
            elevadores.inserirFim(new Elevador(i + 1, andarMaximo));
        }
    }

    public void setAndares(Lista andares) {
        this.andares = andares;
    }

    @Override
    public void atualizar(int minutoSimulado) {
        Ponteiro pElevador = elevadores.getInicio();

        while (pElevador != null) {
            Elevador elevador = (Elevador) pElevador.getElemento();

            processarTarefaElevador(elevador);

            elevador.atualizar(minutoSimulado);

            pElevador = pElevador.getProximo();
        }
    }

    private void processarTarefaElevador(Elevador elevador) {
        int andarAtual = elevador.getAndarAtual();
        Andar andar = (Andar) getAndar(andarAtual);

        if (andarAtual == 0) {
            andar.embarcarPessoas(elevador);
        }

        else {
            Fila filaAguardando = andar.getPessoasAguardando();
            Ponteiro p = filaAguardando.getInicio();
            while (p != null && elevador.getCapacidadeDisponivel() > 0) {
                Pessoa pessoa = (Pessoa) p.getElemento();
                if (!pessoa.estaDentroDoElevador()) {
                    elevador.adicionarPassageiro(pessoa);
                    filaAguardando.desenfileirar();
                    System.out.println("Pessoa " + pessoa.getId() + " embarcou no andar " + andarAtual + " para voltar ao térreo");
                }
                p = p.getProximo();
            }
        }
    }

    private Andar getAndar(int numero) {
        Ponteiro p = andares.getInicio();
        while (p != null) {
            Andar a = (Andar) p.getElemento();
            if (a.getNumero() == numero) return a;
            p = p.getProximo();
        }
        return null;
    }

    public Lista getElevadores() {
        return elevadores;
    }
}
