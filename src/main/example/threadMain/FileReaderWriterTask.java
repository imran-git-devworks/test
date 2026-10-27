package src.main.example.threadMain;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class FileReaderWriterTask implements Runnable {
    private final CyclicBarrier cyclicBarrier;
    private final String filePath;
    private final Map<String, String> fileStorage;

    public FileReaderWriterTask(String filePath, Map<String, String> fileStorage, CyclicBarrier cyclicBarrier) {
        this.filePath = filePath;
        this.fileStorage = fileStorage;
        this.cyclicBarrier = cyclicBarrier;
    }

    public void readFile() {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\'");
            }
            fileStorage.put(filePath, content.toString());

            cyclicBarrier.await();
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
