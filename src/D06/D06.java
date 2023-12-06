package D06;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class D06 {
    public D06(String[] args) {
        main(args);
    }


    private static void getLines(ArrayList<String> lines) {
        try {
            File myObj = new File("src/D06/input.txt");
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

        List<List<Long>> inputArr = new ArrayList<>();
        List<Long> durations = new ArrayList<>();
        List<Long> records = new ArrayList<>();

        parse(lines, inputArr, durations, records);

        // dist(Hold)     = hold * (duration - Hold) = -Hold^2 + duration * Hold
        // dist(Hold)     > record
        // func(Hold)     = -1 * Hold^2 + duration * Hold - record > 0
        // a = -1, b = duration, c = -record
        // delta          = duration^2 - 4 * record
        // Hold_intersect = (-duration +- sqrt(duration^2 - 4 * record)) / (-2)
        // Hold_intersect = (duration +- sqrt(duration^2 - 4 * record)) / 2
        // Hold_intersecct_min < Hold_breaking < Hold_intersect_max


        long product = 1L;
        product = calculateProduct(durations, records, product);
        System.out.println(product);

        Long newDuration = Long.parseLong(
                durations.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining()
                        )
        );

        Long newRecord = Long.parseLong(
                records.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining()
                        )
        );

        durations.clear();
        durations.add(newDuration);

        records.clear();
        records.add(newRecord);

        product = 1L;

        product = calculateProduct(durations, records, product);

        System.out.println(product);
    }

    private static Long calculateProduct(List<Long> durations, List<Long> records, Long product) {
        for (int i = 0; i < durations.size(); i++) {
            Long duration = durations.get(i);
            Long record = records.get(i);

            // delta          = duration^2 + 4 * record
            double delta = Math.pow(duration, 2) - 4 * record;

            // holdIntersect = (duration +- sqrt(duration^2 + 4 * record)) / 2
            double holdIntersectMinDouble = (duration - Math.sqrt(delta)) / 2;
            double holdIntersectMaxDouble = (duration + Math.sqrt(delta)) / 2;
            if (holdIntersectMinDouble > holdIntersectMaxDouble) {
                double temp = holdIntersectMinDouble;
                holdIntersectMinDouble = holdIntersectMaxDouble;
                holdIntersectMaxDouble = temp;
            }

            Long holdIntersectMin = (long) Math.floor(holdIntersectMinDouble);
            Long holdIntersectMax = (long) Math.floor(holdIntersectMaxDouble);

            long distMin = holdIntersectMin * (duration - holdIntersectMin);
            long distMax = holdIntersectMax * (duration - holdIntersectMax);

            if (distMin <= record) {
                ++holdIntersectMin;
            }

            if (distMax <= record) {
                --holdIntersectMax;
            }

            long diff = holdIntersectMax - holdIntersectMin + 1;
            if (diff < 0L) {
                diff = 0L;
            }
            product *= diff;
        }
        return product;
    }

    private static void parse(ArrayList<String> lines, List<List<Long>> inputArr, List<Long> durations, List<Long> records) {
        for (String line : lines) {
            // split by ":" and " "
            List<String> split = Arrays.stream(line.split("[: ]")).toList();

            // remove if not a number
            split = split.stream().filter(s -> {
                try {
                    Integer.parseInt(s);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }).collect(toList());

            inputArr.add(split
                    .stream().map(Long::parseLong)
                    .collect(toList())
            );
        }

        durations.addAll(inputArr.get(0));
        records.addAll(inputArr.get(1));
    }
}
