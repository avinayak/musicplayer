package musicplayer;


import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import static java.nio.file.FileVisitResult.*;
import java.util.ArrayList;

public class Mp3Finder {

    public static class Finder
            extends SimpleFileVisitor<Path> {

        private final PathMatcher matcher;
        public int numMatches = 0;
        public ArrayList<String> fileList;

        Finder() {
            matcher = FileSystems.getDefault().getPathMatcher("glob:*.[Mm][Pp]3");
            
            fileList = new ArrayList<>();
        }

        void find(Path file) {
            Path name = file.getFileName();
            if (name != null && matcher.matches(name)) {
                fileList.add(file.toString());
                numMatches++;

            }
        }

        ArrayList<String> getMp3() {
            return fileList;
        }

        @Override
        public FileVisitResult visitFile(Path file,
                BasicFileAttributes attrs) {
            find(file);
            return CONTINUE;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir,
                BasicFileAttributes attrs) {
            find(dir);
            return CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file,
                IOException exc) {
            System.err.println(exc);
            return CONTINUE;
        }
    }
}
