package io.github.dediamondpro.autoupdater.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileUtils {
    public static String readFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        StringBuilder builder = new StringBuilder();
        while (scanner.hasNextLine()) {
            builder.append(scanner.next());
        }
        scanner.close();
        return builder.toString();
    }
}
