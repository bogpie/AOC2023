package D04;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Math.min;

public class D04 {

    int SIZE = 223;

    public D04(String[] args) {
        main(args);
    }

    private static void getLines(ArrayList<String> lines) {
        try {
            File myObj = new File("src/D04/input.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                lines.add(line);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void main(String[] args) {
        ArrayList<String> lines = new ArrayList<>();
        getLines(lines);

        long[] noDuplicates = new long[250];
        for (int i = 0; i < SIZE; ++i) {
            noDuplicates[i] = 1;
        }

        long sum = 0;
        long sum2 = 0;
        int idLine = 0;
        for (String line : lines) {
            String[] sets = line.split("[:|]+");

            int[] freq = new int[100];

            String[] plays = sets[1].split(" ");
            String[] wins = sets[2].split(" ");

            for (String play : plays) {
                int number;
                try {
                    number = Integer.parseInt(play);

                } catch (NumberFormatException e) {
                    continue;
                }

                freq[number] = 1;
            }

            for (String win : wins) {
                int number;
                try {
                    number = Integer.parseInt(win);

                } catch (NumberFormatException e) {
                    continue;
                }

                if (freq[number] == 1) {
                    freq[number] = 2;
                }
            }

            long power = 0;
            for (int i = 0; i < 100; ++i) {
                if (freq[i] == 2) {
                    ++power;
                }
            }

            --power;
            if (power < 0) {
                ++idLine;
                continue;
            }

            long score = 1L << power;
            long multiplier = noDuplicates[idLine];

            int start = idLine + 1;
            int end = min((int) (idLine + power + 1), SIZE);

            for (int idIncremented = start; idIncremented <= end; ++idIncremented) {
                noDuplicates[idIncremented] += multiplier;
            }

            sum += score;

            ++idLine;
        }

        for (int i = 0; i < idLine; ++i) {
            sum2 += noDuplicates[i];
        }

        System.out.println(sum);
        System.out.println(sum2);
    }
}
