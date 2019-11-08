package filetransfer;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.Instant;

public class ServerThread extends Thread {
    private Socket socket;
    private FileHandler fileHandler;
    private Long fileSize;
    private Long totalBytesReceived;
    private byte[] buffer = new byte[8192];
    private Progress fileProgress;
    private String storagePath;

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
        long now = Instant.now().toEpochMilli();
        String fileName = receiveFileName();
        fileHandler.createFile(storagePath + now + "_" + fileName);

        Integer progressDelay = 0;
        while (true) {
            try {
                Integer bytesReaded = dataInputStream.read(buffer);
                totalBytesReceived += bytesReaded;
                updateFileContent(buffer, bytesReaded);
                progressDelay += updateFileProgress(bytesReaded, progressDelay, fileName);

                if (totalBytesReceived >= fileSize) {
                    fileProgress.removeFileProgress(fileName);

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    
                    System.out.println("File '" + fileName + "' received on: " + storagePath);
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

    private void updateFileContent(byte[] buffer, Integer bytesReaded) throws IOException {
        if (bytesReaded > 0) {
            writeBytesToFile(buffer, bytesReaded);
        }
    }

    private Integer updateFileProgress(Integer bytesReaded, Integer progressDelay, String fileName) {
        if (bytesReaded > 0) {
            if (progressDelay >= 20) {
                fileProgress.addFileProgress(fileName, (double) totalBytesReceived / fileSize);
                return -20;
            }
            return 1;
        }
        return 0;
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

    public void setStoragePath(String path) {
        this.storagePath = path;
    }

    public String getStoragePath() {
        return this.storagePath;
    }

    public void setBufferSize(int size) {
        this.buffer = new byte[size];
    }
}
