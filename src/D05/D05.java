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

    static class SeedInterval {
        long start;
        long length;

        public SeedInterval(long start, long length) {
            this.start = start;
            this.length = length;
        }

        @Override
        public String toString() {
            return "SeedInterval{" +
                    "start=" + start +
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

        long min = partOne(seeds, maps);
        System.out.println(min);

        List<SeedInterval> seedIntervals = new ArrayList<>();

        for (int idSeed = 0; idSeed < seeds.size() - 1; idSeed += 2) {
            seedIntervals.add(
                    new SeedInterval(
                            seeds.get(idSeed),
                            seeds.get(idSeed + 1)
                    )
            );
        }

        int seedIntervalId = 0;


        for (seedIntervalId = 0; seedIntervalId < seedIntervals.size(); ++seedIntervalId) {
            var seedInterval = seedIntervals.get(seedIntervalId);
            var seedStart = seedInterval.start;
            var seedEnd = seedInterval.start + seedInterval.length;

            seedIntervals.remove(seedInterval);
            seedIntervalId--;

            for (var map : maps) {
                for (var interval : map) {
                    var sourceStart = interval.sourceStart;
                    var sourceEnd = interval.sourceStart + interval.length;

                    var destStart = interval.destStart;
                    var destEnd = interval.destStart + interval.length;

                    // (.....[....).....]
                    seedIntervals.remove(seedInterval);
                    if (seedStart <= sourceStart && sourceStart <= seedEnd && seedEnd <= sourceEnd) {
                        seedIntervals.addAll(
                                List.of(
                                        new SeedInterval(seedStart, sourceStart - seedStart),
                                        new SeedInterval(destStart, seedEnd - sourceStart)
                                )
                        );
                    }
                    // [.....(....].....)
                    else if (sourceStart <= seedStart && seedStart <= sourceEnd && sourceEnd <= seedEnd) {
                        seedIntervals.addAll(
                                List.of(
                                        new SeedInterval(destStart, sourceEnd - seedStart),
                                        new SeedInterval(seedEnd, seedEnd - sourceEnd)
                                )
                        );
                    }
                    // [.....(....).....]
                    else if (sourceStart <= seedStart && seedEnd <= sourceEnd) {
                        seedIntervals.add(
                                new SeedInterval(destStart, seedEnd - seedStart)
                        );
                    }
                    // (.....[....].....)
                    else if (seedStart <= sourceStart && sourceEnd <= seedEnd)
                        seedIntervals.addAll(
                                List.of(
                                        new SeedInterval(seedStart, sourceStart - seedStart),
                                        new SeedInterval(destStart, sourceEnd - sourceStart),
                                        new SeedInterval(seedEnd, seedEnd - sourceEnd)
                                )
                        );
                }
            }
        }

        // Find min of start of seedIntervals
        long partTwo = seedIntervals.stream()
                .mapToLong(seedInterval -> seedInterval.start)
                .min()
                .orElseThrow();

    }


    private static long partOne(List<Long> seeds, List<ArrayList<Interval>> maps) {
        long min;
        min = Long.MAX_VALUE;

        for (var value : seeds) {
            for (var map : maps) {
                long oldValue = value;

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
            }
            min = Math.min(min, value);
        }
        return min;
    }
}
