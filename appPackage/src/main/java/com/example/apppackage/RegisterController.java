package com.example.apppackage;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.Arrays;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class RegisterController implements Initializable
{
    @FXML
    public Button closeButton,registerButton;
    @FXML
    public Label firstnameLabel,lastnameLabel,emailIdLabel,emailPasswordLabel,usernameLabel,passwordLabel,confirmPasswordLabel,userRegistrationLabel,registrationMsgLabel,passwordValidateLabel1,passwordValidateLabel2,usernameValidateLabel,emailValidateLabel;
    @FXML
    public ImageView mailImageView;
    @FXML
    public TextField firstnameTextField,lastnameTextField,emailIdTextField,usernameTextField;
    @FXML
    public PasswordField emailSetPasswordField,setUserPasswordField,setUserConfirmPasswordField;
    @FXML
    public void registerButtonOnAction()
    {
        registerUser();
    }
    public void registerUser()
    {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String firstname = firstnameTextField.getText();
        String lastname = lastnameTextField.getText();
        String emailId = emailIdTextField.getText();
        String emailIdPassword = emailSetPasswordField.getText();
        String username = usernameTextField.getText();
        String password = setUserPasswordField.getText();
        String password1 = setUserPasswordField.getText();
        String password2 = setUserConfirmPasswordField.getText();
        if(!firstname.isBlank() && !lastname.isBlank() && !emailId.isBlank() && !emailIdPassword.isBlank() && !username.isBlank() && !password.isBlank())
        {
            int a = passwordvalidator();
            int b = emailChecker();
            int c = usernamechecker();
            int d = 0;
            if(a==1 && b==1 && c==1)
            {
                String insertFields = "insert into user_account(firstname,lastname,emailid,emailpassword,username,password) values ('";
                String insertValues = firstname + "','" + lastname + "','" + emailId + "','" + emailIdPassword + "','" + username +"','" + password + "')";
                String insertToRegister = insertFields + insertValues;
                if(password1.equals(password2))
                {
                    passwordValidateLabel2.setText("Password entered matched correctly.");
                    d = 1;
                }
                if(d==1)
                {
                    try
                    {
                        Statement statement = connectDB.createStatement();
                        statement.executeUpdate(insertToRegister);
                        registrationMsgLabel.setText("User details registered successfully! You can Sign In Now.");
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        e.getCause();
                    }
                }
                else
                {
                    passwordValidateLabel2.setText("Password entered does not match!");
                }
            }
            else
            {
                registrationMsgLabel.setText("User details not entered! You can Sign In Now.");
            }
        }
        else
        {
            registrationMsgLabel.setText("Please Enter all the fields.");
        }
    }
    public void CloseButtonOnAction()
    {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
        Platform.exit();
    }
    public int passwordvalidator()
    {
        String password = setUserPasswordField.getText();
        int uppercount = 0,lowercount = 0, specialcount = 0, digitcount = 0;
        if(password.length() > 8)
        {
            for(int i=0;i<password.length();i++)
            {
                char a = password.charAt(i);
                if(Character.isUpperCase(a))
                {
                    uppercount+=1;
                }
                else if(Character.isLowerCase(a))
                {
                    lowercount+=1;
                }
                else if(Character.isDigit(a))
                {
                    digitcount+=1;
                }
                else
                {
                    specialcount+=1;
                }
            }
            if(uppercount >=2 && lowercount >=2 && specialcount>=1 && digitcount >=1)
            {
                passwordValidateLabel1.setText("Password is strong password.");
                return 1;
            }
            else if(uppercount < 2)
            {
                passwordValidateLabel1.setText("Password must contain atleast 2 upper case.");
                return 0;
            }
            else if(specialcount < 1)
            {
                passwordValidateLabel1.setText("Password must contain atleast 1 special character.");
                return 0;
            }
            else if(digitcount < 1)
            {
                passwordValidateLabel1.setText("Password must contain atleast one digit.");
                return 0;
            }
            return 0;
        }
        else
        {
            passwordValidateLabel1.setText("Password must be atleast 8 characters in length.");
            return 0;
        }
    }
    public int emailChecker()
    {
        String email = emailIdTextField.getText();
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(regex);
        if(pattern.matcher(email).matches())
        {
            emailValidateLabel.setText("Entered mail id is a valid mail id.");
            return 1;
        }
        else
        {
            emailValidateLabel.setText("Entered mail id is not a valid mail id.");
            return 0;
        }
    }
    public int usernamechecker()
    {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String userExtract = "select username from user_account";
        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(userExtract);
            ArrayList<String> dataList = new ArrayList<String>();
            while(queryResult.next())
            {
                String uname = queryResult.getString("username");
                dataList.add(uname);
            }
            String[] array = dataList.toArray(new String[0]);
            for(int i=0;i < array.length;i++)
            {
                String username = usernameTextField.getText();
                username = username.toLowerCase();
                int flag = 0;
                if(!Arrays.asList(array).contains(username))
                {
                    for(int j=0;j<username.length();j++)
                    {
                        char a = username.charAt(j);
                        if(Character.isSpaceChar(a))
                        {
                            flag = 1;
                        }
                    }
                    if(flag==1)
                    {
                        usernameValidateLabel.setText("Username must not contain any space.");
                        return 0;
                    }
                    else
                    {
                        return 1;
                    }
                }
                else
                {
                    usernameValidateLabel.setText("Username already exists.");
                    return 0;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
        return 0;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        File techMailFile = new File("/Users/jcg/Desktop/Programming/Java Programming/appDBConnect/Images/PSG-Tech-Mail-Logo.png");
        Image techMailImage = new Image(techMailFile.toURI().toString());
        mailImageView.setImage(techMailImage);
    }
}