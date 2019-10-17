package filetransfer;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerThread extends Thread {
    private Socket socket;
    private FileHandler fileHandler;
    private Long fileSize;
    private Long totalBytesReceived;
    private byte[] buffer = new byte[8192];
    private Progress fileProgress;

    public ServerThread(Socket socket, FileHandler fileHandler, Progress progress) {
        this.fileSize = (long) 0;
        this.totalBytesReceived = (long) 0;
        this.socket = socket;
        this.fileHandler = fileHandler;
        this.fileProgress = progress;
    }

    public void run() {
        DataInputStream dataInputStream;
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
            return;
        }

        fileSize = receiveFileSize();
        String fileName = receiveFileName();
        fileHandler.createFile("C:\\FH_" + fileName);

        Integer progressDelay = 0;
        while (true) {
            try {
                Integer bytesReaded = dataInputStream.read(buffer);
                totalBytesReceived += bytesReaded;

                if (bytesReaded > 0) {
                    writeBytesToFile(buffer, bytesReaded);
                    if (progressDelay > 20) {
                        fileProgress.addFileProgress(fileName, (double) totalBytesReceived / fileSize);
                        progressDelay = 0;
                    }
                    progressDelay++;
                }

                if (totalBytesReceived >= fileSize) {
                    fileProgress.removeFileProgress(fileName);

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    
                    System.out.println("File '" + fileName + "' received on drive C: !");
                    fileHandler.closeFile();
                    socket.close();
                    return;
                }
            } catch (IOException ex) {
                System.out.println("Server exception: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void writeBytesToFile(byte[] buffer, Integer len) throws IOException {
        fileHandler.writeOutputStreamNbytes(buffer, len);
    }

    private String receiveFileName() {
        String fileName = "";
        DataInputStream dataInputStream;

        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
            return null;
        }

        try {
            Integer fileNameLength = dataInputStream.readInt();
            Integer totalBytes = 0;
            byte[] buffer = new byte[fileNameLength];
            do {
                Integer numBytes = dataInputStream.read(buffer, 0, fileNameLength);
                fileName += new String(buffer, 0, numBytes);
                totalBytes+= numBytes;
            } while ( totalBytes < fileNameLength);
        } catch (IOException e) {
            return null;
        }

        return fileName;
    }

    private long receiveFileSize() {
        DataInputStream dataInputStream;
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
            return -1;
        }
        try {
            long fileSize = dataInputStream.readLong();
            return fileSize;
        } catch (IOException e) {
            return -1;
        }
    }
}
