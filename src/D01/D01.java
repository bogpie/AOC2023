package D01;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class D01 {
    public D01(String[] args) {
        main(args);
    }

    public static void main(String[] args) {

        int sum = 0;

        sum = getSumPart1(sum);
        System.out.println(sum);


        sum = 0;
        sum = getSumPart2(sum);
        System.out.println(sum);
    }

    private static int getSumFromLine(int sum, String data) {
        int number = 0;
        for (int idChr = 0; idChr < data.length(); ++idChr) {
            char chr = data.charAt(idChr);
            if (chr >= '0' && chr <= '9') {
                number = chr - '0';
                break;
            }
        }

        for (int idChr = data.length() - 1; idChr >= 0; --idChr) {
            char chr = data.charAt(idChr);
            if (chr >= '0' && chr <= '9') {
                number = number * 10 + (chr - '0');
                break;
            }
        }
        sum += number;
        return sum;
    }

    private static int getSumPart1(int sum) {
        try {
            File myObj = new File("src/D01/input.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();

                sum = getSumFromLine(sum, data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return sum;
    }


    private static int getSumPart2(int sum) {
        try {
            File myObj = new File("src/D01/input.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();

                data = data.replaceAll("oneight", "18");
                data = data.replaceAll("threeight", "38");
                data = data.replaceAll("sevenine", "79");
                data = data.replaceAll("eightwo", "82");
                data = data.replaceAll("twone", "21");

                data = data.replaceAll("zero", "0");
                data = data.replaceAll("one", "1");
                data = data.replaceAll("two", "2");
                data = data.replaceAll("three", "3");
                data = data.replaceAll("four", "4");
                data = data.replaceAll("five", "5");
                data = data.replaceAll("six", "6");
                data = data.replaceAll("seven", "7");
                data = data.replaceAll("eight", "8");
                data = data.replaceAll("nine", "9");

                sum = getSumFromLine(sum, data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return sum;
    }


}
