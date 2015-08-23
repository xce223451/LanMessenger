package Controller;

import Model.ChatClient;
import Model.ChatServer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable
{
    @FXML
    Button hostButton;

    @FXML
    Button joinButton;

    @FXML
    TextField username;

    @FXML
    TextField ipAddress;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1)
    {
        ipAddress.setText("127.0.0.1");
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                username.requestFocus();
            }
        });
    }

    public void openServerSetup(ActionEvent e)
    {
        if(username.getText().trim().equals(""))
            return;
        try
        {
            ChatServer.init();
            ChatClient.connect(username.getText(), ipAddress.getText());
            openChat();
            closeCurrentWindow();
        }
        catch(IOException ioe)
        {
            System.out.println("Unable to Setup");
        }

    }

    public void openJoinSetup(ActionEvent e)
    {
        if(username.getText().trim().equals(""))
            return;
        try
        {
            ChatClient.connect(username.getText(), ipAddress.getText());
            openChat();
            closeCurrentWindow();
        }
        catch(IOException ioe)
        {
            System.err.println(ioe.getMessage());
        }
    }

    public void openChat()
    {
        Parent root;
        try
        {
            root = FXMLLoader.load(getClass().getResource("/View/Chat.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add("file:res/application.css");
            stage.setTitle("LAN Messenger");
            stage.getIcons().add(new Image("file:res/people_icon.png"));
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
    }

    public void closeCurrentWindow()
    {
        Stage stage = (Stage) ipAddress.getScene().getWindow();
        stage.close();
    }
}
