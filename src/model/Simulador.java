import java.io.*;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Simulador implements Serializable {
    private int minutoSimulado;
    private int velocidadeEmMs;
    private transient Timer timer;
    private boolean emExecucao;
    private Predio predio;
    private int totalPessoas;

    public void iniciar() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite a quantidade de andares: ");
        int andares = scanner.nextInt();
        if (andares < 5) {
            System.out.println("O valor da andares deve ser maior que 5");
            return;
        }

        System.out.print("Digite a quantidade de elevadores: ");
        int elevadores = scanner.nextInt();
        if (elevadores < 2) {
            System.out.println("O valor de elevadores deve ser maior que 2");
            return;
        }

        System.out.print("Digite a velocidade da simulação (em milissegundos): ");
        this.velocidadeEmMs = scanner.nextInt();

        System.out.print("Digite a quantidade de pessoas iniciais: ");
        this.totalPessoas = scanner.nextInt();

        this.predio = new Predio(andares, elevadores);
        gerarPessoasIniciais();

        this.minutoSimulado = 0;
        emExecucao = true;
        iniciarTimer();

        System.out.println("Simulação iniciada.");
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

    private void gerarPessoasIniciais() {
        Andar terreo = getAndar(0);
        Random random = new Random();
        int quantidadeAndares = getQuantidadeAndares();

        for (int i = 0; i < totalPessoas; i++) {
            int destino = random.nextInt(quantidadeAndares - 1) + 1; // de 1 até N-1
            Pessoa p = new Pessoa(i + 1, 0, destino);
            terreo.adicionarPessoa(p);
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
}
