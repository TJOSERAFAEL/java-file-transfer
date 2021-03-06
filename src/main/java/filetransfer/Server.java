package filetransfer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    private static Integer port;
    private static ServerSocket serverSocket;
    private static FileHandler fileHandler;
    private static Progress fileProgress;
    private static String storagePath;
    private static int bufferSize;

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

    public void stopServer() throws IOException {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }

    public void handleConnection() {
        Socket socket;

        while (true) {
            try {
               socket = serverSocket.accept();
               ServerThread serverThread = new ServerThread(socket, fileHandler, fileProgress);
               serverThread.setStoragePath(storagePath);
               serverThread.setBufferSize(bufferSize);
               serverThread.start();
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

    public void setStoragePath(String path) {
        storagePath = path;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setBufferSize(int size) {
        bufferSize = size;
    }

    public int getBufferSize() {
        return bufferSize;
    }
}
