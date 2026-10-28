package src.main.example.threadMain;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.IntStream;

public class FileReadWriteTaskMain {

    private static final Map<String, List<String>> fileStorage = new ConcurrentHashMap<>();

    public static void main(String[] args) {

        int workerThreadCount = 2;
        String file1Path = "test1.txt";
        String file2Path = "test2.txt";


        Runnable barrierAction = () -> {
            System.out.println(Thread.currentThread().getName() + " - " + "Cyclic Barrier Action starting for the second phase of writing to the file!!");
            writeToFile();
        };

        CyclicBarrier cyclicBarrier = new CyclicBarrier(workerThreadCount, barrierAction);
        Thread t1 = new Thread(new FileReaderWriterTask(file1Path, fileStorage, cyclicBarrier));
        Thread t2 = new Thread(new FileReaderWriterTask(file2Path, fileStorage, cyclicBarrier));
        t1.setName("Thread-1");
        t2.setName("Thread-2");
        t1.start();
        t2.start();

    }

    public static void writeToFile() {
        String outputFile = "test3.txt";
        String file1Path = "test1.txt";
        String file2Path = "test2.txt";
        Path path = Paths.get("test3.txt");

        List<String> file1Content = fileStorage.get(file1Path);
        List<String> file2Content = fileStorage.get(file2Path);
        int size = Math.min(file1Content.size(), file2Content.size());
        List<String> mergedList = IntStream.range(0, size).mapToObj(i -> file1Content.get(i) + file2Content.get(i)).toList();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            for (String line : mergedList) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
