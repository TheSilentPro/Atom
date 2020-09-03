package tsp.atom.editor;

import tsp.atom.util.Config;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Editor {

    private File file;

    public Editor(File file) {
        this.file = file;
    }

    public List<File> getFiles() {
        if (file.isDirectory()) {
            return Arrays.asList(file.listFiles());
        }
        return null;
    }

    public List<String> getLines() {
        try {
            Scanner scanner = new Scanner(file);
            List<String> lines = new ArrayList<String>();
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }

            return lines;
        } catch (FileNotFoundException ex) {
            return null;
        }
    }

    public void setLine(int line, String text) throws IOException {
        Path path = Paths.get(file.getPath());
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        lines.set(line, text.replace(Config.getString("spaceCharacter"), " "));
        Files.write(path, lines, StandardCharsets.UTF_8);
    }

}
