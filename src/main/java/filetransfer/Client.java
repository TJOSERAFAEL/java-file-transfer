package filetransfer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private static Integer port;
    private byte[] buffer = new byte[8192];
    private Socket socket;
    private FileHandler fileHandler;
    private int timeout;

    public Client(FileHandler fH) {
        port = 9901;
        fileHandler = fH;
        socket = null;
    }

    public void setPort(Integer number) {
        port = number;
    }

    public Integer getPort() {
        return port;
    }

    public void setBufferSize(int size) {
        buffer = new byte[size];
    }  

    public void connect(String hostname, Integer port) throws Exception {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(hostname, port), this.timeout);
        } catch (UnknownHostException unknownHostException ) {
            throw unknownHostException;
        } catch (IOException ioException ) {
            throw ioException;
        }
    }

    public void sendFile(String path) {
        if (fileHandler.readFile(path)) {
            try {
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                Integer bytesReaded = fileHandler.getNextChunck(buffer);

                if (bytesReaded == -1) {
                    socket.close();
                    return;
                }
                
                do {
                    output.write(buffer, 0, bytesReaded);
                    output.flush();
                    bytesReaded = fileHandler.getNextChunck(buffer);
                } while (bytesReaded > 0);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendFileName(String path) {
        try {
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            String fileName = fileHandler.getFileNameFromPath(path);
           
            output.writeInt(fileName.length());
            output.writeBytes(fileName);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendFileSize(String path) {
        try {
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            Long fileSize = fileHandler.getFileSizeInBytes(path);
            output.writeLong(fileSize);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getTimeout() {
        return this.timeout;
    }
}
