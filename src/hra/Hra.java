package hra;


import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import java.io.*;
import javafx.event.ActionEvent;
import java.util.Random;
import java.util.*;


public class Hra extends Application {
    private Policko[][] HraciaPlocha;
    private Stage primaryStage;
    private Menu mFile;
    private MenuItem miFileNew;
    private MenuItem miFileOpen;
    private MenuItem miFileSave;
    private MenuItem miFileExit;
    private GridPane grid;
    private int aktualR;
    private int aktualS;
    public int pocetTahov = 0;
    private boolean canSave = false;
    private boolean end = false;
    private int pocitadlo = 0;
    private int n;
    private int m;


    private boolean newAction() {
        canSave = true;
        Dialog dialog = new Dialog(this);
        dialog.showDialog();
        return true;
    }


    private boolean exitAction() {
        Platform.exit();
        return true;
    }


    private void loadFromFile(File file) throws IOException {
        Scanner in = new Scanner(new BufferedReader(new FileReader(file)));
        this.m = in.nextInt();
        this.n = in.nextInt();
        this.grid.getChildren().clear();
        this.HraciaPlocha = new Policko[n][m];
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                int cislo = in.nextInt();
                if (cislo == 0) {
                    this.aktualR = i;
                    this.aktualS = j;
                    this.HraciaPlocha[i][j] = new Policko(i, j, n * m, this);
                } else {
                    this.HraciaPlocha[i][j] = new Policko(i, j, cislo, this);
                    grid.add(HraciaPlocha[i][j], j, i, 1, 1);
                }

            }
        }
        this.pocetTahov = in.nextInt();
        this.primaryStage.sizeToScene();
    }

    private void writeToFile(File file) throws IOException {
        PrintStream out = new PrintStream(file);
        out.print(m);
        out.print(" ");
        out.println(n);

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (HraciaPlocha[i][j].getCislo() == m*n) {
                    out.print("0 ");
                } else {
                    out.print(HraciaPlocha[i][j].getCislo());
                    out.print(" ");
                }
            }
        out.println();
        }
        out.println(pocetTahov);
    }


    private boolean openAction() {
        File file = chooseFileToOpen();
        if (file != null) {
            try {
                loadFromFile(file);
            } catch (IOException e) {
                System.err.println("Nieco sa pokazilo.");
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean saveAction() {
        File file = chooseFileToSave();
        if (file != null && canSave) {
            try {
                writeToFile(file);
            } catch (IOException e) {
                System.err.println("Nieco sa pokazilo.");
            }
            return true;
        } else return false;
    }

    private FileChooser prepareFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Textové súbory (*.txt)", "*.txt"),
                new FileChooser.ExtensionFilter("Všetky súbory (*.*)", "*.*"));
        return fileChooser;
    }


    private File chooseFileToOpen() {
        FileChooser fileChooser = prepareFileChooser();
        fileChooser.setTitle("Otvoriť");
        File file = fileChooser.showOpenDialog(primaryStage);
        return file;
    }

    private File chooseFileToSave() {
        FileChooser fileChooser = prepareFileChooser();
        fileChooser.setTitle("Uložiť");
        File file = fileChooser.showSaveDialog(primaryStage);
        return file;
    }


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        grid = new GridPane();

        grid.setHgap(2);
        grid.setVgap(2);

        BorderPane border = new BorderPane();
        border.setCenter(grid);

        MenuBar menuBar = new MenuBar();
        border.setTop(menuBar);

        Scene scene = new Scene(border);
        primaryStage.setScene(scene);
        primaryStage.setTitle("15");
        primaryStage.show();


        mFile = new Menu("Game");
        miFileNew = new MenuItem("New");
        miFileOpen = new MenuItem("Open");
        miFileSave = new MenuItem("Save");
        miFileExit = new MenuItem("Exit");
        mFile.getItems().addAll(miFileNew, miFileOpen, miFileSave, miFileExit);
        menuBar.getMenus().add(mFile);

        miFileNew.setOnAction((ActionEvent event) -> {
            newAction();
        });

        miFileOpen.setOnAction((ActionEvent event) -> {
            openAction();
        });

        miFileSave.setDisable(!canSave);
        miFileSave.setOnAction((ActionEvent event) -> {
            saveAction();
        });
        miFileExit.setOnAction((ActionEvent event) -> {
            exitAction();
        });
    }

    public void posunPolicko(Policko policko) {
        boolean pom = false;
        if (aktualR < m - 1 && HraciaPlocha[aktualR + 1][aktualS] == policko) {
            pom = true;
        }
        if (aktualS < n - 1 && HraciaPlocha[aktualR][aktualS + 1] == policko) {
            pom = true;
        }
        if (aktualR > 0 && HraciaPlocha[aktualR - 1][aktualS] == policko) {
            pom = true;
        }
        if (aktualS > 0 && HraciaPlocha[aktualR][aktualS - 1] == policko) {
            pom = true;
        }
        if (pom) {
            pocetTahov++;
            GridPane.setConstraints(policko, aktualS, aktualR);
            HraciaPlocha[aktualR][aktualS].setStlpec(policko.getStlpec());
            HraciaPlocha[aktualR][aktualS].setRiadok(policko.getRiadok());
            HraciaPlocha[policko.getRiadok()][policko.getStlpec()] = HraciaPlocha[aktualR][aktualS];
            int y = aktualS;
            int x = aktualR;
            aktualS = policko.getStlpec();
            aktualR = policko.getRiadok();
            policko.setRiadok(x);
            policko.setStlpec(y);
            HraciaPlocha[x][y] = policko;
        }

        boolean pomocna = true;
        int pocitadlo2 = 0;
        for (int k = 0; k < m; k++) {
            boolean pomocna2 = false;
            for (int l = 0; l < n; l++) {
                pocitadlo2++;
                if (HraciaPlocha[k][l].getCislo()!=pocitadlo2) {
                    pomocna = false;
                    pomocna2 = true;
                    break;
                }
            }
            if (pomocna2) break;

        }

        if (pocetTahov > 0 && aktualS == n-1 && aktualR == m-1 && pomocna) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("The End");
            alert.setHeaderText(null);
            alert.setContentText("Pocet tahov " + pocetTahov);

            ButtonType buttonTypeOK = new ButtonType("OK", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonTypeOK);
            alert.show();

            this.grid.getChildren().clear();
            miFileSave.setDisable(canSave);
        }
    }

    public void generujHru(int n, int m, int pocetGenerovani) {
        this.grid.getChildren().clear();
        this.m = m;
        this.n = n;
        pocetTahov = 0;
        HraciaPlocha = new Policko[m][n];
        aktualR = m - 1;
        aktualS = n - 1;
        pocitadlo = 0;
        miFileSave.setDisable(!canSave);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                pocitadlo++;
                Policko policko = new Policko(i, j, pocitadlo, this);
                HraciaPlocha[i][j] = policko;
                if (!(i == m - 1 && j == n - 1)) {
                    grid.add(policko, j, i, 1, 1);
                }
            }
        }

        for (int i = 0; i < pocetGenerovani; i++) {
            Random random = new Random();
            int x = random.nextInt(4);
            if (x==0 && aktualR >= 1) {
                GridPane.setConstraints(HraciaPlocha[aktualR-1][aktualS], aktualS, aktualR);
                Policko pomocna = HraciaPlocha[aktualR-1][aktualS];
                pomocna.setRiadok(aktualR);
                pomocna.setStlpec(aktualS);
                HraciaPlocha[aktualR-1][aktualS] = HraciaPlocha[aktualR][aktualS];
                HraciaPlocha[aktualR-1][aktualS].setRiadok(aktualR - 1);
                HraciaPlocha[aktualR][aktualS] = pomocna;
                aktualR--;
            }
            if (x==1 && aktualS < n-1) {
                GridPane.setConstraints(HraciaPlocha[aktualR][aktualS+1], aktualS, aktualR);
                Policko pomocna = HraciaPlocha[aktualR][aktualS+1];
                pomocna.setRiadok(aktualR);
                pomocna.setStlpec(aktualS);
                HraciaPlocha[aktualR][aktualS+1] = HraciaPlocha[aktualR][aktualS];
                HraciaPlocha[aktualR][aktualS+1].setStlpec(aktualS + 1);
                HraciaPlocha[aktualR][aktualS] = pomocna;
                aktualS++;
            }
            if (x==2 && aktualR < m-1) {
                GridPane.setConstraints(HraciaPlocha[aktualR+1][aktualS], aktualS, aktualR);
                Policko pomocna = HraciaPlocha[aktualR+1][aktualS];
                pomocna.setRiadok(aktualR);
                pomocna.setStlpec(aktualS);
                HraciaPlocha[aktualR+1][aktualS] = HraciaPlocha[aktualR][aktualS];
                HraciaPlocha[aktualR+1][aktualS].setRiadok(aktualR + 1);
                HraciaPlocha[aktualR][aktualS] = pomocna;
                aktualR++;
            }
            if (x==3 && aktualS >= 1) {
                GridPane.setConstraints(HraciaPlocha[aktualR][aktualS-1], aktualS, aktualR);
                Policko pomocna = HraciaPlocha[aktualR][aktualS-1];
                pomocna.setRiadok(aktualR);
                pomocna.setStlpec(aktualS);
                HraciaPlocha[aktualR][aktualS-1] = HraciaPlocha[aktualR][aktualS];
                HraciaPlocha[aktualR][aktualS-1].setStlpec(aktualS - 1);
                HraciaPlocha[aktualR][aktualS] = pomocna;
                aktualS--;
            }
        }
        this.primaryStage.sizeToScene();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
