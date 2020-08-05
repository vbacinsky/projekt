package hra;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.event.*;
import javafx.geometry.*;

class Dialog  {
private Stage dialogStage;
    public Dialog(Hra hra) {
        dialogStage = new Stage();

        BorderPane border = new BorderPane();
        Scene scene = new Scene(border);

        dialogStage.setScene(scene);
        VBox central = new VBox();
        central.setPadding(new Insets(5, 1, 5, 1));
        central.setSpacing(10);

        border.setCenter(central);

        Label lblrows = new Label("Rows (2-10):");
        central.getChildren().add(lblrows);

        TextField rows = new TextField();
        central.getChildren().add(rows);

        Label lblColumns = new Label("Columns (2-10):");
        central.getChildren().add(lblColumns);

        TextField columns = new TextField();
        central.getChildren().add(columns);

        Label lblmixing = new Label("Mixing(1-1000000):");
        central.getChildren().add(lblmixing);

        TextField mixing = new TextField();
        central.getChildren().add(mixing);

        Button btnOk = new Button("OK");
        central.getChildren().add(btnOk);

        btnOk.setOnAction((ActionEvent event)-> {
            try {
                if (Integer.parseInt(rows.getText()) > 1 && Integer.parseInt(rows.getText()) < 11 &&
                        Integer.parseInt(mixing.getText()) < 1000000 && Integer.parseInt(mixing.getText()) > 0 &&
                            Integer.parseInt(columns.getText()) > 1 && Integer.parseInt(columns.getText()) < 11) {
                    hra.generujHru(Integer.parseInt(rows.getText()), Integer.parseInt(columns.getText()), Integer.parseInt(mixing.getText()));
                    dialogStage.close();
                }
            }
            catch (NumberFormatException e) {
                System.out.println("chyba");
            }
        });

    }

    public void showDialog() {
        dialogStage.showAndWait();
    }

}