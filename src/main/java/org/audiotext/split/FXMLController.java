package org.audiotext.split;


import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;


public class FXMLController implements Initializable {


    /*XML FILTER CONTROLLER*/

    @FXML
    private Button chooseButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;

    @FXML
    private TextField filepath;


    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("Open Audio File");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TXT", "*.txt")
        );
    }

    /**
     * Display The Exception to the user
     *
     * @param e       Exception
     * @param Message The Message to display
     */
    private void HandleExceptions(Exception e, String Message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        if (Message == null)
            alert.setContentText(e.toString());
        else alert.setContentText(Message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }



    @FXML
    void handlecancelButton(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handlechooseButton(ActionEvent event) {
        loadOpenDialog(filepath, chooseButton);
    }

    void loadOpenDialog(TextField t, Button btn) {
        Stage stage = (Stage) btn.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            t.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    void handleSaveButton(ActionEvent event) {
        //file Path
        String p = filepath.getText();
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream(p);
        } catch (FileNotFoundException e) {
            HandleExceptions(e,"File Not Found. Please check the given path");
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        String dir =p.substring(0,p.lastIndexOf('\\'));
        String strLine;

//Read File Line By Line
        try {
        while ((strLine = br.readLine()) != null)   {
            String name =strLine.substring(0,strLine.indexOf("###"));
            System.out.println(name);
            String text =strLine.substring(strLine.indexOf("###")+3);
            writeUsingFileWriter(dir,text,name);

        }
        } catch (IOException e) {
            HandleExceptions(e,"Error Writing Files");
        }
        try {
            fstream.close();
        } catch (IOException e) {
            HandleExceptions(e,"Error Closing Steam");
        }


    }

    private static void writeUsingFileWriter(String dir,String data,String name) {
        File directory = new File(dir+"/Output/");
        if (! directory.exists()){System.out.println(directory.getAbsolutePath());
            directory.mkdir();
        }
        File file =new File(directory,name+".txt");
        FileWriter fr = null;
        try {
            fr = new FileWriter(file);
            fr.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //close resources
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}








