package Model;

/**
 * Created by Theo Stone on 8/19/2015.
 */
public class Packet
{
    public static class Connect extends Packet
    {
        public String username;
    }

    public static class Disconnect extends Packet
    {
        public String username;
    }

    public static class Chat extends Packet
    {
        public String userName, message;
    }
}