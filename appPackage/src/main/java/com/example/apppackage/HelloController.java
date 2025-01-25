package com.example.apppackage;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;

import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
public class HelloController
{
    long sum=0;
    @FXML
    private Label sizeoverload;
    @FXML
    private TextField subjectid;
    @FXML
    private TextArea messageid;
    @FXML
    private TextField toaddress;
    @FXML
    private Label subjecttext;
    @FXML
    private HBox attachmentsHBox;
    private ObservableList<File> attachedFiles = FXCollections.observableArrayList();
    @FXML
    public void onAttachFileClick()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        File file = fileChooser.showOpenDialog(null);
        if (file != null)
        {
            addAttachment(file);
        }
    }
    private void addAttachment(File file)
    {
        attachedFiles.add(file);
        HBox attachmentHBox = new HBox();
        Label fileNameLabel = new Label(file.getName()+"    ");
        Button removeButton = new Button();
        ImageView view=new ImageView(new Image("trash.png",18,18,false,false));
        removeButton.setGraphic(view);
        removeButton.setOnAction(event -> {
            if (file.exists())
            {
                sum-=file.length();
                System.out.println(file.getName() + " has been removed.");
            }
            else
            {
                System.out.println("Failed to remove " + file.getName());
            }
            attachmentsHBox.getChildren().remove(attachmentHBox);
            attachedFiles.remove(file);
            if(sum>26214400)
            {
                sizeoverload.setText("Total attachment size cannot exceed 25MB");
            }
            else
            {
                sizeoverload.setText("");
            }
        });
        attachmentHBox.getChildren().addAll(fileNameLabel, removeButton);
        attachmentsHBox.getChildren().add(attachmentHBox); // Add to the VBox
        sum+=file.length();
        if(sum>26214400)
        {
            sizeoverload.setText("Total attachment size cannot exceed 25MB");
        }
        else
        {
            sizeoverload.setText("");
        }
    }
    @FXML
    protected void onSubjectTextChanged()
    {
        subjecttext.setText(subjectid.getText());
    }
    @FXML
    protected void onSendButtonClick()
    {
        HelloApplication obj=new HelloApplication();
        File[] filesToSend = attachedFiles.toArray(new File[0]);
        obj.mail(toaddress.getText(),subjectid.getText(),messageid.getText(),filesToSend);
        subjectid.setText("");
        toaddress.setText("");
        messageid.setText("");
        subjecttext.setText("Subject");
        attachmentsHBox.getChildren().clear();
        attachedFiles.clear();
        sizeoverload.setText("");
    }
    @FXML
    protected void onDiscardButtonClick()
    {
        subjectid.setText("");
        toaddress.setText("");
        messageid.setText("");
        subjecttext.setText("Subject");
        attachmentsHBox.getChildren().clear();
        attachedFiles.clear();
        sizeoverload.setText("");
    }
}