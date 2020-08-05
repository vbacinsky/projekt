package hra;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.scene.control.*;

public class Policko extends StackPane {

    private int stlpec;
    private int riadok;
    private int cislo;

    Policko(int riadok, int stlpec, int cislo, Hra hra) {
        this.riadok = riadok;
        this.stlpec = stlpec;
        this.cislo = cislo;
        this.getChildren().addAll(new Rectangle(50, 50, Color.RED), new Label("" + cislo));
        this.setOnMouseClicked((MouseEvent e) -> hra.posunPolicko(this));
    }


    public int getStlpec() {
        return this.stlpec;
    }

    public int getRiadok() {
        return  this.riadok;
    }

    public void setStlpec(int x){
        this.stlpec = x;
    }

    public void setRiadok(int x){
        this.riadok = x;
    }

    public int getCislo(){
        return this.cislo;
    }
}
