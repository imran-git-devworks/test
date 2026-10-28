package src.main.example.threadMain;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class FileReaderWriterTask implements Runnable {
    private final CyclicBarrier cyclicBarrier;
    private final String filePath;
    private final Map<String, List<String>> fileStorage;

    public FileReaderWriterTask(String filePath, Map<String, List<String>> fileStorage, CyclicBarrier cyclicBarrier) {
        this.filePath = filePath;
        this.fileStorage = fileStorage;
        this.cyclicBarrier = cyclicBarrier;
    }

    public void readFile() {
        StringBuilder content = new StringBuilder();
        List<String> fileContentValues = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                fileContentValues.add(line);
            }
            fileStorage.put(filePath, fileContentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        readFile();
        System.out.println(Thread.currentThread().getName() + " completed reading content " + filePath);
        try {
            System.out.println(Thread.currentThread().getName() + " now calling await()!");
            cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException ie) {
            ie.printStackTrace();
        }
    }//end run()
}//end file
