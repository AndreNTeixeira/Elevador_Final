import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Simulador simulador = new Simulador();
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n===== MENU SIMULADOR =====");
            System.out.println("1 - Iniciar simulação");
            System.out.println("2 - Pausar simulação");
            System.out.println("3 - Continuar simulação");
            System.out.println("4 - Encerrar simulação");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();

            switch (opcao) {
                case 1 -> simulador.iniciar();
                case 2 -> simulador.pausar();
                case 3 -> simulador.continuar();
                case 4 -> simulador.encerrar();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida.");
            }

        } while (opcao != 0);
    }
}
