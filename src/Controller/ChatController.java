package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Model.ChatClient;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Created by Theo Stone on 8/19/2015.
 */
public class ChatController implements Initializable
{
    @FXML
    private TextArea messageArea;

    @FXML
    private TextField userMessage;

    @FXML
    private ImageView usersImage;

    Task task = new Task<Void>()
    {
        @Override
        public Void call() throws Exception
        {
            while (true)
            {
                Platform.runLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        setMessageArea(ChatClient.getMessages());
                    }
                });
                Thread.sleep(500);
            }
        }
    };

    Thread thread = new Thread(task);


    @Override
    public void initialize(URL arg0, ResourceBundle arg1)
    {
        userMessage.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {
                    sendMessage();
                }
            }
        });

        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                userMessage.requestFocus();
            }
        });

        usersImage.setImage(new Image("file:res/send.png"));
        messageArea.setEditable(false);
        thread.setDaemon(true);
        thread.start();
    }

    public void send(ActionEvent e)
    {
        sendMessage();
    }

    private void sendMessage()
    {
        ChatClient.sendMessage(userMessage.getText());
        userMessage.clear();
    }
    public void setMessageArea(ArrayList<String> messages)
    {
        messageArea.clear();
        for(String message: messages)
        {
            messageArea.appendText(message + "\n");
        }
    }
}
