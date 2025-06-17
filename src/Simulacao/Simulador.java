package Simulacao;

import Base.Andar;
import Base.Pessoa;
import Base.Predio;
import EstruturaDados.Ponteiro;
import controle.HeuristicaControle;

import java.io.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Simulador implements Serializable { // Classe principal da simulação, permite serialização do estado
    private int minutoSimulado; // Contador de tempo simulado em minutos
    private int velocidadeEmMs; // Velocidade da simulação (1 minuto simulado a cada X ms reais)
    private transient Timer timer; // Timer de execução (não serializável)
    private boolean emExecucao; // Status da simulação
    private Predio predio; // Instância do prédio com andares e elevadores
    private int totalPessoas; // Número total de pessoas que devem ser geradas
    private int pessoasRestantes; // Quantidade restante de pessoas para gerar
    private int tempoTotalSimulacao = 1440; // Duração total da simulação (1440 min = 24h)
    private int idPessoaAtual = 1; // ID incremental para novas pessoas
    private double acumuladorPessoa = 0; // Acumula frações para geração contínua de pessoas

    public void configurar(int andares, int elevadores, int pessoas, int velocidadeEmMs, HeuristicaControle heuristica) {
        this.velocidadeEmMs = velocidadeEmMs; // Define a velocidade da simulação
        this.totalPessoas = pessoas; // Define o total de pessoas a gerar
        this.pessoasRestantes = pessoas; // Inicializa a contagem de pessoas restantes
        this.predio = new Predio(andares, elevadores, heuristica); // Cria o prédio com a heurística
        System.out.println("Heurística selecionada: " + heuristica.getClass().getSimpleName());
    }

    public void iniciarSimulacao() { // Inicia a simulação
        this.minutoSimulado = 0;
        this.emExecucao = true;
        iniciarTimer(); // Inicia o timer que atualiza a simulação a cada intervalo
    }

    private void iniciarTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (minutoSimulado >= tempoTotalSimulacao) {
                    encerrar(); // Encerra se chegou ao tempo limite
                    return;
                }
                predio.atualizar(minutoSimulado); // Atualiza estado do prédio
                gerarPessoas(minutoSimulado); // Gera novas pessoas
                minutoSimulado++; // Avança o tempo
            }
        }, 0, velocidadeEmMs); // Executa a cada intervalo definido
    }

    public void pausar() { // Pausa a simulação
        if (timer != null) {
            timer.cancel();
            emExecucao = false;
            System.out.println("Simulação pausada.");
        }
    }

    public void continuar() { // Continua a simulação se estiver pausada
        if (!emExecucao) {
            iniciarTimer();
            emExecucao = true;
            System.out.println("Simulação retomada.");
        }
    }

    public void encerrar() { // Encerra a simulação e imprime relatório final
        if (timer != null) timer.cancel();
        emExecucao = false;
        System.out.println("Simulação encerrada.");

        System.out.println("\n========= RELATÓRIO FINAL =========");

        int somaTempoTotal = 0;
        int somaEnergiaTotal = 0;
        int somaViagensTotal = 0;

        Ponteiro pElev = predio.getCentral().getElevadores().getInicio(); // Itera por todos os elevadores
        while (pElev != null) {
            Base.Elevador e = (Base.Elevador) pElev.getElemento();
            Metricas.MetricasElevador m = e.getMetricas();

            int tempoMov = m.getTempoTotalMovimentacao();
            int tempoParado = m.getTempoTotalParado();
            int tempoTotal = tempoMov + tempoParado;
            int energia = m.getEnergiaTotalGasta();
            int viagens = m.getNumeroViagens();

            somaTempoTotal += tempoTotal;
            somaEnergiaTotal += energia;
            somaViagensTotal += viagens;

            System.out.printf(
                    "Elevador %d | Viagens: %d | Pessoas: %d | Energia: %d | " +
                            "Movimentação: %d min | Parado: %d min%n",
                    e.getId(),
                    m.getNumeroViagens(),
                    m.getNumeroPessoasTransportadas(),
                    m.getEnergiaTotalGasta(),
                    m.getTempoTotalMovimentacao(),
                    m.getTempoTotalParado()
            );
            pElev = pElev.getProximo(); // Próximo elevador
        }

        System.out.println("===================================\n");
        System.out.println("===================================");
        System.out.printf("Tempo total (todos os elevadores): %d min%n", somaTempoTotal);
        System.out.printf("Energia total (todos os elevadores): %d%n", somaEnergiaTotal);
        System.out.printf("Total de viagens (todos os elevadores): %d%n", somaViagensTotal);
        System.out.println("===================================\n");
    }

    private void gerarPessoas(int minutoAtual) { // Geração dinâmica de pessoas

        if (pessoasRestantes <= 0) return; // Não gera mais se acabou

        double taxaBase = (double) totalPessoas / tempoTotalSimulacao; // Distribuição uniforme
        double taxa = taxaBase;

        // Aumenta a taxa em horários de pico
        if (minutoAtual >= 420 && minutoAtual <= 540) taxa *= 1.6; // pico manhã (7h–9h)
        if (minutoAtual >= 1020 && minutoAtual <= 1140) taxa += 1.5; // pico tarde (17h–19h)

        acumuladorPessoa += taxa; // Acumula frações para gerar pessoa inteira

        int pessoasAGerar = (int) acumuladorPessoa;
        acumuladorPessoa -= pessoasAGerar;

        Random random = new Random();
        int quantidadeAndares = getQuantidadeAndares();

        for (int i = 0; i < pessoasAGerar && pessoasRestantes > 0; i++) {
            int destino = random.nextInt(quantidadeAndares - 1) + 1; // Gera destino entre 1 e N-1
            boolean idoso = random.nextInt(100) < 15; // 15% chance de ser idoso
            boolean cadeirante = !idoso && random.nextInt(100) < 3; // 3% cadeirante se não for idoso
            Pessoa p = new Pessoa(idPessoaAtual++, 0, destino, idoso, cadeirante); // Cria pessoa
            getAndar(0).adicionarPessoa(p, minutoAtual); // Adiciona no térreo
            pessoasRestantes--;

            System.out.println("Pessoa " + p.getId() + " gerada para o andar " + destino + " no minuto " + minutoAtual);
        }
    }

    // Método auxiliar para acessar um andar específico
    private Andar getAndar(int numero) {
        Ponteiro p = predio.getAndares().getInicio();
        while (p != null) {
            Andar a = (Andar) p.getElemento();
            if (a.getNumero() == numero) return a;
            p = p.getProximo();
        }
        return null;
    }

    // Conta quantos andares o prédio tem
    private int getQuantidadeAndares() {
        int count = 0;
        Ponteiro p = predio.getAndares().getInicio();
        while (p != null) {
            count++;
            p = p.getProximo();
        }
        return count;
    }

    // Salva o estado atual da simulação em arquivo
    public void gravar(String nomeArquivo) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(nomeArquivo))) {
            out.writeObject(this);
            System.out.println("Simulação gravada em: " + nomeArquivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Carrega uma simulação salva anteriormente
    public static Simulador carregar(String nomeArquivo) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(nomeArquivo))) {
            Simulador sim = (Simulador) in.readObject();
            sim.continuar(); // Retoma a simulação após carregar
            return sim;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Getter para o prédio
    public Predio getPredio() {
        return predio;
    }

    // Getter para o tempo atual da simulação
    public int getMinutoAtual() {
        return minutoSimulado;
    }
}
