import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ClientLog {
    private ArrayList<int[]> choice = new ArrayList<>();

    public void log(int productNum, int amount) {
        this.choice.add(new int[]{productNum, amount});
    }

    public void exportAsCSV(File file) throws IOException {
        if (file.exists()) {
            try (CSVWriter writer = new CSVWriter(new FileWriter(file,true))) {
                for (int[] currentChoice : choice) {
                    writer.writeNext(Arrays.stream(currentChoice)
                            .mapToObj(String::valueOf)
                            .toArray(String[]::new));
                }
            }
        }
        else {
            file.createNewFile();
            try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
                writer.writeNext(new String[]{"productNum", "amount"});
                for (int[] currentChoice : choice) {
                    writer.writeNext(Arrays.stream(currentChoice)
                            .mapToObj(String::valueOf)
                            .toArray(String[]::new));
                }
            }
        }
    }
}
