package k4unl.minecraft.siv.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import k4unl.minecraft.siv.lib.EnumValues;
import k4unl.minecraft.siv.lib.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Koen Beckers (K-4U)
 * Code originally from ryanshawty: https://github.com/ryanshawty/MCJQuery
 * Modified for use in SIV
 */
public class Query {
    /**
     * The target address and port
     */
    private InetSocketAddress address;
    /**
     * <code>null</code> if no successful request has been sent, otherwise a Map
     * containing any metadata received except the player list
     */
    private Map<String, String> values;
    /**
     * <code>null</code> if no successful request has been sent, otherwise an
     * array containing all online player usernames
     */
    private String[]			onlineUsernames;

    private Map<String, Object> extendedValues;

    /**
     * Convenience constructor
     *
     * @see Query#Query(InetSocketAddress)
     * @param host
     *            The target host
     * @param port
     *            The target port
     */
    public Query(String host, int port) {
        this(new InetSocketAddress(host, port));
    }

    /**
     * Create a new instance of this class
     *
     * @param address
     *            The servers IP-address
     */
    public Query(InetSocketAddress address) {
        this.address = address;
    }

    /**
     * Try pinging the server and then sending the query
     *
     * @see Query#pingServer()
     * @see Query#sendQueryRequest()
     * @throws IOException
     *             If the server cannot be pinged
     */
    public void sendQuery() throws IOException {
        sendQueryRequest();
    }

    /**
     * Try pinging the server
     *
     * @return <code>true</code> if the server can be reached within 1.5 second
     */
    public boolean pingServer() {
        // try pinging the given server
        try {
            final Socket socket = new Socket();
            socket.connect(address, 1500);
            socket.close();
            return true;
        } catch(IOException e) {}
        return false;
    }

    /**
     * Get the additional values if the Query has been sent
     *
     * @return The data
     * @throws IllegalStateException
     *             if the query has not been sent yet or there has been an error
     */
    public Map<String, String> getValues() {
        if(values == null)
            throw new IllegalStateException("Query has not been sent yet!");
        else
            return values;
    }

    /**
     * Get the online usernames if the Query has been sent
     *
     * @return The username array
     * @throws IllegalStateException
     *             if the query has not been sent yet or there has been an error
     */
    public String[] getOnlineUsernames() {
        if(onlineUsernames == null)
            throw new IllegalStateException("Query has not been sent yet!");
        else
            return onlineUsernames;
    }

    /**
     * Request the UDP query
     *
     * @throws IOException
     *             if anything goes wrong during the request
     */
    private void sendQueryRequest() throws IOException {
        final DatagramSocket socket = new DatagramSocket();
        try {
            final byte[] receiveData = new byte[10240];
            socket.setSoTimeout(2000);
            sendPacket(socket, address, 0xFE, 0xFD, 0x09, 0x01, 0x01, 0x01, 0x01);
            final int challengeInteger;
            {
                receivePacket(socket, receiveData);
                byte byte1 = -1;
                int i = 0;
                byte[] buffer = new byte[8];
                for(int count = 5; (byte1 = receiveData[count++]) != 0;)
                    buffer[i++] = byte1;
                challengeInteger = Integer.parseInt(new String(buffer).trim());
            }
            sendPacket(socket, address, 0xFE, 0xFD, 0x00, 0x01, 0x01, 0x01, 0x01, challengeInteger >> 24, challengeInteger >> 16, challengeInteger >> 8, challengeInteger, 0x00, 0x00, 0x00, 0x00);

            final int length = receivePacket(socket, receiveData).getLength();
            values = new HashMap<String, String>();
            final AtomicInteger cursor = new AtomicInteger(5);
            while(cursor.get() < length) {
                final String s = readString(receiveData, cursor);
                if(s.length() == 0)
                    break;
                else {
                    final String v = readString(receiveData, cursor);
                    values.put(s, v);
                }
            }
            readString(receiveData, cursor);
            final Set<String> players = new HashSet<String>();
            while(cursor.get() < length) {
                final String name = readString(receiveData, cursor);
                if(name.length() > 0)
                    players.add(name);
            }
            onlineUsernames = players.toArray(new String[players.size()]);
        } finally {
            socket.close();
        }
    }

    public void requestExtendedInfo(EnumValues... values) throws IOException {
        final DatagramSocket socket = new DatagramSocket();
        try {
            final byte[] receiveData = new byte[10240];
            socket.setSoTimeout(2000);
            sendPacket(socket, address, 0xFE, 0xFD, 0x09, 0x01, 0x01, 0x01, 0x01);
            final int challengeInteger;
            {
                receivePacket(socket, receiveData);
                byte byte1 = -1;
                int i = 0;
                byte[] buffer = new byte[8];
                for(int count = 5; (byte1 = receiveData[count++]) != 0;)
                    buffer[i++] = byte1;
                challengeInteger = Integer.parseInt(new String(buffer).trim());
            }

            //Setup json:
            List<String> toRequest = new ArrayList<String>();
            for(EnumValues value : values){
                toRequest.add(value.toString());
            }

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            String endString = gson.toJson(toRequest);

            sendPacket(socket, address, 0xFE, 0xFD, 0x08, 0x01, 0x01, 0x01, 0x01, challengeInteger >> 24, challengeInteger >> 16, challengeInteger >> 8, challengeInteger, endString);

            int length = receivePacket(socket, receiveData).getLength();
            
            handleExtendedPacket(receiveData, length);
        } finally {
            socket.close();
        }
    }



    //TODO: USE THE LENGTH TO PARSE OR YOU'LL OVERFLOW YOUR SHIT
    private void handleExtendedPacket(byte[] receiveData, int length) {
        int i = 5;
        String json = "";
        for(i = 5; i < length-1; i++){
            byte b = receiveData[i];
            json += (char)b;
        }

        Log.debug(json);
        Gson nGson = new Gson();
        Map<String, Object> jsonList = nGson.fromJson(json, HashMap.class);

        for(Map.Entry<String, Object> entry: jsonList.entrySet()){

        }


    }

    /**
     * Helper method to send a datagram packet
     *
     * @param socket
     *            The connection the packet should be sent through
     * @param targetAddress
     *            The target IP
     * @param data
     *            The byte data to be sent
     * @throws IOException
     */
    private final static void sendPacket(DatagramSocket socket, InetSocketAddress targetAddress, byte... data) throws IOException {
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, targetAddress.getAddress(), targetAddress.getPort());
        socket.send(sendPacket);
    }

    /**
     * Helper method to send a datagram packet
     *
     * @see Query#sendPacket(DatagramSocket, InetSocketAddress, byte...)
     * @param socket
     *            The connection the packet should be sent through
     * @param targetAddress
     *            The target IP
     * @param data
     *            The byte data to be sent, will be cast to bytes
     * @throws IOException
     */
    private final static void sendPacket(DatagramSocket socket, InetSocketAddress targetAddress, Object... data) throws IOException {
        List<Byte> list = new ArrayList<Byte>();

        int i = 0;
        for(Object j : data) {
            if(j instanceof Integer) {
                list.add((byte) ((Integer)j & 0xff));
            }else if(j instanceof String){
                for(char c : ((String) j).toCharArray()){
                    list.add((byte)c);
                }
            }
        }

        final byte[] d = new byte[list.size()];
        for(Byte b : list){
            d[i++] = b;
        }

        sendPacket(socket, targetAddress, d);
    }

    /**
     * Receive a packet from the given socket
     *
     * @param socket
     *            the socket
     * @param buffer
     *            the buffer for the information to be written into
     * @return the entire packet
     * @throws IOException
     */
    private final static DatagramPacket receivePacket(DatagramSocket socket, byte[] buffer) throws IOException {
        final DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
        socket.receive(dp);
        return dp;
    }

    /**
     * Read a String until 0x00
     *
     * @param array
     *            The byte array
     * @param cursor
     *            The mutable cursor (will be increased)
     * @return The string
     */
    private final static String readString(byte[] array, AtomicInteger cursor) {
        final int startPosition = cursor.incrementAndGet();
        for(; cursor.get() < array.length && array[cursor.get()] != 0; cursor.incrementAndGet())
            ;
        return new String(Arrays.copyOfRange(array, startPosition, cursor.get()));
    }
}