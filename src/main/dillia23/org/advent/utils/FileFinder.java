package org.advent.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Scanner;

public class FileFinder {
    public static Scanner getScannerForFileName(final String fileName)
            throws FileNotFoundException, URISyntaxException {
        final File input = new File(String.valueOf(Path.of(
                Objects.requireNonNull(FileFinder.class.getResource(fileName)).toURI())));
        return new Scanner(Objects.requireNonNull(input));
    }
}
