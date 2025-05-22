package Base;

import EstruturaDados.Fila;
import EstruturaDados.Lista;
import EstruturaDados.Ponteiro;
import Simulacao.PainelElevador;

import java.io.Serializable;

public class Andar implements Serializable {
    private int numero;
    private Fila pessoasAguardando; //estão esperando levador
    private PainelElevador painel;
    private Lista pessoasPresentes; //pessoas que estão "trabalhando no andar"

    public Andar(int numero) {
        this.numero = numero;
        this.pessoasAguardando = new Fila();
        this.painel = new PainelElevador();
        this.pessoasPresentes = new Lista();
    }

    public int getNumero() {
        return numero;
    }

    public Fila getPessoasAguardando() {
        return pessoasAguardando;
    }

    public Lista getPessoasPresentes() { //é pra ser as pessoas no andar!
        return pessoasPresentes;
    }

    public PainelElevador getPainel() {
        return painel;
    }

    public void adicionarPessoa(Pessoa pessoa, int minutoAtual) {
        if (pessoa.isPrioritaria()) {
            pessoasAguardando.enfileirarPrioritario(pessoa);
        } else {
            pessoasAguardando.enfileirar(pessoa);
        }
        painel.registrarChamada(pessoa.getAndarDestino());
        System.out.println("Pessoa " + pessoa.getId() +
                (pessoa.isPrioritaria() ? " (PRIORIDADE)" : "") +
                " adicionada no andar " + numero + " com destino ao " +
                pessoa.getAndarDestino());
    }

    public void embarcarPessoas(Elevador elevador, int minutoAtual) {
        Ponteiro atual = pessoasAguardando.getInicio();

        while (atual != null && elevador.getCapacidadeDisponivel() > 0) {
            Pessoa pessoa = (Pessoa) atual.getElemento();

            boolean mesmaDirecao = (elevador.isSubindo() && pessoa.getAndarDestino() > numero) ||
                    (!elevador.isSubindo() && pessoa.getAndarDestino() < numero);

            if (pessoa.getAndarOrigem() == numero && mesmaDirecao) {
                elevador.adicionarPassageiro(pessoa, minutoAtual);
                pessoasAguardando.desenfileirar();
            }

            atual = atual.getProximo();
        }

        if (pessoasAguardando.estaVazia()) {
            painel.limparChamadas();
        }
    }

    public void embarcarPessoasPorDestino(Elevador elevador, int destino, int minutoAtual) {
        Ponteiro atual = pessoasAguardando.getInicio();
        Ponteiro anterior = null;

        while (atual != null && elevador.getCapacidadeDisponivel() > 0) {
            Pessoa pessoa = (Pessoa) atual.getElemento();
            Ponteiro proximo = atual.getProximo();

            if (pessoa.getAndarDestino() == destino && pessoa.getAndarOrigem() == this.numero) {
                elevador.adicionarPassageiro(pessoa, minutoAtual);
                if (anterior == null) {
                    pessoasAguardando.setInicio(proximo);
                } else {
                    anterior.setProximo(proximo);
                }
                if (proximo == null) {
                    pessoasAguardando.setFim(anterior);
                }
            } else {
                anterior = atual;
            }

            atual = proximo;
        }

        if (pessoasAguardando.estaVazia()) {
            painel.limparChamadas();
        }
    }

    public void registrarChegadaDePassageiro(Pessoa pessoa, int minutoAtual) {
        pessoa.registrarTempoChegada(minutoAtual);
        pessoasPresentes.inserirFim(pessoa);
        System.out.println("Pessoa " + pessoa.getId() + " está temporariamente no andar " + numero);
    }


    public void processarRetornoParaTerreo(int minutoAtual) {
        Ponteiro atual = pessoasPresentes.getInicio();
        Ponteiro anterior = null;

        while (atual != null) {
            Pessoa pessoa = (Pessoa) atual.getElemento();
            Ponteiro proximo = atual.getProximo();

            if (minutoAtual - pessoa.getTempoChegada() >= 3) { // ficou 3 minutos no andar
                pessoa.setAndarOrigem(numero);
                pessoa.setAndarDestino(0); // térreo
                adicionarPessoa(pessoa, minutoAtual); // volta para a fila de espera
                painel.registrarChamada(0);
                System.out.println("Pessoa " + pessoa.getId() + " quer retornar ao térreo.");

                // remove da lista de pessoas presentes
                if (anterior == null) {
                    pessoasPresentes.setInicio(proximo);
                } else {
                    anterior.setProximo(proximo);
                }

                if (proximo == null) {
                    pessoasPresentes.setFim(anterior);
                }
            } else {
                anterior = atual;
            }

            atual = proximo;
        }
    }
}