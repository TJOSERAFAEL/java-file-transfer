package filetransfer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Progress {

    private Map<String, Double> fileProgress;

    public Progress() {
        fileProgress = Collections.synchronizedMap(new HashMap<String, Double>());
    }

    public void addFileProgress(String file, Double progress) {
        fileProgress.put(file, progress);
    }

    public void removeFileProgress(String file) {
        fileProgress.remove(file);
    }

    public ArrayList<String> getFilesProgress() {
        ArrayList<String> fileArrayList = new ArrayList<>();

        for (Map.Entry<String, Double> entry : fileProgress.entrySet()) {
            String fileToAdd = "File: " + entry.getKey() + " - " + (entry.getValue() * 100) + "%";
            fileArrayList.add(fileToAdd);

            if (entry.getValue() == 1) {
                fileProgress.remove(entry.getKey());
            }
        }

        return fileArrayList;
    }

    public void clearFilesProgress() {
        ArrayList<Map.Entry<String, Double>> filesToRemove = new ArrayList<Map.Entry<String, Double>>();
        
        for (Map.Entry<String, Double> entry : fileProgress.entrySet()) {
            if (entry.getValue() > 0.98  && entry.getValue() <= 1) {
                filesToRemove.add(entry);
            }
        }
        
        for (Map.Entry<String, Double> fileToRemove : filesToRemove) {
            fileProgress.remove(fileToRemove.getKey());
        }
    }
}
