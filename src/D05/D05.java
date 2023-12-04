package D05;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class D05 {
    public D05(String[] args) {
        main(args);
    }


    private static void getLines(ArrayList<String> lines) {
        try {
            File myObj = new File("src/D05/input.txt");
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

    static class Interval {
        long destStart;
        long sourceStart;
        long length;

        public Interval(long destStart, long sourceStart, long length) {
            this.destStart = destStart;
            this.sourceStart = sourceStart;
            this.length = length;
        }

        @Override
        public String toString() {
            return "Interval{" +
                    "destStart=" + destStart +
                    ", sourceStart=" + sourceStart +
                    ", length=" + length +
                    '}' + "\n";
        }
    }

    private void parse(ArrayList<String> lines, List<Long> seeds, List<ArrayList<Interval>> maps) {
        lines.removeIf(
                line -> line.length() == 0
        );

        // Read seeds
        int idLine = 0;

        // seeds:
        String line = lines.get(idLine++);

        // 79 14 55 13
        seeds.addAll(
                Arrays.stream(line.split(" "))
                        .map((String s) -> {
                            try {
                                return Long.parseLong(s);
                            } catch (NumberFormatException e) {
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .toList()
        );


        // seed-to-soil map:
        ++idLine;


        for (int idMap = 0; idMap < 7; ++idMap) {
            maps.add(new ArrayList<>());
        }

        int idMap = 0;
        while (idLine < lines.size()) {
            line = lines.get(idLine++);

            // empty line
            if (line.length() == 0) {
                idMap++;
                continue;
            }

            // fertilizer-to-water map:
            if (line.contains(":")) {
                idMap++;
                continue;
            }

            // 0 15 37
            List<Long> Longs =
                    Arrays.stream(line.split(" "))
                            .map((String s) -> {
                                try {
                                    return Long.parseLong(s);
                                } catch (NumberFormatException e) {
                                    return null;
                                }
                            })
                            .filter(Objects::nonNull)
                            .toList();

            maps.get(idMap)
                    .add(new Interval(
                            Longs.get(0),
                            Longs.get(1),
                            Longs.get(2))
                    );

        }
    }

    private void main(String[] args) {

        ArrayList<String> lines = new ArrayList<>();
        getLines(lines);

        List<Long> seeds = new ArrayList<>();
        List<ArrayList<Interval>> maps = new ArrayList<>();

        parse(lines, seeds, maps);

        System.out.println("seeds: " + seeds);
        System.out.println("maps: " + maps);

        long min = Long.MAX_VALUE;

        for (var value : seeds) {
            for (var map : maps) {
                long oldValue = value;

                if (oldValue == 53) {
                    System.out.println("debug");
                }

                for (var interval : map) {
                    long sourceStart = interval.sourceStart;
                    long sourceEnd = interval.sourceStart + interval.length;

                    if (oldValue >= sourceStart && oldValue <= sourceEnd) {
                        long destStart = interval.destStart;
                        long offset = oldValue - sourceStart;

                        value = destStart + offset;
                        break;
                    }

                }
                System.out.println(oldValue + " -> " + value);
            }
            System.out.println(value);
            min = Math.min(min, value);
            System.out.println();
        }

        System.out.println(min);
    }


}
