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

        public long getSourceStart() {
            return sourceStart;
        }

        public long getDestStart() {
            return destStart;
        }

        public long getSourceEnd() {
            return sourceStart + length - 1;
        }

        public long getDestEnd() {
            return destStart + length - 1;
        }

        public long getLength() {
            return length;
        }

        public Long getDiff() {
            return getDestStart() - getSourceStart();
        }

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
        long currentMap;

        public SeedInterval(long start, long end) {
            this.start = start;
            this.length = end - start + 1;
            this.currentMap = 0;
        }

        public SeedInterval(long start, long end, long currentMap) {
            this.start = start;
            this.length = end - start + 1;
            this.currentMap = currentMap;
        }

        public long getStart() {
            return start;
        }

        public long getLength() {
            return length;
        }

        public long getEnd() {
            return start + length - 1;
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


        for (long idMap = 0; idMap < 7; ++idMap) {
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
                            seeds.get(idSeed) + seeds.get(idSeed + 1) - 1
                    )
            );
        }
        Queue<SeedInterval> queue = new LinkedList<>(seedIntervals);

        ArrayList<SeedInterval> finalSeeds = new ArrayList<>();

        while (!queue.isEmpty()) {
            SeedInterval seed = queue.poll();
            if (seed.currentMap == maps.size()) {
                finalSeeds.add(seed);
                continue;
            }

            ArrayList<Interval> map = maps.get((int) seed.currentMap);

            boolean intersects = false;
            for (var interval : map) {
                // Check if intersects

                long newSeedStart = seed.getStart() + interval.getDiff();
                long newSeedEnd = seed.getEnd() + interval.getDiff();

                // ....(.........[.......)........]........
                if (seed.getStart() < interval.getSourceStart() && interval.getSourceStart() < seed.getEnd() && seed.getEnd() <= interval.getSourceEnd()) {
                    queue.addAll(List.of(
                            new SeedInterval(
                                    seed.getStart(),
                                    interval.getSourceStart() - 1,
                                    seed.currentMap + 1
                            ),
                            new SeedInterval(
                                    interval.getDestStart(),
                                    newSeedEnd,
                                    seed.currentMap + 1
                            )
                    ));
                    intersects = true;
                    break;
                }
                // ....[.........(.......]........)........
                else if (interval.getSourceStart() < seed.getStart() && seed.getStart() < interval.getSourceEnd() && interval.getSourceEnd() <= seed.getEnd()) {
                    queue.addAll(List.of(
                            new SeedInterval(
                                    newSeedStart,
                                    interval.getDestEnd() - 1,
                                    seed.currentMap + 1
                            ),
                            new SeedInterval(
                                    interval.getSourceEnd(),
                                    seed.getEnd(),
                                    seed.currentMap + 1
                            )
                    ));
                    intersects = true;
                    break;
                }
                // ....[.........(.......)........]........
                else if (interval.getSourceStart() < seed.getStart() && seed.getStart() <= interval.getSourceEnd()) {
                    queue.add(
                            new SeedInterval(
                                    newSeedStart,
                                    newSeedEnd,
                                    seed.currentMap + 1
                            )
                    );
                    intersects = true;
                    break;
                }
                // ....(.........[.......]........)........
                else if (seed.getStart() < interval.getSourceStart() && interval.getSourceEnd() <= seed.getEnd()) {
                    queue.addAll(List.of(
                            new SeedInterval(
                                    seed.getStart(),
                                    interval.getSourceStart() - 1,
                                    seed.currentMap + 1
                            ),
                            new SeedInterval(
                                    interval.getDestStart(),
                                    interval.getDestEnd() - 1,
                                    seed.currentMap + 1
                            ),
                            new SeedInterval(
                                    interval.getSourceEnd(),
                                    seed.getEnd(),
                                    seed.currentMap + 1
                            )
                    ));
                    intersects = true;
                    break;
                }
            }
            if (!intersects) {
                seed = new SeedInterval(seed.getStart(), seed.getEnd(), seed.currentMap + 1);
                queue.add(seed);
            }
        }

        // Get min
        min = Long.MAX_VALUE;
        for (var seed : finalSeeds) {
            min = Math.min(min, seed.getStart());
        }
        System.out.println(min);

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
