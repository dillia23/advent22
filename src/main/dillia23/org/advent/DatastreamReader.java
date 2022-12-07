package org.advent;

import org.advent.utils.FileFinder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class DatastreamReader {
    private static final String DATASTREAM_FILE_LOC = "/datastream.txt";

    private int startOfPacketMarkerLoc;

    public DatastreamReader() {
        final Scanner scanner;
        try  {
            scanner = FileFinder.getScannerForFileName(DATASTREAM_FILE_LOC);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        findPacketMarker(scanner);
    }

    private void findPacketMarker(final Scanner scanner) {
        // note there is only 1 line of data
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            int lo = 0;
            int hi = 3;

            while (lo < line.length() && hi < line.length()) {
                if (isValidStartOfPacketMarker(line, lo, hi)) {
                    startOfPacketMarkerLoc = hi + 1;
                    break;
                }
                lo++;
                hi++;
            }
        }
    }

    private boolean isValidStartOfPacketMarker(final String stream, final int lo, final int hi) {
        // double loop is gross but this is 4*4 so whatever
        for (int i = lo; i <= hi; i++) {
            for (int j = lo; j <= hi; j++) {
                if (i != j && stream.charAt(i) == stream.charAt(j)) {
                    return false;
                }
            }
        }

        return true;
    }

    public int getStartOfPacketMarkerLoc() {
        return startOfPacketMarkerLoc;
    }
}
