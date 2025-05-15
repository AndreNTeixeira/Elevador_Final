package Base;

import EstruturaDados.Lista;
import EstruturaDados.Ponteiro;
import Simulacao.EntidadeSimulavel;

public class Elevador extends EntidadeSimulavel {
    private int id;
    private int andarAtual = 0;
    private boolean subindo = true;
    private int capacidadeMaxima = 8;
    private Lista passageiros = new Lista();
    private int andarMaximo;

    public Elevador(int id, int andarMaximo) {
        this.id = id;
        this.andarMaximo = andarMaximo;
        System.out.println("Elevador " + id + " criado. Andar máximo: " + andarMaximo);
    }

    public void adicionarPassageiro(Pessoa pessoa) {
        if (getQuantidadePassageiros() < capacidadeMaxima && !pessoa.estaDentroDoElevador()) {
            pessoa.entrarElevador();
            passageiros.inserirFim(pessoa);
            System.out.println("Pessoa " + pessoa.getId() + " entrou no Elevador " + id);
        }
    }

    private void removerPassageiros() {
        Ponteiro atual = passageiros.getInicio();
        Ponteiro anterior = null;

        while (atual != null) {
            Pessoa p = (Pessoa) atual.getElemento();
            Ponteiro proximo = atual.getProximo();

            if (p.getAndarDestino() == andarAtual) {
                p.sairElevador();
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
        if (getQuantidadePassageiros() == 0) {
            // Base.Elevador vazio → desce para o térreo
            if (andarAtual > 0) {
                andarAtual--;
            }
            // Se já estiver no térreo, não faz nada
        } else {
            // Base.Elevador com passageiros → segue lógica normal
            if (subindo) {
                if (andarAtual < andarMaximo) {
                    andarAtual++;
                } else {
                    subindo = false;
                    andarAtual--; // começa a descer
                }
            } else {
                if (andarAtual > 0) {
                    andarAtual--;
                } else {
                    subindo = true;
                    andarAtual++; // começa a subir novamente
                }
            }
        }

        System.out.println("Elevador " + id + " está no andar " + andarAtual +
                " com " + getQuantidadePassageiros() + " passageiros.");
    }

    @Override
    public void atualizar(int minutoSimulado) {
        removerPassageiros();
        mover();

        System.out.println("Elevador " + id + " está no andar " + andarAtual +
                " com " + getQuantidadePassageiros() + " passageiros.");
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

    public Lista getPassageiros() {
        return passageiros;
    }
}
