package D03;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class D03 {
    public D03(String[] args) {
        main(args);
    }

    private static void getLines(ArrayList<String> lines) {
        try {
            File myObj = new File("src/D03/input.txt");
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

    private long findFirstSum(ArrayList<String> lines) {
        long sum = 0;
        int idLine = 0;
        for (String line : lines) {
            int number = 0;
            int left = -1;
            boolean foundAdjacent = false;
            for (int idCol = 0; idCol < line.length(); ++idCol) {
                char chr = line.charAt(idCol);
                if (isDigit(chr)) {
                    int digit = chr - '0';
                    number = number * 10 + digit;

                    if (left == -1) {
                        left = idCol;
                    }

                    if (!foundAdjacent) {
                        foundAdjacent = isAdjacent(lines, idLine, idCol);
                    }
                } else {
                    if (number != 0) {
                        if (foundAdjacent) {
                            sum += number;
                        }
                    }
                    number = 0;
                    left = -1;
                    foundAdjacent = false;
                }
            }

            if (number != 0) {
                if (foundAdjacent) {
                    sum += number;
                }
            }
            ++idLine;
        }
        return sum;
    }

    private boolean isDigit(char chr) {
        return chr >= '0' && chr <= '9';
    }

    private boolean isSymbol(char chr) {
        return !isDigit(chr) && chr != '.';
    }

    private boolean isAdjacent(ArrayList<String> lines, int initialLine, int initialCol) {

        for (int idLine = initialLine - 1; idLine <= initialLine + 1; ++idLine) {
            for (int idCol = initialCol - 1; idCol <= initialCol + 1; ++idCol) {
                if (isInvalid(lines, initialLine, initialCol, idLine, idCol)) continue;

                char chr = lines.get(idLine).charAt(idCol);
                if (isSymbol(chr)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isInvalid(ArrayList<String> lines, int initialLine, int initialCol, int idLine, int idCol) {
        if (
                idLine < 0 ||
                        idLine >= lines.size() ||
                        idCol < 0 ||
                        idCol >= lines.get(0).length()
        ) {
            return true;
        }

        return idLine == initialLine && idCol == initialCol;
    }

    private boolean getIsGear(ArrayList<String> lines, int initialLine, int initialCol, AtomicInteger ratio) {
        int noNumbers = 0;
        for (int idLine = initialLine - 1; idLine <= initialLine + 1; ++idLine) {
            for (int idCol = initialCol - 1; idCol <= initialCol + 1; ++idCol) {
                if (isInvalid(lines, initialLine, initialCol, idLine, idCol)) continue;

                char chr = lines.get(idLine).charAt(idCol);
                if (isDigit(chr)) {
                    int number = markNumber(lines, idLine, idCol);
                    ++noNumbers;
                    if (noNumbers == 3) {
                        return false;
                    }
                    ratio.set(ratio.get() * number);
                }
            }
        }

        return noNumbers == 2;
    }

    private int markNumber(ArrayList<String> lines, int idLine, int initialCol) {
        int left;
        for (left = initialCol - 1; left >= 0; --left) {
            char chr = lines.get(idLine).charAt(left);
            if (!isDigit(chr)) {
                break;
            }
        }
        ++left;

        int right;
        for (right = initialCol + 1; right < lines.get(idLine).length(); ++right) {
            char chr = lines.get(idLine).charAt(right);
            if (!isDigit(chr)) {
                break;
            }
        }
        --right;

        int number = 0;
        for (int idCol = left; idCol <= right; ++idCol) {
            char chr = lines.get(idLine).charAt(idCol);
            int digit = chr - '0';
            number = number * 10 + digit;
        }
        String oldLine = lines.get(idLine);
        String newLine = oldLine.substring(0, left) + "a".repeat(right - left + 1) + oldLine.substring(right + 1);
        lines.set(idLine, newLine);

        return number;
    }

    @SuppressWarnings("unchecked")
    private long findSecondSum(ArrayList<String> lines) {
        int idLine = 0;
        long sum = 0;
        for (String line : lines) {
            for (int idCol = 0; idCol < line.length(); ++idCol) {
                char chr = line.charAt(idCol);
                if (isStar(chr)) {
                    ArrayList<String> copy;
                    copy = (ArrayList<String>) lines.clone();

                    AtomicInteger ratio = new AtomicInteger(1);
                    boolean isGear = getIsGear(
                            lines,
                            idLine,
                            idCol,
                            ratio
                    );
                    if (isGear) {
                        sum += ratio.get();
                    }

                    lines = (ArrayList<String>) copy.clone();
                }
            }
            ++idLine;
        }

        return sum;
    }

    private boolean isStar(char chr) {
        return chr == '*';
    }

    public void main(String[] args) {
        ArrayList<String> lines = new ArrayList<>();
        getLines(lines);

        long sum = findFirstSum(lines);
        System.out.println(sum);

        sum = findSecondSum(lines);
        System.out.println(sum);

    }

}
