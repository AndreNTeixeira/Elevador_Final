package Simulacao;

import Base.Andar;
import Base.Elevador;
import Base.Pessoa;
import EstruturaDados.Fila;
import EstruturaDados.Lista;
import EstruturaDados.Ponteiro;
import Metricas.MetricasElevador;
import controle.HeuristicaControle;

public class CentralDeControle extends EntidadeSimulavel {                       // Classe que coordena o funcionamento dos elevadores
    private Lista elevadores;                                                    // Lista de elevadores gerenciados
    private Lista andares;                                                       // Lista de andares do prédio
    private int andarMaximo;                                                     // Número total de andares (máximo)
    private HeuristicaControle heuristica;                                       // Estratégia de decisão usada para controlar os elevadores

    public CentralDeControle(int quantidadeElevadores, int andarMaximo, HeuristicaControle heur) {
        this.andarMaximo = andarMaximo;                                          // Define o andar máximo
        this.heuristica = heur;                                                  // Define a heurística usada
        elevadores = new Lista();                                                // Inicializa a lista de elevadores
        for (int i = 0; i < quantidadeElevadores; i++) {
            Elevador elevador = new Elevador(i + 1, andarMaximo);
            elevador.setCentral(this);
            elevadores.inserirFim(elevador);
        }
    }

    public void setAndares(Lista andares) {
        this.andares = andares;                                                  // Define a lista de andares do prédio
    }

    @Override
    public void atualizar(int minutoSimulado) {
        heuristica.decidir(elevadores, andares, minutoSimulado);                // Aplica a heurística para decidir as ações dos elevadores
        Ponteiro pElevador = elevadores.getInicio();                            // Ponteiro para iterar os elevadores

        while (pElevador != null) {
            Elevador elevador = (Elevador) pElevador.getElemento();             // Recupera o elevador da lista

            processarTarefaElevador(elevador, minutoSimulado);                  // Trata embarque de pessoas no elevador

            elevador.atualizar(minutoSimulado);                                 // Atualiza o estado do elevador

            pElevador = pElevador.getProximo();                                 // Vai para o próximo elevador
        }

        if (minutoSimulado % 60 == 0) {                                          // A cada 60 minutos, exibe métricas
            exibirMetricas(minutoSimulado);
        }
    }

    private void exibirMetricas(int minutoSimulado) {
        System.out.println("\n=== METRICAS DOS ELEVADORES (Minuto" + minutoSimulado + ") ===");

        Ponteiro pElevador = elevadores.getInicio();                            // Itera sobre todos os elevadores
        while (pElevador != null) {
            Elevador elevador = (Elevador) pElevador.getElemento();             // Obtém o elevador
            MetricasElevador metricas = elevador.getMetricas();                 // Obtém as métricas do elevador

            // Exibe as métricas
            System.out.println("Elevador " + elevador.getId() + ":");
            System.out.println("  - Energia total gasta: " + metricas.getEnergiaTotalGasta() + " unidades");
            System.out.println("  - Tempo em movimento: " + metricas.getTempoTotalMovimentacao() + " minutos");
            System.out.println("  - Tempo parado: " + metricas.getTempoTotalParado() + " minutos");
            System.out.println("  - Número de viagens: " + metricas.getNumeroViagens());
            System.out.println("  - Pessoas transportadas: " + metricas.getNumeroPessoasTransportadas());

            pElevador = pElevador.getProximo();                                 // Vai para o próximo elevador
        }

        System.out.println("============================\n");
    }

    private void processarTarefaElevador(Elevador elevador, int minutoAtual) {
        int andarAtual = elevador.getAndarAtual();                              // Pega o andar atual do elevador
        Andar andar = (Andar) getAndar(andarAtual);                             // Recupera o objeto Andar correspondente

        if (andarAtual == 0) {
            andar.embarcarPessoas(elevador, minutoAtual);                       // No térreo: embarca pessoas que vão subir
        } else {
            Fila filaAguardando = andar.getPessoasAguardando();                // Em outros andares: fila de pessoas que querem descer
            Ponteiro p = filaAguardando.getInicio();                           // Ponteiro para iterar a fila

            while (p != null && elevador.getCapacidadeDisponivel() > 0) {
                Pessoa pessoa = (Pessoa) p.getElemento();                       // Obtém a pessoa da fila

                if (!pessoa.estaDentroDoElevador()                             // Se a pessoa ainda não entrou
                        && pessoa.getAndarOrigem() == andarAtual) {           // E está no andar certo
                    elevador.adicionarPassageiro(pessoa, minutoAtual);         // Adiciona a pessoa ao elevador
                    filaAguardando.desenfileirar();                             // Remove da fila
                    System.out.println("Pessoa " + pessoa.getId() +
                            " embarcou no andar " + andarAtual +
                            " para voltar ao térreo");
                }

                p = p.getProximo();                                             // Vai para a próxima pessoa na fila
            }
        }
    }

    public Andar getAndar(int numero) {
        Ponteiro p = andares.getInicio();                                      // Itera sobre a lista de andares
        while (p != null) {
            Andar a = (Andar) p.getElemento();                                 // Recupera o objeto Andar
            if (a.getNumero() == numero) return a;                             // Retorna se for o andar procurado
            p = p.getProximo();                                                // Continua para o próximo
        }
        return null;                                                           // Retorna null se não encontrar
    }

    public Lista getElevadores() {
        return elevadores;                                                     // Getter da lista de elevadores
    }
}
