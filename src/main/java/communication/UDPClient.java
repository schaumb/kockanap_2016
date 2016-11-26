package communication;

import bean.CommandType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Created by qqcs on 25/11/16.
 */
public class UDPClient implements Runnable, AutoCloseable {
    private SocketAddress address;
    private DatagramSocket clientSocket;

    private TickMessage tickMessage = new TickMessage();

    private final Function<TickMessage, List<Move>> setMoves;

    public UDPClient(Function<TickMessage, List<Move>> setMoves) {
        this("10.4.11.24", 11111, setMoves);
    }

    private UDPClient(String host, int port, Function<TickMessage, List<Move>> setMoves) {
        this.setMoves = setMoves;
        address = new InetSocketAddress(host, port);
        try {
            clientSocket = new DatagramSocket(null);
            clientSocket.setReuseAddress(true);
            clientSocket.setSoTimeout(1000);
            clientSocket.bind(new InetSocketAddress(12345));

            if(!sendLogin()) {
                System.out.println("Login send failed -> exit");
                System.exit(1);
            }
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    private static final byte[] upperBits = new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
    private static final byte[] lowerBits = new byte[9];

    private void sendPacket(byte[] data) throws IOException {
        clientSocket.send(new DatagramPacket(data, data.length, address));
    }

    public boolean sendLogin() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(upperBits);
            outputStream.write(1);
            outputStream.write(clientSocket.getLocalPort() / 0x100);
            outputStream.write(clientSocket.getLocalPort() % 0x100);
            outputStream.write(lowerBits);

            sendPacket(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean sendMove(Move move) {
        try {
            System.out.println("Move to: " + move);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(upperBits);
            outputStream.write(2);
            outputStream.write(clientSocket.getLocalPort() / 0x100);
            outputStream.write(clientSocket.getLocalPort() % 0x100);
            outputStream.write(move.getCommandType().ordinal());
            int param = move.getParameter();
            if(move.getCommandType() == CommandType.SETSPEED) {
                param += 100;
            } else if (move.getCommandType() == CommandType.SETROTATION) {
                boolean remainder = param % 10 >= 5;
                param /= 10;

                if(remainder) {
                    ++param;
                }
            }
            outputStream.write(param);
            outputStream.write(Arrays.copyOf(lowerBits, 7));

            sendPacket(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean sendLogout() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(upperBits);
            outputStream.write(1);
            outputStream.write(clientSocket.getLocalPort() / 0x100);
            outputStream.write(clientSocket.getLocalPort() % 0x100);
            outputStream.write(lowerBits);

            sendPacket(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public byte[] receive() throws IOException {
        DatagramPacket packet = new DatagramPacket(new byte[16], 16);
        clientSocket.receive(packet);
        return packet.getData();
    }

    @Override
    public void run() {
        while (true) {
            try {
                byte[] message = receive();
                if (Arrays.equals(Arrays.copyOf(message, 4), upperBits) &&
                        message[4] == 0) {
                    if (tickMessage.getStatus() != null) {
                        makeMoves();
                    }
                    tickMessage.setStatus(Status.create(message));
                } else {
                    // check duplicate:
                    Item newItem = Item.create(message);

                    if (tickMessage.getItemList().stream().anyMatch(item -> item.getItemId() == newItem.getItemId())) {
                        makeMoves();
                    }
                    tickMessage.addItem(newItem);
                }
            } catch (SocketTimeoutException e) {
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void makeMoves() {
        for(Move move : setMoves.apply(tickMessage)) {
            if(!sendMove(move)) {
                System.err.println("Not success move: " + move);
            }
        }
        tickMessage = new TickMessage();
    }

    @Override
    public void close() throws Exception {
        sendLogout();
        clientSocket.close();
    }
}
