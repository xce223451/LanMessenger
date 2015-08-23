package Model;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Theo Stone on 8/19/2015.
 */
public class ChatServer
{
    private static Server server;
    private static HashMap<Integer, String> users;

    static
    {
        server = new Server();
        users = new HashMap<>();
    }

    public static void init() throws IOException
    {
        server.start();
        server.bind(7777);

        server.getKryo().register(Packet.Connect.class);
        server.getKryo().register(Packet.Chat.class);
        server.getKryo().register(Packet.Disconnect.class);

        server.addListener(new Listener()
        {
            @Override
            public void received(Connection c, Object o)
            {
                if (o instanceof Packet.Connect)
                {
                    server.sendToAllExceptTCP(c.getID(), o);
                    users.put(c.getID(), ((Packet.Connect)o).username);
                }
                else if (o instanceof Packet.Chat)
                {
                    server.sendToAllTCP(o);
                }
            }

            @Override
            public void disconnected(Connection c)
            {
                Packet.Disconnect disconnect = new Packet.Disconnect();
                disconnect.username = users.get(c.getID());
                users.remove(c.getID());
                server.sendToAllExceptTCP(c.getID(), disconnect);
            }
        });
    }
}
