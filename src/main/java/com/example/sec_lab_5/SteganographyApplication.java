package com.example.sec_lab_5;

import com.example.sec_lab_5.utils.DecryptCipherOperator;
import com.example.sec_lab_5.utils.EncryptCipherOperator;
import com.example.sec_lab_5.utils.SteganographyUtil;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SteganographyApplication extends Application {

    File inFile;
    File outFile;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Steganography");

        Text fileLoad = new Text("Выберите изначальный файл: ");
        Button loadBtn = new Button("Обзор");
        Text fileLoaded = new Text("Файл не выбран!");

        Text fileExport = new Text("Выберите конечный файл: ");
        Button exportBtn = new Button("Обзор");
        Text fileExported = new Text("Файл не выбран!");

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("img.png").getFile());

        ImageView inView = new ImageView(new Image(new FileInputStream(file)));
        inView.setFitHeight(128);
        inView.setFitWidth(128);

        ImageView outView = new ImageView(new Image(new FileInputStream(file)));
        outView.setFitHeight(128);
        outView.setFitWidth(128);

        Text encP = new Text("Введите пароль: ");
        TextField encPassword = new TextField();

        Text encText = new Text("Введите текст для зашифровки: ");
        TextArea textArea = new TextArea();
        //textArea.setEditable(false);
        textArea.setMaxSize(300, 100);
        textArea.setWrapText(true);

        Button enc = new Button("Зашифровать");
        Button dec = new Button("Дешифровать");

        FlowPane loadPane = new FlowPane(fileLoad, loadBtn, fileLoaded);
        loadPane.setAlignment(Pos.CENTER);
        FlowPane exportPane = new FlowPane(fileExport, exportBtn, fileExported);
        exportPane.setAlignment(Pos.CENTER);
        FlowPane imgPane = new FlowPane(inView, outView);
        imgPane.setAlignment(Pos.CENTER);
        imgPane.setHgap(5);
        FlowPane encPassPane = new FlowPane(encP, encPassword);
        encPassPane.setAlignment(Pos.CENTER);
        FlowPane encTextPane = new FlowPane(encText);
        encTextPane.setAlignment(Pos.CENTER);
        FlowPane encTextAreaPane = new FlowPane(textArea);
        encTextAreaPane.setAlignment(Pos.CENTER);
        FlowPane encDecPane = new FlowPane(enc, dec);
        encDecPane.setAlignment(Pos.CENTER);
        FlowPane root = new FlowPane(loadPane, exportPane, imgPane, encPassPane, encTextPane, encTextAreaPane, encDecPane);
        root.setVgap(5);
        root.setOrientation(Orientation.VERTICAL);

        loadBtn.setOnAction(value -> {
            inFile = fileLoadBtnPressed();
            if(inFile != null) {
                try {
                    InputStream isImage = (InputStream) new FileInputStream(inFile);
                    inView.setImage(new Image(isImage));
                    fileLoaded.setText("Файл загружен!");
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        exportBtn.setOnAction(value -> {
            outFile = fileLoadBtnPressed();
            if(outFile != null) {
                fileExported.setText("Файл загружен!");
            }
        });

        enc.setOnAction(value -> {
            if (outFile == null)
            {
                message("Выходной файл не выбран!");
                return;
            }
            EncryptCipherOperator encryptCipherOperator = new EncryptCipherOperator(encPassword.getText());
            String s = encryptCipherOperator.apply(textArea.getText());
            File ofile = SteganographyUtil.integrateMessage(inFile,
                    s.getBytes(StandardCharsets.UTF_8),
                    outFile);
            if(ofile != null) {
                try {
                    InputStream isImage = (InputStream) new FileInputStream(ofile);
                    outView.setImage(new Image(isImage));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        dec.setOnAction(value -> {
            if (inFile == null)
            {
                message("Входной файл не выбран!");
                return;
            }
            DecryptCipherOperator decryptCipherOperator = new DecryptCipherOperator(encPassword.getText());
            byte[] bytes = SteganographyUtil.disintegrateMessage(inFile);
            message(decryptCipherOperator.apply(new String(bytes)));
        });


        stage.setScene(new Scene(root, 520, 400));
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    public File fileLoadBtnPressed() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter fileExtension = new FileChooser.ExtensionFilter("Выберите файл (*.*)", "*.*");
        fileChooser.getExtensionFilters().add(fileExtension);
        File file = fileChooser.showOpenDialog(new Stage());
        return file;
    }

    public void message(String message) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);

        VBox vbox = new VBox(new Text(message));
        vbox.setMinSize(400, 100);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(15));

        dialogStage.setScene(new Scene(vbox));
        dialogStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}