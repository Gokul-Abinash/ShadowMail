package com.example.apppackage;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

import javafx.fxml.FXML;
import javafx.stage.StageStyle;
import java.util.ResourceBundle;

public class LoginController implements Initializable
{
    @FXML
    public Button signUpButton;
    @FXML
    public Button loginButton;
    @FXML
    public Label loginMessageLabel;
    @FXML
    public ImageView brandingImageView;
    @FXML
    public ImageView profileImageView;
    @FXML
    public TextField usernameTextField;
    @FXML
    public PasswordField enterPasswordField;
    @FXML
    protected void signUpButtonOnAction()
    {
        createAccountForm();
        //Stage stage = (Stage) signUpButton.getScene().getWindow();
        //stage.close();
    }
    @FXML
    protected void loginButtonOnAction()
    {
        if(!usernameTextField.getText().isBlank() && !enterPasswordField.getText().isBlank())
        {
            validateLogin();
        }
        else
        {
            loginMessageLabel.setText("Please Enter Username and Password.");
        }
    }
    @FXML
    protected void validateLogin()
    {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        if(connectDB == null)
        {
            loginMessageLabel.setText("Database Connection Failed!");
        }

        String verifyLogin = "select count(1) from user_account where username ='" + usernameTextField.getText() + "' and password ='" + enterPasswordField.getText() + "'";

        try
        {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);
            while(queryResult.next())
            {
                if(queryResult.getInt(1) == 1)
                {
                    loginMessageLabel.setText("Congratulations!");
                    mailingForum();
                    //createAccountForm();
                }
                else
                {
                    loginMessageLabel.setText("Invalid Login Credentials. New User, Sign Up!");
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }
    public void createAccountForm()
    {
        try
        {
            Stage registerStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("register.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 667);
            registerStage.setTitle("Sign In");
            registerStage.initStyle(StageStyle.DECORATED);
            registerStage.setScene(scene);
            registerStage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }
    public void mailingForum()
    {
        try
        {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 900, 600);
            stage.setTitle("New Mail");
            stage.setScene(scene);
            stage.show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        File brandingFile = new File("/Users/jcg/Desktop/Programming/Java Programming/appDBConnect/Images/openart-image_9rU_muvV_1728143181326_raw.jpg");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

        File profileFile = new File("/Users/jcg/Desktop/Programming/Java Programming/appDBConnect/Images/admin-business-icon-businessman-business-people-male-avatar-profile-pictures-man-in-suit-for-your-web-site-design-logo-app-ui-solid-style-illustration-design-on-white-background-eps-10-vector.jpg");
        Image profileImage = new Image(profileFile.toURI().toString());
        profileImageView.setImage(profileImage);
    }
}


