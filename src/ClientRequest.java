import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


public class ClientRequest implements Runnable{
    private String request;
    private String response;
    private final Socket socket;
    private final BufferedReader buffRead;
    private final DataOutputStream OutputStream;
    private final String PATH = "C:\\www\\";
    private final HashMap<String,String> contentTypes = new HashMap<>(){{
        put("html", "text/html");
        put("txt", "text/plain");
        put("jpg", "image/jpeg");
        put("png", "image/png");
        put("pdf", "application/pdf");
    }};
    public ClientRequest(Socket socket, BufferedReader buffRead, DataOutputStream OutputStream) throws IOException {
        this.socket = socket;
        this.buffRead = buffRead;
        this.OutputStream = OutputStream;
    }

    @Override
    public void run() {
        try {
            request = buffRead.readLine();
            String[] httpRequest = request.split(" ");
            System.out.println("[!] Resource \"" + httpRequest[1] + "\" requested");
            if(httpRequest[1].equals("/"))
                showFile("index.html");
            else
                showFile(httpRequest[1]);

            buffRead.close();
            OutputStream.close();
            socket.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showFile(String fileName) throws IOException {
        File file = new File(PATH+fileName);
        String contentType="";
        if(file.exists()){
            FileInputStream fileStream = new FileInputStream(file);
            byte[] fileContent = new byte[(int) file.length()];
            fileStream.read(fileContent);
            fileStream.close();
            for (String key : contentTypes.keySet()) {
                if (file.getName().endsWith("." + key)) { contentType = key; break; }
            }
            OutputStream.writeBytes("HTTP/1.1 200 \r\n");
            OutputStream.writeBytes("Content-Type: " + contentType + "\r\n");
            OutputStream.writeBytes("Content-Length: " + fileContent.length);
            OutputStream.writeBytes("\r\n\r\n");
            OutputStream.write(fileContent);
        } else {

            OutputStream.writeBytes("HTTP/1.1 404 \r\n");
            OutputStream.writeBytes("Content-Type: " + "text/html" + "\r\n");
            OutputStream.writeBytes("\r\n\r\n");
            OutputStream.writeBytes("<h1>Error 404: Resource \"" + fileName + "\" Not Found");
        }
    }
}
