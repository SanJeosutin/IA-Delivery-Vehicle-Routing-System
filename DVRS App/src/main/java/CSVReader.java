import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CSVReader {
    public CSVReader() {
    }

    public List<List<String>> readFile(String filePath) throws IOException {
        String row;
        List<List<String>> result = null;
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        while ((row = reader.readLine()) != null) {
            List<String> data = Arrays.asList(row.split(","));
            result.add(data);
        }

        if (reader != null) {
            reader.close();
        }

        return result;
    }

}
