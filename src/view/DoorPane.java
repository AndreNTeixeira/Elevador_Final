package view;

import javafx.animation.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class DoorPane extends StackPane {

    private final Rectangle left;
    private final Rectangle right;
    private final double half;

    public DoorPane(double w, double h) {
        setPrefSize(w, h);
        half = w / 2;

        left  = criaPorta(half, h);
        right = criaPorta(half, h);

        left .setTranslateX(-half / 2);   // posição “fechada”
        right.setTranslateX(+half / 2);

        getChildren().addAll(left, right);
    }

    private Rectangle criaPorta(double w, double h) {
        Rectangle r = new Rectangle(w, h, Color.DIMGRAY);
        r.setStroke(Color.BLACK);
        return r;
    }

    /* ── animações ───────────────────────────────── */

    public void open()  { desliza(-half, +half); }           // abre
    public void close() { desliza(-half / 2, +half / 2); }   // fecha

    private void desliza(double xLeft, double xRight) {
        Duration d = Duration.millis(400);

        TranslateTransition tl = new TranslateTransition(d, left);
        TranslateTransition tr = new TranslateTransition(d, right);
        tl.setToX(xLeft);
        tr.setToX(xRight);

        new ParallelTransition(tl, tr).play();
    }
}
