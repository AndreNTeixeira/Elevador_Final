package Simulacao;

import Base.Andar;
import Base.Pessoa;
import Base.Predio;
import EstruturaDados.Ponteiro;

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

    public void configurar(int andares, int elevadores, int pessoas, int velocidadeEmMs) {
        this.velocidadeEmMs = velocidadeEmMs;
        this.totalPessoas = pessoas;
        this.predio = new Predio(andares, elevadores);
        gerarPessoasIniciais(minutoSimulado);
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
                predio.atualizar(minutoSimulado++);
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
    }

    private void gerarPessoasIniciais(int minutoAtual) {
        Andar terreo = getAndar(0);
        Random random = new Random();
        int quantidadeAndares = getQuantidadeAndares();

        for (int i = 0; i < totalPessoas; i++) {
            int destino = random.nextInt(quantidadeAndares - 1) + 1; // de 1 até N-1
            Pessoa p = new Pessoa(i + 1, 0, destino);
            terreo.adicionarPessoa(p, minutoAtual);
        }
    }

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
