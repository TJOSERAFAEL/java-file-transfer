package filetransfer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import org.apache.commons.io.FilenameUtils;
import java.io.IOException;

public class FileHandler {
    private static BufferedInputStream bufferedInputStream = null;
    private static BufferedOutputStream bufferedOutputStream = null;

    public FileHandler() {}

    public FileInputStream openFile(String path) throws FileNotFoundException {
        try {
            return new FileInputStream(path);
        } catch (FileNotFoundException e) {
            throw e;
        }
    }

    public boolean readFile(String path) {
        FileInputStream fileInputStream;
        try {
            fileInputStream = openFile(path);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
        } catch (FileNotFoundException e) {
           return false;
        }

        return true;
    }

    public Integer getNextChunck(byte[] buffer) {
        Integer bytesRead = 0;
        try {
            bytesRead = bufferedInputStream.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytesRead;
    }

    public boolean createFile(String path) {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(path);
            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        } catch (FileNotFoundException e) {
           return false;
        }
        return true;
    }

    public void closeFile() throws IOException {
        if (bufferedOutputStream != null) {
            try {
                bufferedOutputStream.close();
            } catch (IOException e) {
                throw e;
            }
        }
    }

    public String getFileNameFromPath(String path) {
        return FilenameUtils.getName(path);
    }

    public void writeOutputStream(byte[] buffer) throws IOException{
        bufferedOutputStream.write(buffer);
        bufferedOutputStream.flush();
    }

    public void writeOutputStreamNbytes(byte[] buffer, Integer len) throws IOException{
        bufferedOutputStream.write(buffer, 0, len);
        bufferedOutputStream.flush();
    }

    public long getFileSizeInBytes(String path) {
        return new File(path).length();
    }
}
