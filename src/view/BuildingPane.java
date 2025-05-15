package view;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import Base.Elevador;
import Base.Predio;
import Simulacao.CentralDeControle;
import EstruturaDados.Ponteiro;

import java.util.HashMap;
import java.util.Map;

public class BuildingPane extends GridPane {

    private final Map<Elevador, StackPane> views = new HashMap<>();
    private final Predio predio;

    public BuildingPane(Predio predio) {
        this.predio = predio;
        setHgap(10);
        setVgap(5);

        CentralDeControle central = predio.getCentral();
        int totalAndares = predio.getAndares().tamanho();

        int col = 0;
        Ponteiro ponteiro = central.getElevadores().getInicio();
        while (ponteiro != null) {
            Elevador elevador = (Elevador) ponteiro.getElemento();
            StackPane elevadorView = criarElevadorView(elevador);
            views.put(elevador, elevadorView);

            int andarAtual = elevador.getAndarAtual();
            if (andarAtual >= 0 && andarAtual < totalAndares) {
                int row = totalAndares - andarAtual - 1;
                add(elevadorView, col++, row);
            } else {
                System.err.println("⚠️ Elevador " + elevador + " em andar inválido: " + andarAtual);
            }

            ponteiro = ponteiro.getProximo();
        }
    }

    public void updateFromModel() {
        int totalAndares = predio.getAndares().tamanho();

        for (Map.Entry<Elevador, StackPane> entry : views.entrySet()) {
            Elevador elevador = entry.getKey();
            StackPane view = entry.getValue();

            int andarAtual = elevador.getAndarAtual();
            if (andarAtual >= 0 && andarAtual < totalAndares) {
                int row = totalAndares - andarAtual - 1;
                setRowIndex(view, row);

                Rectangle rect = (Rectangle) view.getChildren().get(0);
                Text text = (Text) view.getChildren().get(1);

                rect.setFill(elevador.getQuantidadePassageiros() == 0 ? Color.LIGHTGRAY : Color.ORANGE);
                text.setText("P: " + elevador.getQuantidadePassageiros());
            } else {
                System.err.println("⚠️ Atualização ignorada: andar inválido para elevador.");
            }
        }
    }

    private StackPane criarElevadorView(Elevador elevador) {
        Rectangle box = new Rectangle(140, 140);
        box.setFill(Color.LIGHTGRAY);
        box.setStroke(Color.BLACK);

        Text label = new Text("P: " + elevador.getQuantidadePassageiros());
        return new StackPane(box, label);
    }
}
