package EstruturaDados;

public class FilaComPrioridade {

    private Fila filaVip   = new Fila(); // Fila para pessoas com prioridade (cadeirante/gestante/idoso)
    private Fila filaNormal = new Fila(); // Fila para pessoas sem prioridade

    public void enfileirar(Object elemento, boolean prioridade) {
        if (prioridade) filaVip.enfileirar(elemento);      // Enfileira na fila VIP se for prioritário
        else            filaNormal.enfileirar(elemento);   // Caso contrário, enfileira na fila normal
    }

    public Object desenfileirar() {
        if (!filaVip.estaVazia()) {                        // Se a fila VIP não estiver vazia
            Object e = filaVip.primeiro();                 // Pega o primeiro da fila VIP
            filaVip.desenfileirar();                       // Remove da fila VIP
            return e;                                      // Retorna o elemento retirado
        }
        if (!filaNormal.estaVazia()) {                     // Se a fila normal não estiver vazia
            Object e = filaNormal.primeiro();              // Pega o primeiro da fila normal
            filaNormal.desenfileirar();                    // Remove da fila normal
            return e;                                      // Retorna o elemento retirado
        }
        return null;                                       // Se ambas estiverem vazias, retorna null
    }

    public boolean estaVazia() {
        return filaVip.estaVazia() && filaNormal.estaVazia(); // Retorna true somente se as duas filas estiverem vazias
    }

    public int getTamanhoTotal() {
        return filaVip.getTamanho() + filaNormal.getTamanho(); // Soma o total de pessoas nas duas filas
    }

    public int getTamanhoPrioritarios() {
        return filaVip.getTamanho(); // Retorna o número de pessoas na fila prioritária
    }
}
