package Simulacao;

import java.io.Serializable;

public abstract class EntidadeSimulavel implements Serializable { // Classe base abstrata que permite que objetos sejam atualizados na simulação
    public abstract void atualizar(int minutoSimulado);           // Método que deve ser implementado por qualquer entidade simulável
}
