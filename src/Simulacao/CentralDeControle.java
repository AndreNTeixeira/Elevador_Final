package Simulacao;

import Base.Andar;
import Base.Elevador;
import Base.Pessoa;
import EstruturaDados.Fila;
import EstruturaDados.Lista;
import EstruturaDados.Ponteiro;
import Metricas.MetricasElevador;
import controle.HeuristicaControle;

public class CentralDeControle extends EntidadeSimulavel {
    private Lista elevadores;
    private Lista andares;
    private int andarMaximo;
    private HeuristicaControle heuristica;


    public CentralDeControle(int quantidadeElevadores, int andarMaximo, HeuristicaControle heur) {
        this.andarMaximo = andarMaximo;
        this.heuristica = heur;
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
        heuristica.decidir(elevadores, andares, minutoSimulado);
        Ponteiro pElevador = elevadores.getInicio();

        while (pElevador != null) {
            Elevador elevador = (Elevador) pElevador.getElemento();

            processarTarefaElevador(elevador, minutoSimulado);

            elevador.atualizar(minutoSimulado);

            pElevador = pElevador.getProximo();
        }
        if (minutoSimulado % 60 == 0) {
            exibirMetricas(minutoSimulado);
        }
    }

    private void exibirMetricas(int minutoSimulado) {
        System.out.println("\n=== METRICAS DOS ELEVADORES (Minuto" + minutoSimulado + ") ===");

        Ponteiro pElevador = elevadores.getInicio();
        while (pElevador != null) {
            Elevador elevador = (Elevador) pElevador.getElemento();
            MetricasElevador metricas = elevador.getMetricas();

            System.out.println("Elevador " + elevador.getId() + ":");
            System.out.println("  - Energia total gasta: " + metricas.getEnergiaTotalGasta() + " unidades");
            System.out.println("  - Tempo em movimento: " + metricas.getTempoTotalMovimentacao() + " minutos");
            System.out.println("  - Tempo parado: " + metricas.getTempoTotalParado() + " minutos");
            System.out.println("  - Número de viagens: " + metricas.getNumeroViagens());
            System.out.println("  - Pessoas transportadas: " + metricas.getNumeroPessoasTransportadas());

            pElevador = pElevador.getProximo();
        }
        System.out.println("============================\n");
    }

    private void processarTarefaElevador(Elevador elevador, int minutoAtual) {
        int andarAtual = elevador.getAndarAtual();
        Andar andar = (Andar) getAndar(andarAtual);

        if (andarAtual == 0) {
            andar.embarcarPessoas(elevador, minutoAtual);
        } else {
            Fila filaAguardando = andar.getPessoasAguardando();
            Ponteiro p = filaAguardando.getInicio();
            while (p != null && elevador.getCapacidadeDisponivel() > 0) {
                Pessoa pessoa = (Pessoa) p.getElemento();
                if (!pessoa.estaDentroDoElevador()
                        && pessoa.getAndarOrigem() == andarAtual) {   // ← verificação adicionada
                    elevador.adicionarPassageiro(pessoa, minutoAtual);
                    filaAguardando.desenfileirar();
                    System.out.println("Pessoa " + pessoa.getId() +
                            " embarcou no andar " + andarAtual +
                            " para voltar ao térreo");
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
