package filetransfer;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.Test;

/**
 * Unit test for simple FileHandler.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FileHandlerTest {
    private static String testFilePath;
    private static FileHandler fileHandler;

    @BeforeClass
    public static void runBeforeClass() {
        fileHandler = new FileHandler();
        testFilePath = FileHandlerTest.class.getResource("../file.txt").getPath();
    }

    @Test
    public void _1_shouldOpenFile() {
        try {
            FileInputStream fileInputStream = fileHandler.openFile(testFilePath);
            assertNotEquals(null, fileInputStream);
        } catch (FileNotFoundException e) {
            assertTrue(false);
        }
    }

    @Test
    public void _2_shouldReadFile() {
        assertTrue(fileHandler.readFile(testFilePath));
    }

    @Test
    public void _3_shouldGetFileSize() {
        Long fileSize = fileHandler.getFileSizeInBytes(testFilePath);
        assertNotEquals(Long.valueOf(0), fileSize);
    }

    @Test
    public void _4_shouldGetFileContent() {
        fileHandler.readFile(testFilePath);
        byte[] buffer = new byte[8192];
        Integer bytesReaded = fileHandler.getNextChunck(buffer);
        assertNotEquals(Integer.valueOf(0), bytesReaded);

        do {
            bytesReaded = fileHandler.getNextChunck(buffer);
        } while (bytesReaded > 0);

        assertTrue(true);
    }

    @Test
    public void _5_itShouldCloseFile() {
        try {
            fileHandler.closeFile();
            assertTrue(true);
        } catch (IOException e) {
            assertTrue(false);
        }
    }
}
