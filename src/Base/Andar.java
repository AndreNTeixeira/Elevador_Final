package Base;

import EstruturaDados.Fila;
import EstruturaDados.Lista;
import EstruturaDados.Ponteiro;
import Simulacao.PainelElevador;

import java.io.Serializable;

public class Andar implements Serializable {                                        // Representa um andar do prédio na simulação
    private int numero;                                                            // Número identificador do andar
    private Fila pessoasAguardando;                                                // Fila de pessoas aguardando o elevador
    private PainelElevador painel;                                                 // Painel com botões de chamada do elevador
    private Lista pessoasPresentes;                                                // Lista de pessoas presentes no andar (trabalhando)

    public Andar(int numero) {
        this.numero = numero;                                                      // Define o número do andar
        this.pessoasAguardando = new Fila();                                       // Inicializa a fila de espera
        this.painel = new PainelElevador();                                        // Inicializa o painel do andar
        this.pessoasPresentes = new Lista();                                       // Inicializa a lista de pessoas no andar
    }

    public int getNumero() {                                                       // Retorna o número do andar
        return numero;
    }

    public Fila getPessoasAguardando() {                                           // Retorna a fila de espera por elevador
        return pessoasAguardando;
    }

    public Lista getPessoasPresentes() {                                           // Retorna a lista de pessoas no andar
        return pessoasPresentes;
    }

    public PainelElevador getPainel() {                                            // Retorna o painel de chamadas
        return painel;
    }

    public void adicionarPessoa(Pessoa pessoa, int minutoAtual) {                  // Adiciona pessoa à fila de espera com base na prioridade
        if (pessoa.isPrioritaria()) {
            pessoasAguardando.enfileirarPrioritario(pessoa);                       // Enfileira prioritariamente (idoso, cadeirante, etc.)
        } else {
            pessoasAguardando.enfileirar(pessoa);                                  // Enfileira normalmente
        }
        painel.registrarChamada(pessoa.getAndarDestino());                         // Registra chamada no painel para o destino da pessoa
        System.out.println("Pessoa " + pessoa.getId() +
                (pessoa.isPrioritaria() ? " (PRIORIDADE)" : "") +
                " adicionada no andar " + numero + " com destino ao " +
                pessoa.getAndarDestino());
    }

    public void embarcarPessoas(Elevador elevador, int minutoAtual) {              // Embarca pessoas na direção do elevador
        Ponteiro atual = pessoasAguardando.getInicio();

        while (atual != null && elevador.getCapacidadeDisponivel() > 0) {
            Pessoa pessoa = (Pessoa) atual.getElemento();

            boolean mesmaDirecao = (elevador.isSubindo() && pessoa.getAndarDestino() > numero) ||
                    (!elevador.isSubindo() && pessoa.getAndarDestino() < numero); // Verifica se o destino da pessoa está na mesma direção do elevador

            if (pessoa.getAndarOrigem() == numero && mesmaDirecao) {
                elevador.adicionarPassageiro(pessoa, minutoAtual);                // Adiciona ao elevador
                pessoasAguardando.desenfileirar();                                // Remove da fila
            }

            atual = atual.getProximo();                                           // Vai para a próxima pessoa
        }

        if (pessoasAguardando.estaVazia()) {
            painel.limparChamadas();                                              // Limpa chamadas do painel se fila estiver vazia
        }
    }

    public void embarcarPessoasPorDestino(Elevador elevador, int destino, int minutoAtual) {
        Ponteiro atual = pessoasAguardando.getInicio();
        Ponteiro anterior = null;

        while (atual != null && elevador.getCapacidadeDisponivel() > 0) {
            Pessoa pessoa = (Pessoa) atual.getElemento();
            Ponteiro proximo = atual.getProximo();

            if (pessoa.getAndarDestino() == destino && pessoa.getAndarOrigem() == this.numero) {
                elevador.adicionarPassageiro(pessoa, minutoAtual);                // Adiciona ao elevador
                if (anterior == null) {
                    pessoasAguardando.setInicio(proximo);                         // Remove do início da fila
                } else {
                    anterior.setProximo(proximo);                                 // Remove do meio/fim da fila
                }
                if (proximo == null) {
                    pessoasAguardando.setFim(anterior);                           // Atualiza o ponteiro de fim
                }
            } else {
                anterior = atual;
            }

            atual = proximo;
        }

        if (pessoasAguardando.estaVazia()) {
            painel.limparChamadas();                                              // Limpa o painel se não restarem pessoas
        }
    }

    public void registrarChegadaDePassageiro(Pessoa pessoa, int minutoAtual) {
        pessoa.registrarTempoChegada(minutoAtual);                                // Marca o tempo de chegada da pessoa no andar
        pessoasPresentes.inserirFim(pessoa);                                      // Adiciona à lista de presentes no andar
        System.out.println("Pessoa " + pessoa.getId() + " está temporariamente no andar " + numero);
    }

    public void processarRetornoParaTerreo(int minutoAtual) {                     // Verifica quem está há 3 minutos ou mais no andar para retornar ao térreo
        Ponteiro atual = pessoasPresentes.getInicio();
        Ponteiro anterior = null;


        while (atual != null) {
            Pessoa pessoa = (Pessoa) atual.getElemento();
            Ponteiro proximo = atual.getProximo();

            if (minutoAtual - pessoa.getTempoChegada() >= 150) {                    // Fica no andar por 3 minutos
                pessoa.setAndarOrigem(numero);
                pessoa.setAndarDestino(0);                                        // Define destino como térreo
                adicionarPessoa(pessoa, minutoAtual);                             // Coloca novamente na fila de espera
                painel.registrarChamada(0);                                       // Registra chamada para o térreo
                System.out.println("Pessoa " + pessoa.getId() + " quer retornar ao térreo.");

                if (anterior == null) {
                    pessoasPresentes.setInicio(proximo);                          // Remove do início da lista
                } else {
                    anterior.setProximo(proximo);                                 // Remove do meio/fim
                }

                if (proximo == null) {
                    pessoasPresentes.setFim(anterior);                            // Atualiza ponteiro de fim
                }
            } else {
                anterior = atual;
            }

            atual = proximo;                                                      // Avança para a próxima pessoa
        }
    }
}
