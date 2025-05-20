package Base;

import EstruturaDados.Lista;
import EstruturaDados.Ponteiro;
import Metricas.MetricasElevador;
import Simulacao.EntidadeSimulavel;

public class Elevador extends EntidadeSimulavel {
    private int id;
    private int andarAtual = 0;
    private boolean subindo = true;
    private int capacidadeMaxima = 8;
    private Lista passageiros = new Lista();
    private int andarMaximo;

    //adicionando metricas
    private MetricasElevador metricas;
    private long tempoUltimaAtualizacao;
    private boolean estavaParado;

    public Elevador(int id, int andarMaximo) {
        this.id = id;
        this.andarMaximo = andarMaximo;
        // adicionando metricas
        this.metricas = new MetricasElevador();
        this.tempoUltimaAtualizacao = 0;
        this.estavaParado = true;
        System.out.println("Elevador " + id + " criado. Andar máximo: " + andarMaximo);
    }

    @Override
    public void atualizar(int minutoSimulado){
        int tempoDecorrido = minutoSimulado - (int)tempoUltimaAtualizacao;

        if(getQuantidadePassageiros() == 0 && andarAtual==0){
            if(!estavaParado){
                estavaParado = true;
            }
            metricas.adicionarTempoParado(tempoDecorrido);
        } else{
            if(estavaParado){
                estavaParado = false;
                metricas.incrementarViagens();
            }
            int energiaGasta= calcularEnergiaGasta(tempoDecorrido);
            metricas.adicionarEnergiaGasta(energiaGasta);
            metricas.adicionarTempoMovimentacao(tempoDecorrido);
        }
        tempoUltimaAtualizacao = minutoSimulado;
        removerPassageiros(minutoSimulado);
        mover();
    }

    private int calcularEnergiaGasta(int tempoDecorrido){
        int consumoBase=1;
        int consumoPorPassageiro=1;
        int quantidadePassageiros=getQuantidadePassageiros();

        return tempoDecorrido *(consumoBase + (consumoPorPassageiro * quantidadePassageiros));
    }

    public void adicionarPassageiro(Pessoa pessoa, int minutoAtual) {
        if (getQuantidadePassageiros() < capacidadeMaxima && !pessoa.estaDentroDoElevador()) {
            pessoa.entrarElevador();
            pessoa.registrarTempoSaidaFila(minutoAtual);
            pessoa.registrarTempoEntradaElevador(minutoAtual);
            passageiros.inserirFim(pessoa);
            metricas.incrementarPessoasTransportadas();
            System.out.println("Pessoa " + pessoa.getId() + " entrou no Elevador " + id);
        }
    }
    public MetricasElevador getMetricas() {
        return metricas;
    }

    private void removerPassageiros(int minutoAtual) {
        Ponteiro atual = passageiros.getInicio();
        Ponteiro anterior = null;

        while (atual != null) {
            Pessoa p = (Pessoa) atual.getElemento();
            Ponteiro proximo = atual.getProximo();

            if (p.getAndarDestino() == andarAtual) {
                p.sairElevador();
                p.registrarTempoSaidaElevador(minutoAtual);
                System.out.println("Pessoa " + p.getId() + " saiu no andar " + andarAtual + " do Elevador " + id);

                if (anterior == null) {
                    passageiros.setInicio(proximo);
                } else {
                    anterior.setProximo(proximo);
                }

                if (proximo == null) {
                    passageiros.setFim(anterior);
                }

            } else {
                anterior = atual;
            }

            atual = proximo;
        }
    }

    private void mover() {

    /* ────────────────────────────────────────────────
       1) ELEVADOR VAZIO → deve retornar ao térreo
       ────────────────────────────────────────────────*/
        if (getQuantidadePassageiros() == 0) {

            // Se ainda não está no térreo, desce um andar
            if (andarAtual > 0) {
                subindo = false;
                andarAtual--;
            }

            // Chegou vazio ao térreo: reseta direção para cima e sai
            if (andarAtual == 0) {
                subindo = true;
                System.out.println("Elevador " + id +
                        " chegou vazio ao térreo. Pronto para nova viagem.");
                return;   // evita imprimir log “Descendo” parado no 0
            }

    /* ────────────────────────────────────────────────
       2) ELEVADOR COM PASSAGEIROS
       ────────────────────────────────────────────────*/
        } else {

            // Inverte direção nos extremos
            if (subindo && andarAtual == andarMaximo) {
                subindo = false;
            } else if (!subindo && andarAtual == 0) {
                subindo = true;
            }

            // Move um andar conforme a direção
            if (subindo) {
                andarAtual++;
            } else {
                andarAtual--;
            }
        }

    /* ────────────────────────────────────────────────
       Log
       ────────────────────────────────────────────────*/
        System.out.println("Elevador " + id + " está no andar " + andarAtual +
                " com " + getQuantidadePassageiros() + " passageiros. " +
                (subindo ? "Subindo ↑" : "Descendo ↓"));
    }

    public int getAndarAtual() {
        return andarAtual;
    }

    public void setAndarAtual(int andar) {
        this.andarAtual = andar;
    }

    public boolean isSubindo() {
        return subindo;
    }

    public void setSubindo(boolean subindo) {
        this.subindo = subindo;
    }

    public int getQuantidadePassageiros() {
        int count = 0;
        Ponteiro p = passageiros.getInicio();
        while (p != null) {
            count++;
            p = p.getProximo();
        }
        return count;
    }

    public int getCapacidadeDisponivel() {
        return capacidadeMaxima - getQuantidadePassageiros();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Lista getPassageiros() {
        return passageiros;
    }
}
