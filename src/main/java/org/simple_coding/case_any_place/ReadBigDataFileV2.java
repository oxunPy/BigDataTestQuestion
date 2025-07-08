package org.simple_coding.case_any_place;

import org.simple_coding.common.HashMapList;
import org.simple_coding.common.RowValidator;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;

public class ReadBigDataFileV2 {
    String fileName;
    UnionFind unionFind;
    double[][] fileData;
    int maxRowNum, maxColNum;

    public ReadBigDataFileV2(String fileName) {
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
                String[] parts = line.split(";");
                if(RowValidator.includesInvalidColumn(parts))
                    continue;

                maxRow++;
                maxCol = Math.max(maxCol, parts.length);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        return new int[] {maxRow, maxCol};
    }

    public void divideGroupsFileRows() {
        fillFileDataByReading();
        unionGroups();
        printOutput();
    }

    private void fillFileDataByReading() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = null;
            int row = 0;
            while((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if(RowValidator.includesInvalidColumn(parts))
                    continue;

                for(int col = 0; col < parts.length; col++) {
                    if(RowValidator.emptyOrBlank(parts[col]))
                        fileData[row][col] = 0;
                    else
                        fileData[row][col] = Double.parseDouble(parts[col].replaceAll("\"", ""));
                }
                row++;
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void unionGroups() {
        for(int col = 0; col < maxColNum; col++) {
            HashMap<Double, Integer> firstSameCols = new HashMap<>();
            for(int row = 0; row < maxRowNum; row++) {
                if(fileData[row][col] == 0.0) continue;

                if(firstSameCols.containsKey(fileData[row][col])) {
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

        for(int row = 0; row < maxRowNum; row++) {
            int currRowRoot = unionFind.find(row);
            if(roots.contains(currRowRoot)) {
                groupContents.put(currRowRoot, row);
            }
        }

        return new ArrayList<>(groupContents.values());
    }

    private void sortGroups(List<List<Integer>> groups) {
        groups.sort((g1, g2) -> Integer.compare(g2.size(), g1.size()));
    }

    private void printOutput() {
        String outPath = Paths.get("output.txt").toString();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outPath, StandardCharsets.UTF_8))) {
            List<List<Integer>> groups = groupRootsList();

            printTitle(groups.size(), writer);
            printGroupRowsInOrdered(groups, writer);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void printTitle(int maxGroups, BufferedWriter writer) throws IOException {
        writer.write("Получившиеся число групп с более чем одним элементом = " + maxGroups);
        writer.newLine();
    }

    private void printGroupRowsInOrdered(List<List<Integer>> groups, BufferedWriter writer) throws IOException {
        sortGroups(groups);
        int groupNum = 1;
        for(List<Integer> group : groups) {
            writer.write("Группа " + groupNum++);
            writer.newLine();
            for(int row : group) {
                writer.write(rowToString(fileData[row]));
                writer.newLine();
            }
        }
    }

    String rowToString(double[] row) {
        StringBuilder sb = new StringBuilder();
        for(double num : row) {
            sb.append(num);
            sb.append(";");
        }

        return sb.toString();
    }
}
