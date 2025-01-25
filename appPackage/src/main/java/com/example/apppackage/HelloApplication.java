package com.example.apppackage;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Pattern;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class HelloApplication extends Application
{
    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        stage.setTitle("New Mail");
        stage.setScene(scene);
        stage.show();
    }
    boolean flag=false;
    public boolean emailchecker(String email)
    {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(regex);
        if(pattern.matcher(email).matches())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public void mail(String toadd, String subject, String text,File[] attachments)
    {
        String to = toadd; // recipient's email ID
        String from = "r4321684@gmail.com"; // sender's email ID
        final String username = "r4321684@gmail.com"; // sender's email username
        final String password = "uqir vgvz pgoe xcko"; // sender's email password
        String host = "smtp.gmail.com"; // SMTP server address
        // Setup properties for the email session
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        // Create a session with authentication
        Session session = Session.getInstance(props, new jakarta.mail.Authenticator()
        {
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(username, password);
            }
        });
        try
        {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            String[] toAddresses = to.split(",");
            //Address[] addresses = new InternetAddress[toAddresses.length];
            ArrayList<Address> addresses=new ArrayList<>();
            for (int i = 0; i < toAddresses.length; i++)
            {
                if(emailchecker(toAddresses[i]))
                {
                    addresses.add(new InternetAddress(toAddresses[i].trim()));//changes done here
                }
                else
                {
                    System.out.println("Invalid Mail Address:"+toAddresses[i].trim());
                }
            }
            if(addresses.isEmpty())
            {
                System.out.println("No mail address found");
                return;
            }
            message.setRecipients(Message.RecipientType.TO, addresses.toArray(new Address[0]));
            message.setSubject(subject);
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(text);
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            // Attach files
            if (attachments != null)
            {
                for (File file : attachments)
                {
                    BodyPart attachPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(file);
                    attachPart.setDataHandler(new DataHandler(source));
                    attachPart.setFileName(file.getName());
                    multipart.addBodyPart(attachPart);
                }
            }
            message.setContent(multipart);
            Transport.send(message);
            System.out.println("Email Message Sent Successfully");
            flag=true;
        }
        catch (MessagingException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Error sending email", e);
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            VBox vBox;
            if(flag)
            {
                vBox= new VBox(new Label("Message has been sent successfully."));
            }
            else
            {
                vBox= new VBox(new Label("Failed to send message."));
            }
            vBox.setAlignment(Pos.CENTER);
            Scene scene = new Scene(vBox,200,100);
            Stage stage = new Stage();
            stage.setTitle("Status Window");
            stage.setScene(scene);
            stage.show();
        }
    }
    public static void main(String[] args)
    {
        launch();
    }
}