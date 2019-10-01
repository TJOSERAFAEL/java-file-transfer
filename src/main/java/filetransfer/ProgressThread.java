package filetransfer;

import java.util.ArrayList;

public class ProgressThread extends Thread {

    private Progress fileProgress;

    public ProgressThread(Progress progress) {
        fileProgress = progress;
    }

    public void run() {

        while (true) {
            try {
                Thread.sleep(200);
                renderProgress(fileProgress.getFilesProgress());
                clearProgress();
            } catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    public void renderProgress(ArrayList<String> files) {
        for (String file : files) {
            System.out.println(file);
        }
    }

    public void clearProgress() {
        fileProgress.clearFilesProgress();
    }
}
