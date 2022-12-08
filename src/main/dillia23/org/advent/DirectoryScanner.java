package org.advent;

import org.advent.utils.FileFinder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class DirectoryScanner {
    private static final String TERMINAL_FILE_LOC = "/terminal.txt";

    private static final long REQ_SPACE = 30_000_000;

    private static final long AVAIL_SPACE = 70_000_000;

    private Directory root;

    private final long totalSize;
    private final long availSpace;
    private long minDirSize;

    public DirectoryScanner() {
        final Scanner scanner;
        try  {
            scanner = FileFinder.getScannerForFileName(TERMINAL_FILE_LOC);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        scanTerminalCommands(scanner);
        totalSize = calcDirectories(root);
        availSpace = AVAIL_SPACE - totalSize;
        System.out.println(availSpace);
        minDirSize = Integer.MAX_VALUE;
        calcMinDirSize(root);
    }

    private void scanTerminalCommands(final Scanner scanner) {
        root = new Directory("/");
        Directory curr = root;
        while(scanner.hasNextLine()) {
            final String line = scanner.nextLine();

            // move
            if (line.startsWith("$ cd")) {
                curr = moveDir(line, curr);
            }
            // list
            else if (line.startsWith("$ ls")) {
                // I don't think we need to do anything?
            }
            // have a dir
            else if (line.startsWith("dir")) {
                createDir(line, curr);
            }
            // have a file
            else {
                final File file = createFile(line);
                curr.addFile(file);
            }
        }
    }

    private int calcDirectories(final Directory dir) {
        if (dir == null) {
            return 0;
        } else {
            int total = 0;
            for (final Directory child: dir.children) {
                total += calcDirectories(child);
            }

            for (final File file: dir.files) {
                total += file.size;
            }

            return total;
        }
    }

    public long getMinDirSize() {
        return minDirSize;
    }

    private long calcMinDirSize(final Directory dir) {
        if (dir == null) {
            return 0;
        } else {
            long total = 0;
            for (final Directory child: dir.children) {
                total += calcMinDirSize(child);
            }

            for (final File file: dir.files) {
                total += file.size;
            }

            // 7000000 = totalSize + REQ + Dir_DEL
            if (total + availSpace >= REQ_SPACE) {
                minDirSize = Math.min(minDirSize, total);
            }

            return total;
        }
    }

    private static Directory moveDir(final String input, final Directory curr) {
        // example $ cd .. (go up to parent)
        if (input.endsWith("..")) {
            return curr.parent;
        }

        // example cd ctd
        final String[] parsed = input.split(" ");
        if (parsed.length != 3) {
            throw new IllegalStateException("expected 2 parts for a cd command");
        }

        // root directory is a special case
        if (parsed[2].equals("/")) {
            return curr;
        }

        // we should always have this directory in children
        for (final Directory dir: curr.children) {
            if (dir.name.equals(parsed[2])) {
                return dir;
            }
        }

        // if we get here, something is wrong
        throw new IllegalStateException("directory does not exist");
    }

    private static Directory createDir(final String input, final Directory parent) {
        // example dir pgqmwn
        final String[] parsed = input.split(" ");
        if (parsed.length != 2) {
            throw new IllegalStateException("expected 2 parts for a directory");
        }

        // root directory is a special case
        if (parsed[1].equals("/")) {
            return parent;
        }

        // check if we have already seen this dir
        for (final Directory dir: parent.children) {
            if (dir.name.equals(parsed[1])) {
                return dir;
            }
        }

        final Directory dir = new Directory(parsed[1], parent);
        parent.addDirectory(dir);

        return dir;
    }

    private static File createFile(final String input) {
        //example "17637 snqcgbs.nhv"
        final String[] parsed = input.split(" ");
        if (parsed.length != 2) {
            throw new IllegalStateException("expected 2 parts for a file");
        }

        return new File(parsed[1], Integer.parseInt(parsed[0]));
    }

    public long getTotalSize() {
        return totalSize;
    }

    static class Directory {
        private final String name;
        Directory parent;
        Collection<Directory> children;
        Collection<File> files;

        public Directory(final String name) {
            this.name = name;
            parent = null;
            children = new ArrayList<>();
            files = new ArrayList<>();
        }

        public Directory(final String name, final Directory parent) {
            this(name);
            this.parent = parent;
        }

        public Directory(final String name, final Directory parent, final Collection<Directory> children, final Collection<File> files) {
            this(name, parent);
            this.children = children;
            this.files = files;
        }

        public String getName() {
            return name;
        }

        public void addDirectory(final Directory directory) {
            this.children.add(directory);
        }

        public void addFile(final File file) {
            this.files.add(file);
        }
    }

    private record File(String name, int size) {}
}
