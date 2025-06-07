package xujiejie;

import java.io.FileReader;

import com.opencsv.CSVReader;

public class Regression {
    
    public static void main(String[] args) {

        
        try (CSVReader reader = new CSVReader(new FileReader("d:\\program\\Learning\\learning\\src\\main\\resources\\Case1.csv"))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                // Assuming the first column is the feature and the second is the target
 
                
                // Here you would implement your regression logic
                System.out.println("Feature: " + nextLine[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
