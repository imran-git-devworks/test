package src.main.example.threadMain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.IntStream;

public class FileReaderWriterTask implements Runnable {
    private static int workerThread = 2;
    private static Path filePath;
    private static CyclicBarrier cyclicBarrier;
    private static final Path path1 = Paths.get("test1.txt");
    private static final Path path2 = Paths.get("test2.txt");
    private static final Path path3 = Paths.get("test3.txt");
    private static List<String> contentFile1 = new ArrayList<>();
    private static List<String> contentFile2 = new ArrayList<>();
    private static List<String> mergedContent = new ArrayList<>();

    public FileReaderWriterTask(Path filePath, CyclicBarrier cyclicBarrier) {
        this.filePath = filePath;
        this.cyclicBarrier = cyclicBarrier;
    }

    public static void main(String[] args) {
        Runnable barrierAction = () -> {
            System.out.println(Thread.currentThread().getName() + " - " +"Cyclic Barrier Action starting for the second phase of writing to the file!!");
            writeToFile(path3, mergedContent);
        };
        cyclicBarrier = new CyclicBarrier(workerThread, barrierAction);
        Thread t1 = new Thread(new FileReaderWriterTask(path1,  cyclicBarrier));
        Thread t2 = new Thread(new FileReaderWriterTask(path2,  cyclicBarrier));
        t1.setName("Thread-1");
        t2.setName("Thread-2");
        t1.start();
        t2.start();
        mergedContent = IntStream.range(0, contentFile1.size()).mapToObj( i -> contentFile1.get(i) + contentFile2.get(i)).toList();
       // mergedContent.stream().forEach(System.out::println);
    }

    public static List<String> readFile(Path path) {
        List<String> temp = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader((path))) {
            String line;
            while ((line = br.readLine()) != null) {
                temp.add(line);
            }
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        return temp;
    }
    public static void writeToFile(Path path, List<String> content) {
        try (BufferedWriter bw = Files.newBufferedWriter((path))) {
            content.stream().forEachOrdered(line -> {
                try {
                    bw.write(line);
                    bw.newLine();
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            });
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void run() {
       contentFile1= readFile(path1);
        System.out.println(Thread.currentThread().getName() + " reading content 1");
       contentFile2 = readFile(path2);
        System.out.println(Thread.currentThread().getName() + " reading content 2");
        mergedContent = IntStream.range(0, contentFile1.size()).mapToObj( i -> contentFile1.get(i) + contentFile2.get(i)).toList();
        try {
            System.out.println(Thread.currentThread().getName() + " completed reading now calling await()!");
            cyclicBarrier.await();
            System.out.println(Thread.currentThread().getName() + " " +"Started writing to the file!");
        } catch (InterruptedException | BrokenBarrierException ie) {
            ie.printStackTrace();
        }
    }//end run()
}//end file
