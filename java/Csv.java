/*
 * The MIT License
 *
 * Copyright 2018 giuliobosco.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Csv File Manager.
 * Can read, read and modify and create an CSV file.
 *
 * @author giuliobosco
 * @version 1.1
 */
public class Csv {

    // ------------------------------------------------------------------------------------------------------- Constants

    /**
     * Default CSV separator.
     */
    public static final char DFL_SEPARATOR = ',';

    // ------------------------------------------------------------------------------------------------------ Attributes
    
    /**
     * Path of the CSV file.
     */
    private Path filePath;

    /**
     * Content of the CSV file.
     */
    private List<String> csv;

    /**
     * Csv Separator.
     */
    private char separator;

    /**
     * Header of the CSV file.
     * Each Array element is an column title.
     */
    private String[] header;

    // ----------------------------------------------------------------------------------------------- Getters & Setters

    /**
     * Getter for the CSV.
     *
     * @return Content of the CSV.
     */
    public List<String> getCsv() {
        return this.csv;
    }

    /**
     * Getter for the CSV separator.
     *
     * @return CSV Separator.
     */
    public char getSeparator() {
        return this.separator;
    }

    /**
     * Setter for the Header of the Csv.
     * Set the header only if the Csv is empty.
     *
     * @param header Header of the Csv. Each element of the Array is an column.
     */
    public void setHeader(String[] header) {
        if (this.csv.size() != 0) {
            return;
        }

        this.header = header;

        String add = "";

        for (String column : header) {
            add += column + separator;
        }

        this.csv.add(add);
    }

    /**
     * Getter for the Header of the Csv.
     *
     * @return Header of the CSV.
     */
    public String[] getHeader() {
        return this.header;
    }

    // ---------------------------------------------------------------------------------------------------- Constructors

    /**
     * Constructor with the file path and the separator.
     *
     * @param filePath  Path of the CSV file.
     * @param separator CSV separator.
     * @throws IOException          Error on the file system.
     * @throws NoCsvHeaderException No Header in the CSV file find.
     */
    public Csv(Path filePath, char separator) throws IOException, NoCsvHeaderException {
        if (Files.exists(filePath) && !Files.notExists(filePath)) {
            this.filePath = filePath;

            this.csv = Files.readAllLines(filePath);

            if (this.csv.size() == 0) {
                throw new NoCsvHeaderException("No Csv Header.");
            }

            String fullHeader = this.csv.get(0);
            this.header = fullHeader.split(Character.toString(separator));
        } else {
            Files.createFile(filePath);

            this.filePath = filePath;

            this.csv = new ArrayList<>();
        }
        this.separator = separator;
    }

    /**
     * Constructor with the file path.
     *
     * @param filePath Path of the CSV file.
     * @throws IOException          Error on the file System.
     * @throws NoCsvHeaderException No Header in the CSV file find.
     */
    public Csv(Path filePath) throws IOException, NoCsvHeaderException {
        this(filePath, DFL_SEPARATOR);
    }

    /**
     * Constructor with the file path and the separator.
     *
     * @param path      Path of the CSV file.
     * @param separator CSV separator.
     * @throws IOException          Error on the file system.
     * @throws NoCsvHeaderException No header in the CSV file find.
     */
    public Csv(String path, char separator) throws IOException, NoCsvHeaderException {
        this(Paths.get(path), separator);
    }

    /**
     * Constructor with the file path.
     *
     * @param path Path of the CSV file.
     * @throws IOException          Error on the file system.
     * @throws NoCsvHeaderException No header in the CSV file find.
     */
    public Csv(String path) throws IOException, NoCsvHeaderException {
        this(Paths.get(path));
    }

    // ---------------------------------------------------------------------------------------------------- Help Methods
    // ------------------------------------------------------------------------------------------------ Generals Methods

    /**
     * Save on the file system.
     *
     * @throws IOException Error on the file system.
     */
    public void save() throws IOException {
        if (Files.isWritable(filePath)) {
            Files.write(filePath, csv, Charset.forName("UTF-8"));
        }
    }

    /**
     * Add line to the CSV.
     * Add the full line passed as parameter, not check the separator or integrity of the string.
     *
     * @param string Line to add. The full line.
     */
    public void addLine(String string) throws NoCsvHeaderException {
        if (this.header == null) {
            throw new NoCsvHeaderException("No CSV Header set.");
        }

        this.csv.add(string);
    }

    /**
     * Add line to the CSV.
     * Add the values in the string array and insert them in the csv with the separator.
     *
     * @param strings Values of the line to add to the CSV.
     */
    public void addLine(String[] strings) throws NoCsvHeaderException {
        String add = "";

        for (String string : strings) {
            add += string + separator;
        }

        this.addLine(add);
    }

    /**
     * Ad many lines to the CSV.
     * Each row needs the full string.
     *
     * @param strings Lines to add to the csv.
     */
    public void addLines(String[] strings) throws NoCsvHeaderException {
        for (String string : strings) {
            this.addLine(string);
        }
    }
}
