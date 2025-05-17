import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) {
        String directory = "C:\\Users\\pc\\Downloads\\Directory";
        List<String> files = getAllFiles(directory);

        System.out.println("Files\t#words\t#is\t\t#are\t#you\t#Longest\t\t#Shortest");
        ExecutorService executor = Executors.newFixedThreadPool(files.size());
        List<Future<Integer>> futures = new ArrayList<>();
        for(String file : files){
            Future future = executor.submit(() -> {try { process_text_files(file);}
            catch (IOException e) {throw new RuntimeException(e);}});
            futures.add(future);
        }




    }

    public static List<String> getAllFiles(String directoryPath) {
        List<String> filePaths = new ArrayList<>();
        File directory = new File(directoryPath);

        // Check if the directory exists and is a directory
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    filePaths.add(file.getAbsolutePath());
                }
            }
        }
        return filePaths;
    }

    public static String readText(String file_path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file_path));
        StringBuilder content = new StringBuilder();
        String line;

        while((line = reader.readLine())!=null){
            content.append(line).append(System.lineSeparator());
        }reader.close();
        String text = content.toString();
        return text;
    }

    public static int[] getTextData(String file_path) throws IOException {
        int[] arr = new int[4];//0 = words, 1 = is, 2 = are, 3 = you

        String text = readText(file_path); //read_text(file_path);

        String[] words = text.split("\\s+");
        arr[0] = words.length;

        for (String word:words) {
            if(Objects.equals(word, "is"))
                arr[1]++;
            else if(Objects.equals(word, "are"))
                arr[2]++;
            else if(Objects.equals(word, "you"))
                arr[3]++;
        }
        return arr;
    }

    public static String[] getWords(String file_path) throws IOException {
        String[] array = {"","                      ",""};//0 = file name, 1 = shortest word, 2 = longest word;
        String[] file = file_path.split("\\\\");
        int x = file.length;
        array[0] = file[x-1];
        int shortest = 0;

        String text = readText(file_path);
        String[] words = text.split("\\s+");

        for (String word :words) {
            if(word.length()>=array[2].length())
                array[2] = word;
            else if (word.length()<=array[1].length() && word.length()>3)
                array[1] = word;
        }

        return array;
    }

    public static void process_text_files(String file_path) throws IOException {

        String[] array = getWords(file_path);//0 = file name, 1 = shortest word, 2 = longest word;
        int[] array1 = getTextData(file_path);//0 = number of words, 1 = is, 2 = are, 3 = you
        System.out.println(array[0] + "\t" + array1[0] + "\t" + array1[1] + "\t\t" + array1[2] + "\t\t" + array1[3] + "\t\t" + array[2] + "\t\t" + array[1]);
    }

}