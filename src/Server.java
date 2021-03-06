import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        int port = 9001;
        ServerSocket serverSocket;
        Socket socket;
        BufferedReader buffRead;
        DataOutputStream OutputStream;


        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server listening on port ["+port+"]..");
            while(true){
                socket = serverSocket.accept();
                buffRead = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                OutputStream = new DataOutputStream(socket.getOutputStream());
                new Thread(new ClientRequest(socket, buffRead,OutputStream)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
