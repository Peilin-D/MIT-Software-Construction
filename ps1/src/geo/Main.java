/* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package geo;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    /** Path to file with example points of interest. */
    static final String EXAMPLES_PATH = "src/geo/example-points.csv";

    /** Approximate bounding box for Cambridge, MA. */
    static final List<Angle> CAMBRIDGE_AREA = Arrays.asList(
            new Angle(42, 24, 15, CardinalDirection.NORTH),
            new Angle(71, 3, 56, CardinalDirection.WEST),
            new Angle(42, 21, 9, CardinalDirection.NORTH),
            new Angle(71, 9, 37, CardinalDirection.WEST));

    /**
     * Main method. Prints information about some example points of interest.
     * 
     * @param args command-line arguments, unused
     * @throws IOException if the example points of interest file cannot be read
     */
    public static void main(final String[] args) throws IOException {
        final List<PointOfInterest> pointsOfInterest = Collections.unmodifiableList(readPOIsFromFile(EXAMPLES_PATH));
        final Random random = new Random();

        final PointOfInterest first = pointsOfInterest.get(random.nextInt(pointsOfInterest.size()));
        System.out.println(first + "\n  in degrees = "
                + Angular.toDegrees(first.latitude()) + ", " + Angular.toDegrees(first.longitude()));

        final PointOfInterest second = pointsOfInterest.get(random.nextInt(pointsOfInterest.size()));
        System.out.println("From " + first + " to " + second + "\n  displacement = "
                + Angular.displacement(first.latitude(), second.latitude()) + ", "
                + Angular.displacement(first.longitude(), second.longitude()));

        final Set<PointOfInterest> mit = new HashSet<>();
        for (final PointOfInterest poi : pointsOfInterest) {
            if (poi.name().contains("MIT")) { mit.add(poi); }
        }
        System.out.println("Bounding box for MIT POIs =\n  " + Bounds.boundingBox(mit));

        System.out.println("POIs in Cambridge area =");
        for (final PointOfInterest poi : Bounds.inBoundingBox(new HashSet<>(pointsOfInterest), CAMBRIDGE_AREA)) {
            System.out.println("  " + poi);
        }

        final Map<String, Set<String>> categoryKeywords = new HashMap<>();
        categoryKeywords.put("Center", Collections.emptySet());
        categoryKeywords.put("Tech", Collections.singleton("MIT"));
        categoryKeywords.put("School", new HashSet<>(Arrays.asList("school", "institute", "student")));
        final String search = "School";
        System.out.println(search + " POIs =");
        for (final PointOfInterest poi : pointsOfInterest) {
            final Set<String> categories = Mapping.findCategories(poi, categoryKeywords);
            if (categories.stream().anyMatch(category -> category.equalsIgnoreCase(search))) {
                System.out.println("  " + poi.name() + " - " + poi.description() + " " + categories);
            }
        }

        System.out.println("Reducing duplicates =");
        final Map<PointOfInterest, List<PointOfInterest>> deduped = Mapping.reduceDuplicates(pointsOfInterest);
        for (final PointOfInterest primary : deduped.keySet()) {
            final int more = deduped.get(primary).size();
            System.out.println("  " + primary.name() + (more > 0 ? " (plus " + more + " more)" : ""));
        }
    }

    /**
     * @param file name of file where each line describes a point of interest as
     *            defined by {@link #parsePOI}
     * @return the points of interest described in file
     * @throws IOException if the points of interest file cannot be read
     */
    static List<PointOfInterest> readPOIsFromFile(final String file) throws IOException {
        return Files.lines(new File(file).toPath())
                .map(line -> parsePOI(line))
                .collect(Collectors.toList());
    }

    /**
     * @param line comma-separated list of: non-empty name, description,
     *            latitude components, and longitude components; where name and
     *            description cannot contain newlines or commas, the angle
     *            components are as defined by {@link #makeAngle}, and leading
     *            and trailing whitespace in each field is ignored
     * @return a point of interest with the parameters from line
     */
    static PointOfInterest parsePOI(final String line) {
        final int parameterCount = 10;
        final List<String> parameters = Arrays.asList(line.split(","));
        assert parameters.size() == parameterCount : "invalid POI line: " + line;
        parameters.replaceAll(String::trim);
        final Iterator<String> it = parameters.iterator();
        final String name = it.next();
        final String description = it.next();
        final Angle latitude = makeAngle(it.next(), it.next(), it.next(), it.next());
        final Angle longitude = makeAngle(it.next(), it.next(), it.next(), it.next());
        return new PointOfInterest(latitude, longitude, name, description);
    }

    /**
     * @param degrees non-negative integer degrees
     * @param minutes non-negative integer arc minutes
     * @param seconds non-negative integer arc seconds
     * @param direction single upper-case first letter of a cardinal direction
     * @return an angle with the given parameters
     */
    static Angle makeAngle(final String degrees, final String minutes, final String seconds, final String direction) {
        return new Angle(Integer.parseInt(degrees), Integer.parseInt(minutes), Integer.parseInt(seconds),
                CardinalDirection.fromChar(direction.charAt(0)));
    }
}
