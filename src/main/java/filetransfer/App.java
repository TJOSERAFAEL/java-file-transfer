package filetransfer;

import java.util.Scanner;
import filetransfer.FileHandler;
import filetransfer.Server;
import filetransfer.Client;
import filetransfer.Progress;

public class App {
    private static Scanner in;
    private static Progress fileProgress = new Progress();

    public static void main(String[] args) {
        runServer();
        runRenderProgress();
        runClient();
    }

    private static void runServer() {
        FileHandler fileHandler = new FileHandler();
        new Server(fileHandler, fileProgress).start();
    }

    private static void runClient() {
        FileHandler fileHandler = new FileHandler();
        Client client = new Client(fileHandler);
        in = new Scanner(System.in);

        String hostname = "localhost";
        Integer port = 9901;

        System.out.println("Please enter hostname or IP: ");
        hostname = in.nextLine();

        while (true) {
            if (client.connect(hostname, port)) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print("Enter file path to send: ");
                String path = in.next();

                if (!fileHandler.readFile(path)) {
                    System.out.println("File '" + path + "' not found'");
                    continue;
                }
                client.sendFileSize(path);
                client.sendFileName(path);
                client.sendFile(path);
            }
        }
    }

    private static void runRenderProgress() {
        new ProgressThread(fileProgress).start();
    }
}
