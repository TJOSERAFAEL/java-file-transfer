package filetransfer;

import java.util.Optional;
import java.util.Scanner;

public class App {
    private static Scanner in;
    private static Progress fileProgress = new Progress();
    private static ConfigurationReader configurationReader = new ConfigurationReader("configuration.yaml");

    public static void main(String[] args) {
        try {
            Optional<Configuration> configuration = configurationReader.readYamlConfiguration();
            if (configuration.isPresent()) {
                renderHeader();
                runServer(configuration.get());
                runRenderProgress();
                runClient(configuration.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void renderHeader() {
        System.out.println("___________.__.__           ___________                              _____ ");            
        System.out.println("\\_   _____/|__|  |   ____   \\__    ___/___________    ____   _______/ ____\\___________");
        System.out.println(" |    __)  |  |  | _/ __ \\    |    |  \\_  __ \\__  \\  /    \\ /  ___/\\   __\\/ __ \\_  __ \\");
        System.out.println(" |     \\   |  |  |_\\  ___/    |    |   |  | \\// __ \\|   |  \\___ \\  |  | \\  ___/|  | \\/");
        System.out.println(" \\___  /   |__|____/\\___  >   |____|   |__|  (____  /___|  /____ > |__|  \\___  >__|");   
        System.out.println("     \\/                 \\/                        \\/     \\/     \\/            \\/       ");
        System.out.println("");
    }

    private static void runServer(Configuration configuration) {
        FileHandler fileHandler = new FileHandler();
        Server server = new Server(fileHandler, fileProgress);

        Boolean enabled = Boolean.parseBoolean(configuration.getServer().get("enabled"));

        if (!enabled) {
            return;
        }

        Integer serverPort = Integer.parseInt(configuration.getServer().get("port"));
        String storagePath = configuration.getServer().get("storage-path");
        int bufferSize = Integer.parseInt(configuration.getNetwork().get("buffer-size"));

        server.setPort(serverPort);
        server.setBufferSize(bufferSize);
        server.setStoragePath(storagePath);
        server.start();
    }

    private static void runClient(Configuration configuration) {
        FileHandler fileHandler = new FileHandler();
        Client client = new Client(fileHandler);

        Boolean enabled = Boolean.parseBoolean(configuration.getClient().get("enabled"));

        if (!enabled) {
            return;
        }

        String hostname = "localhost";
        Integer port = Integer.parseInt(configuration.getServer().get("port")); 
        int clientTimeout = Integer.parseInt(configuration.getClient().get("timeout"));
        int bufferSize = Integer.parseInt(configuration.getNetwork().get("buffer-size"));
        client.setTimeout(clientTimeout);
        client.setBufferSize(bufferSize);

        System.out.println("Please enter hostname or IP: ");
        in = new Scanner(System.in);
        hostname = in.nextLine();

        while (true) {
            
            try {
                client.connect(hostname, port);
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Please enter hostname or IP: ");
                hostname = in.nextLine();
                continue;
            }

            try {
                System.out.print("Enter file path to send: ");
                String path = in.next();
                path = fileHandler.removeSurroundingPathQuotes(path);

                if (!fileHandler.readFile(path)) {
                    System.out.println("File '" + path + "' not found'");
                    continue;
                }
                client.sendFileSize(path);
                client.sendFileName(path);
                client.sendFile(path);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void runRenderProgress() {
        new ProgressThread(fileProgress).start();
    }
}
