package D02;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class D02 {
    public D02(String[] args) {
        main(args);
    }

    public void main(String[] args) {
        ArrayList<String> lines = new ArrayList<>();
        getLines(lines);

        int LIMIT_RED = 12;
        int LIMIT_GREEN = 13;
        int LIMIT_BLUE = 14;

        int idGame = 0;
        int firstSum = 0;
        long secondSum = 0;
        for (String line : lines) {
            ++idGame;

            boolean isPossible = true;
            String[] sets = line.split(";");

            long maxRed = 0, maxGreen = 0, maxBlue = 0;
            for (String set : sets) {
                int red = 0, green = 0, blue = 0;

                String[] words = set.split(" ");

                int number = 0;
                for (String word : words) {
                    if (word.length() == 0) {
                        continue;
                    }
                    if (Objects.equals(word, "Game") || word.charAt(word.length() - 1) == ':') {
                        continue;
                    }

                    if (!word.contains("blue") && !word.contains("red") && !word.contains("green")) {
                        number = Integer.parseInt(word);
                        continue;
                    }

                    if (word.contains("blue")) {
                        blue = number;
                    } else if (word.contains("red")) {
                        red = number;
                    } else if (word.contains("green")) {
                        green = number;
                    }
                }

                maxRed = Math.max(red, maxRed);
                maxGreen = Math.max(green, maxGreen);
                maxBlue = Math.max(blue, maxBlue);

                if ((blue > LIMIT_BLUE || green > LIMIT_GREEN || red > LIMIT_RED) && isPossible) {
                    firstSum += idGame;
                    isPossible = false;
                }
            }
            secondSum += (maxRed * maxGreen * maxBlue);
        }

        firstSum = ((idGame) * (idGame + 1) / 2) - firstSum;
        System.out.println(firstSum);
        System.out.println(secondSum);

    }

    private static void getLines(ArrayList<String> lines) {
        try {
            File myObj = new File("src/D02/input.txt");
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

}
