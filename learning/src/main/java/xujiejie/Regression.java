package xujiejie;

import com.opencsv.CSVReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Regression {
    public static void main(String[] args) {

        InputStream inputStream = Regression.class.getClassLoader().getResourceAsStream("Case1.csv");
        
        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
