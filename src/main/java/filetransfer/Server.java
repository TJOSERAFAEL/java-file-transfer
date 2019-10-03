package filetransfer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    private static Integer port;
    private static ServerSocket serverSocket;
    private static FileHandler fileHandler;
    private static Progress fileProgress;

    public Server(FileHandler fH, Progress progress) {
        port = 9901;
        fileHandler = fH;
        fileProgress = progress;
    }

    public void setPort(Integer number) {
        port = number;
    }

    public Integer getPort() {
        return port;
    }

    public void startServer() throws IOException{
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Listening on port " + port);
        } catch (IOException e) {
            throw e;
        }
    }

    public void handleConnection() {
        Socket socket;

        while (true) {
            try {
               socket = serverSocket.accept();
               new ServerThread(socket, fileHandler, fileProgress).start();
            } catch (Exception e) {
                return;
            }
        }
    }

    public void run() {
        try {
            startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        handleConnection();
    }
}
