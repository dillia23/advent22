package org.advent;

import org.advent.utils.FileFinder;
import org.advent.utils.StringUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class DatastreamReader {
    private static final String DATASTREAM_FILE_LOC = "/datastream.txt";

    private int startOfPacketMarkerLoc;

    private int startOfMessageMarkerLoc;

    public DatastreamReader() {
        final Scanner scanner;
        try  {
            scanner = FileFinder.getScannerForFileName(DATASTREAM_FILE_LOC);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        findPacketMarker(scanner);
    }

    public int getStartOfPacketMarkerLoc() {
        return startOfPacketMarkerLoc;
    }

    public int getStartOfMessageMarkerLoc() {
        return startOfMessageMarkerLoc;
    }

    private void findPacketMarker(final Scanner scanner) {
        // note there is only 1 line of data
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            findStartOfPacketMarketLoc(line);
            findStartOfMessageMarketLoc(line);
        }
    }

    private void findStartOfPacketMarketLoc(final String stream) {
        int lo = 0;
        int hi = 3;

        while (lo < stream.length() && hi < stream.length()) {
            // double loop is gross but this is 4*4 so whatever
            if (StringUtils.isUniqueSubstring(stream, lo, hi)) {
                startOfPacketMarkerLoc = hi + 1;
                break;
            }
            lo++;
            hi++;
        }
    }

    private void findStartOfMessageMarketLoc(final String stream) {
        int lo = 0;
        int hi = 13;

        while (lo < stream.length() && hi < stream.length()) {
            // double loop is gross but this is 13*13 so less whatever
            if (StringUtils.isUniqueSubstring(stream, lo, hi)) {
                startOfMessageMarkerLoc = hi + 1;
                break;
            }
            lo++;
            hi++;
        }
    }
}
