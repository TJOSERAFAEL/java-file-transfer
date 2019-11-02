package filetransfer;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.Test;

/**
 * Unit test for simple ProgressTest.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProgressTest {
    private static Progress progress = new Progress();
    private static int NUMBER_OF_FILES = 100;

    @Before
    public void initFiles() {
        progress.clearFilesProgress();
        for (int i = 0; i < NUMBER_OF_FILES; i++) {
            progress.addFileProgress("/test" + i, 0.99);
        }
    }

    @Test
    public void _1_shouldAddFilesProgress() {
        assertEquals(NUMBER_OF_FILES, progress.getFilesProgress().size());
    }

    @Test
    public void _2_shouldGetFileProgress() {
        ArrayList<String> fileArrayList = progress.getFilesProgress();
        assertEquals(NUMBER_OF_FILES, fileArrayList.size());
    }

    @Test
    public void _3_shouldClearFileProgress() {
        progress.clearFilesProgress();
        ArrayList<String> fileArrayList = progress.getFilesProgress();
        assertEquals(0, fileArrayList.size());
    }

    @Test
    public void _4_shouldRemoveFileProgress() {
        progress.clearFilesProgress();
        progress.addFileProgress("/file-to-remove", 0.99);
        progress.removeFileProgress("/file-to-remove");
        ArrayList<String> fileArrayList = progress.getFilesProgress();
        assertEquals(0, fileArrayList.size());
    }

}
