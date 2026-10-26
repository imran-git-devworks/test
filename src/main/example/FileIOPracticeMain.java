package src.main.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class FileIOPracticeMain {

    public static void main(String[] args) throws InterruptedException {

        Path path1 = Paths.get("test1.txt");
        Path path2 = Paths.get("test2.txt");
        Path path3 = Paths.get("test3.txt");
        // readFile(path);

      /*  Runnable task1 = () -> readFile(path1);
        ;
        Thread t1 = Thread.ofPlatform().start(task1);
        Runnable task2 = () -> readFile(path2);
        Thread t2 = Thread.ofPlatform().start(task2);

        Runnable task3 = () -> writeFile(path3, "");
        Runnable task4 = () -> writeFile(path3, "");
        t1.join();*/

        List<String> result = new ArrayList<>();
        List<String> content1 = readFileInChunks(path1);
        List<String> content2 = readFileInChunks(path2);
        result = IntStream.range(0, content1.size()).mapToObj(i -> content1.get(i) + content2.get(i)).toList();
        result.stream().forEach(System.out::println);
        writeFileInChunks(path3, result);
    }

    public static void readFile(Path path) {
        try {
            String fileContent = Files.readString(path);
            System.out.println(fileContent);


        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public static void writeFile(Path path, CharSequence content) {
        try {
            Files.writeString(path, content);

        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public static List<String> readFileInChunks(Path path) {
        List<String> content = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader((path))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.add(line);
            }
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        return content;
    }

    public static void writeFileInChunks(Path path, List<String> content) {

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


}
