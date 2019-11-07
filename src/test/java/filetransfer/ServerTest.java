package filetransfer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.Test;

import filetransfer.Progress;

/**
 * Unit test for simple Server.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServerTest {
    private static FileHandler fileHandler;
    private static Progress fileProgress;
    private static Server server;
    private int bufferSize = 16384;
    private Integer port = 9991;
    private String storagePath = "/test";

    @BeforeClass
    public static void runBeforeClass() {
        fileHandler = new FileHandler();
        fileProgress = new Progress();
        server = new Server(fileHandler, fileProgress);
    }

    @Test
    public void _1_shouldSetPort() {
        server.setPort(port);
        assertTrue(true);
    }

    @Test
    public void _2_shouldGetPort() {
        server.setPort(port);
        assertEquals(port, server.getPort());
    }

    @Test
    public void _3_shouldSetStoragePath() {
        server.setStoragePath(storagePath);
        assertTrue(true);
    }

    @Test
    public void _4_shouldGetStoragePath() {
        server.setStoragePath(storagePath);
        assertEquals(storagePath, server.getStoragePath());
    }

    @Test
    public void _5_shouldSetBufferSize() {
        server.setBufferSize(bufferSize);
        assertTrue(true);
    }

    @Test 
    public void _6_shouldGetBufferSize() {
        server.setBufferSize(bufferSize);
        assertEquals(bufferSize, server.getBufferSize());
    }

    @Test
    public void _7_shouldStartServer() {
        try {
            server.startServer();
        } catch (IOException ioException) {
            assertTrue(false);
        }
    }
    
    @Test
    public void _8_shouldCloseServer() {
        try {
            server.stopServer();
        } catch (IOException ioException) {
            assertTrue(false);
        }
    }
    
}
