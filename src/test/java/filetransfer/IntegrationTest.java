package filetransfer;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.Test;

import filetransfer.Progress;

/**
 * Integration test.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IntegrationTest {
    private static FileHandler fileHandlerServer;
    private static FileHandler fileHandlerClient;
    private static Progress fileProgress;
    private static Server server;
    private static Client client;
    private static Optional<Configuration> configuration;
    private static String storagePathConfiguration;
    private static int bufferSize;
    private static int clientTimeout;
    private static Integer port;
    private static String hostname = "localhost";

    @BeforeClass
    public static void runBeforeClass() {
        fileProgress = new Progress();
        fileHandlerServer = new FileHandler();
        fileHandlerClient = new FileHandler();
        client = new Client(fileHandlerClient);
        server = new Server(fileHandlerServer, fileProgress);
    }

    @Test
    public void _1_shouldReadConfiguration() {
        try {
            String configurationPath = ConfigurationTest.class.getResource("../configuration.yaml").getPath();
            ConfigurationReader configurationReader = new ConfigurationReader(configurationPath);
            configuration = configurationReader.readYamlConfiguration();

            if (configuration.isPresent()) {
                port = Integer.parseInt(configuration.get().getServer().get("port"));
                storagePathConfiguration = configuration.get().getServer().get("storage-path");
                bufferSize = Integer.parseInt(configuration.get().getNetwork().get("buffer-size"));
                clientTimeout = Integer.parseInt(configuration.get().getClient().get("timeout"));
            }

         } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void _2_shouldStartServer() {
        String storagePath = IntegrationTest.class.getResource(storagePathConfiguration).getPath();
        server.setPort(port);
        server.setBufferSize(bufferSize);
        server.setStoragePath(storagePath);
        server.start();
    }

    @Test
    public void _3_shouldNotConnectToServer() {
        client.setTimeout(clientTimeout);
        client.setBufferSize(bufferSize);

        try {
            client.connect("bad_host_name", port);
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void _4_shouldConnectToServer() {
        try {
            client.connect(hostname, port);
        } catch (Exception e) {
           assertTrue(false);
        }
    }

    @Test
    public void _5_shouldReadFile() {
        String fileToSendPath = IntegrationTest.class.getResource("../file.txt").getPath();
        assertTrue(fileHandlerClient.readFile(fileToSendPath));
    }

    @Test
    public void _6_shouldSendFileToServer() {
        String fileToSendPath = IntegrationTest.class.getResource("../file.txt").getPath();
        client.sendFileSize(fileToSendPath);
        client.sendFileName(fileToSendPath);
        client.sendFile(fileToSendPath);
    }

    @Test
    public void _7_shouldStopServer() {
        try {
            server.stopServer();
        } catch (IOException ioException) {
            assertTrue(false);
        }
    }
}
