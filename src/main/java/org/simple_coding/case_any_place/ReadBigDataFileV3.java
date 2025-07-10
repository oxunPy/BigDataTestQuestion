package org.simple_coding.case_any_place;

import org.simple_coding.common.HashMapList;
import org.simple_coding.common.RowValidator;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;

public class ReadBigDataFileV3 {
    String fileName;
    UnionFind unionFind;
    double[][] fileData;
    int maxRowNum, maxColNum;

    public ReadBigDataFileV3(String fileName) {
        this.fileName = fileName;

        int[] maxRowAndCol = findMaxSizes();
        maxRowNum = maxRowAndCol[0];
        maxColNum = maxRowAndCol[1];
        fileData = new double[maxRowNum][maxColNum];
        unionFind = new UnionFind(maxRowNum);
    }

    private int[] findMaxSizes() {
        int maxCol = 0;
        int maxRow = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (RowValidator.invalidLine(line))
                    continue;

                maxRow++;
                maxCol = Math.max(maxCol, RowValidator.numberOfCharsIn(line, ';') + 1);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        return new int[]{maxRow, maxCol};
    }

    // whFast - Without header printing in file slightly optimized
    public void divideGroupsFileRows(boolean whFast) {
        fillFileDataByReading();
        unionGroups();
        printOutput(whFast);
    }

    private void fillFileDataByReading() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = null;
            int row = 0;
            while ((line = reader.readLine()) != null) {
                if (RowValidator.invalidLine(line))
                    continue;

                String[] parts = line.split(";");
                for (int col = 0; col < parts.length; col++) {
                    if (RowValidator.emptyOrBlank(parts[col]))
                        fileData[row][col] = -1;        // -1 as null
                    else
                        fileData[row][col] = Double.parseDouble(parts[col].substring(1, parts[col].length() - 1));
                }
                row++;
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void unionGroups() {
        for (int col = 0; col < maxColNum; col++) {
            HashMap<Double, Integer> firstSameCols = new HashMap<>();
            for (int row = 0; row < maxRowNum; row++) {
                if (fileData[row][col] == 0.0 || fileData[row][col] == -1) continue;

                if (firstSameCols.containsKey(fileData[row][col])) {
                    unionFind.union(firstSameCols.get(fileData[row][col]), row);
                } else {
                    firstSameCols.put(fileData[row][col], row);
                }

            }
        }
    }

    private List<List<Integer>> groupRootsList() {
        Set<Integer> roots = unionFind.findOnlyRoots();
        HashMapList<Integer, Integer> groupContents = new HashMapList<>();

        for (int row = 0; row < maxRowNum; row++) {
            int currRowRoot = unionFind.find(row);
            if (roots.contains(currRowRoot)) {
                groupContents.put(currRowRoot, row);
            }
        }

        return new ArrayList<>(groupContents.values());
    }

    private void sortGroups(List<List<Integer>> groups) {
        groups.sort((g1, g2) -> Integer.compare(g2.size(), g1.size()));
    }

    private Set<Integer> ignoredGroups(List<List<Integer>> groups) {
        Set<Integer> indexingIgnoreGroups = new HashSet<>();

        for (int groupIdx = 0; groupIdx < groups.size(); groupIdx++) {
            List<Integer> group = groups.get(groupIdx);
            Set<String> uniqueLines = new HashSet<>();

            for (int row : group) {
                uniqueLines.add(rowToString(fileData[row]));
            }

            if (uniqueLines.size() <= 1) {
                indexingIgnoreGroups.add(groupIdx);
            }
        }

        return indexingIgnoreGroups;
    }

    private void printOutput(boolean whFast) {
        String outPath = Paths.get("output.txt").toString();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outPath, StandardCharsets.UTF_8))) {
            List<List<Integer>> groups = groupRootsList();
            sortGroups(groups);

            if(whFast) {
                int maxGroups = printGroupRowsInOrderedWithoutHeaderFast(groups, writer);
                System.out.println("Number of Groups more than one elements = " + maxGroups);
            } else {
                Set<Integer> indexedIgnoreGroups = ignoredGroups(groups);
                printTitle(groups.size() - indexedIgnoreGroups.size(), writer);
                printGroupRowsInOrderedWithHeaderSlow(groups, indexedIgnoreGroups, writer);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void printTitle(int maxGroups, BufferedWriter writer) throws IOException {
        System.out.println("Number of Groups more than one elements = " + maxGroups);
        writer.write("Получившиеся число групп с более чем одним элементом = " + maxGroups);
        writer.newLine();
    }

    private void printGroupRowsInOrderedWithHeaderSlow(List<List<Integer>> groups, Set<Integer> indexedIgnoreGroups, BufferedWriter writer) throws IOException {
        int groupNum = 1;
        for (int groupIdx = 0; groupIdx < groups.size(); groupIdx++) {
            if (indexedIgnoreGroups.contains(groupIdx)) continue;

            List<Integer> group = groups.get(groupIdx);
            writer.write("Группа " + groupNum++);
            writer.newLine();
            for (int row : group) {
                writer.write(rowToString(fileData[row]));
                writer.newLine();
            }
        }
    }

    private int printGroupRowsInOrderedWithoutHeaderFast(List<List<Integer>> groups, BufferedWriter writer) throws IOException {
        int groupNum = 1;
        for(List<Integer> group : groups) {
            Set<String> uniqueLines = new HashSet<>();
            for(int row : group) {
                uniqueLines.add(rowToString(fileData[row]));
            }

            if(uniqueLines.size() > 1) {
                writer.write("Группа " + groupNum++);
                writer.newLine();

                for(String line : uniqueLines) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        }

        return groupNum - 1;
    }

    String rowToString(double[] row) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < row.length; i++) {
            double num = row[i];
            if (num == 0.0) break;

            if (num == -1)
                sb.append("\"\"");
            else if (num == (long) num)
                sb.append('"').append((long) num).append('"');
            else
                sb.append('"').append(num).append('"');

            if (i != row.length - 1) sb.append(";");
        }

        return sb.toString();
    }
}
