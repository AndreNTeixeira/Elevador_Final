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

public class Simulador implements Serializable {
    private int minutoSimulado;
    private int velocidadeEmMs;
    private transient Timer timer;
    private boolean emExecucao;
    private Predio predio;
    private int totalPessoas;
    private int pessoasRestantes;
    private int tempoTotalSimulacao =1440;
    private int idPessoaAtual=1;
    private double acumuladorPessoa = 0;


    public void configurar(int andares, int elevadores, int pessoas, int velocidadeEmMs, HeuristicaControle heuristica) {
        this.velocidadeEmMs = velocidadeEmMs;
        this.totalPessoas = pessoas;
        this.pessoasRestantes = pessoas;
        this.predio = new Predio(andares, elevadores, heuristica);
        System.out.println("Heurística selecionada: " + heuristica.getClass().getSimpleName());
    }

    public void iniciarSimulacao() {
        this.minutoSimulado = 0;
        this.emExecucao = true;
        iniciarTimer();
    }


    private void iniciarTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (minutoSimulado>= tempoTotalSimulacao) {
                    encerrar();
                    return;
                }
                predio.atualizar(minutoSimulado);
                gerarPessoas(minutoSimulado);
                minutoSimulado++;
            }
        }, 0, velocidadeEmMs);
    }

    public void pausar() {
        if (timer != null) {
            timer.cancel();
            emExecucao = false;
            System.out.println("Simulação pausada.");
        }
    }

    public void continuar() {
        if (!emExecucao) {
            iniciarTimer();
            emExecucao = true;
            System.out.println("Simulação retomada.");
        }
    }

    public void encerrar() {
        if (timer != null) timer.cancel();
        emExecucao = false;
        System.out.println("Simulação encerrada.");

        // Relatório final
        System.out.println("\n========= RELATÓRIO FINAL =========");

        //variaveis de soma
        int somaTempoTotal = 0;
        int somaEnergiaTotal = 0;
        int somaViagensTotal = 0;

        Ponteiro pElev = predio.getCentral().getElevadores().getInicio();
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
            pElev = pElev.getProximo();
        }
        System.out.println("===================================\n");
        System.out.println("===================================");
        System.out.printf("Tempo total (todos os elevadores): %d min%n", somaTempoTotal);
        System.out.printf("Energia total (todos os elevadores): %d%n", somaEnergiaTotal);
        System.out.printf("Total de viagens (todos os elevadores): %d%n", somaViagensTotal);
        System.out.println("===================================\n");
    }

    private void gerarPessoas(int minutoAtual) {

        if (pessoasRestantes <= 0) return;

        // Estratégia: distribui pessoas ao longo do dia uniformemente
        double taxaBase =(double)  totalPessoas / tempoTotalSimulacao;

        // simular horários de pico
        double taxa = taxaBase;
        if (minutoAtual >= 420 && minutoAtual <= 540) taxa *= 1.6; // pico manhã (7h–9h)
        if (minutoAtual >= 1020 && minutoAtual <= 1140) taxa += 1.5; // pico tarde (17h–19h)

        acumuladorPessoa += taxa;

        int pessoasAGerar = (int) acumuladorPessoa;
        acumuladorPessoa -= pessoasAGerar;

        Random random = new Random();
        int quantidadeAndares = getQuantidadeAndares();

        for (int i = 0; i < pessoasAGerar && pessoasRestantes > 0; i++) {
            int destino = random.nextInt(quantidadeAndares - 1) + 1; // 1 até N-1
            boolean idoso = random.nextInt(100) < 15;
            boolean cadeirante = !idoso && random.nextInt(100) < 3;
            Pessoa p = new Pessoa(idPessoaAtual++, 0, destino, idoso, cadeirante);
            getAndar(0).adicionarPessoa(p, minutoAtual);
            pessoasRestantes--;

            System.out.println("Pessoa " + p.getId() + " gerada para o andar " + destino + " no minuto " + minutoAtual);

        }
    }

    /*private void gerarPessoasIniciais(int minutoAtual) {
        Andar terreo = getAndar(0);
        Random random = new Random();
        int quantidadeAndares = getQuantidadeAndares();

        for (int i = 0; i < totalPessoas; i++) {
            int destino = random.nextInt(quantidadeAndares - 1) + 1; // de 1 até N-1
            boolean idoso      = random.nextInt(100) < 15;  // 15 % idoso
            boolean cadeirante = !idoso && random.nextInt(100) < 3; // 3 % cadeirante
            Pessoa p = new Pessoa(i + 1, 0, destino, idoso, cadeirante);
            terreo.adicionarPessoa(p, minutoAtual);
        }
    }*/

    private Andar getAndar(int numero) {
        Ponteiro p = predio.getAndares().getInicio();
        while (p != null) {
            Andar a = (Andar) p.getElemento();
            if (a.getNumero() == numero) return a;
            p = p.getProximo();
        }
        return null;
    }

    private int getQuantidadeAndares() {
        int count = 0;
        Ponteiro p = predio.getAndares().getInicio();
        while (p != null) {
            count++;
            p = p.getProximo();
        }
        return count;
    }

    public void gravar(String nomeArquivo) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(nomeArquivo))) {
            out.writeObject(this);
            System.out.println("Simulação gravada em: " + nomeArquivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Simulador carregar(String nomeArquivo) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(nomeArquivo))) {
            Simulador sim = (Simulador) in.readObject();
            sim.continuar();
            return sim;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Predio getPredio() {
        return predio;
    }

    public int getMinutoAtual() {
        return minutoSimulado;
    }

}
