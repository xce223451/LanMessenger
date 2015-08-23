package Model;

import java.io.IOException;
import java.util.ArrayList;

import Controller.ChatController;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

/**
 * Created by Theo Stone on 8/19/2015.
 */
public class ChatClient
{
    private static ArrayList<String> oldMessages;
    private static ArrayList<String> newMessages;
    private static Client client;
    private static String username;

    static
    {
        oldMessages = new ArrayList<>();
        newMessages = new ArrayList<>();
        client = new Client();
        username = "";
    }

    public static void connect(String name, String ipAddress) throws IOException
    {
        username = name;
        client.start();
        client.connect(5000, ipAddress, 7777);
        client.getKryo().register(Packet.Connect.class);
        client.getKryo().register(Packet.Chat.class);
        client.getKryo().register(Packet.Disconnect.class);

        client.addListener(new Listener()
        {
            @Override
            public void received(Connection c, Object o)
            {
                if (o instanceof Packet.Connect)
                {
                    Packet.Connect connect = (Packet.Connect)o;
                    newMessages.add(connect.username + " has connected!");
                }
                else if(o instanceof Packet.Disconnect)
                {
                    Packet.Disconnect disconnect = (Packet.Disconnect)o;
                    newMessages.add(disconnect.username + " has disconnected!");
                }
                else if (o instanceof Packet.Chat)
                {
                    Packet.Chat chat = (Packet.Chat)o;
                    newMessages.add(chat.userName + ": " + chat.message);
                }
            }
        });

        Packet.Connect connect = new Packet.Connect();
        connect.username = username;
        client.sendTCP(connect);
    }

    public static void sendMessage(String message)
    {
        Packet.Chat chat = new Packet.Chat();
        chat.userName = username;
        chat.message = message;
        client.sendTCP(chat);
    }

    public static ArrayList<String> getMessages()
    {
        return newMessages;
    }
}
