package filetransfer;

import java.io.File;
import java.util.Optional;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class App {
    private static Scanner in;
    private static Progress fileProgress = new Progress();

    public static void main(String[] args) {
        try {
            Optional<Configuration> configuration = readYamlConfiguration("configuration.yaml");
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
        System.out.println("Ready for receiving files!");
    }

    private static void runServer(Configuration configuration) {
        FileHandler fileHandler = new FileHandler();
        Server server = new Server(fileHandler, fileProgress);

        Integer serverPort = Integer.parseInt(configuration.getServer().get("port"));
        String storagePath = configuration.getServer().get("storage-path");

        server.setPort(serverPort);
        server.setStoragePath(storagePath);
        server.start();
    }

    private static void runClient(Configuration configuration) {
        FileHandler fileHandler = new FileHandler();
        Client client = new Client(fileHandler);

        String hostname = "localhost";
        Integer port = 991;

        int clientTimeout = Integer.parseInt(configuration.getClient().get("timeout"));
        client.setTimeout(clientTimeout);

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

    private static Optional<Configuration> readYamlConfiguration(String path) throws Exception {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            Configuration configuration = mapper.readValue(new File(path), Configuration.class);
            System.out.println(ReflectionToStringBuilder.toString(configuration,ToStringStyle.MULTI_LINE_STYLE));
            return Optional.of(configuration);
        } catch (Exception e) {
            throw e;
        }
    }
}
