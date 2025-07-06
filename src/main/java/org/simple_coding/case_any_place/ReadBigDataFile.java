package org.simple_coding.case_any_place;

import org.simple_coding.common.HashMapList;
import org.simple_coding.common.RowData;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;

public class ReadBigDataFile {
    List<RowData> allUniqueRows;
    String fileName;
    HashMapList<String, Integer> colRowsMap;       // column to row indexes map
    UnionFind unionFind;

    public ReadBigDataFile(String fileName) {
        allUniqueRows = new ArrayList<>();
        colRowsMap = new HashMapList<>();
        this.fileName = fileName;
    }

    public void divideGroupsFileRows() {
        makeFileRowsByReading();
        collectColumnRowsInMap();
        unionGroups();

        List<List<RowData>> groups = groupRootsList();
        sortGroups(groups);
        printOutput(groups);
    }

    private void makeFileRowsByReading() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = null;
            int lineNumber = 0;
            while((line = reader.readLine()) != null) {
                lineNumber++;
                RowData currentRow = makeRowData(line, lineNumber);
                if(currentRow == null) continue;

                allUniqueRows.add(currentRow);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void collectColumnRowsInMap() {
        for(int i = 0; i < allUniqueRows.size(); i++) {
            RowData row = allUniqueRows.get(i);
            for(int j = 0; j < row.sizeColumns(); j++) {
                if(row.isValidColAtI(j)) {
                    String col = row.getColumnAtI(j);
                    String key = col + ":" + j;
                    colRowsMap.put(key, i);
                }
            }
        }
    }

    private void unionGroups() {
        unionFind = new UnionFind(allUniqueRows.size());
        for(String key : colRowsMap.keySet()) {
            List<Integer> groupRowsIdx = colRowsMap.get(key);
            for(int i = 1; i < groupRowsIdx.size(); i++) {
                unionFind.union(groupRowsIdx.get(i - 1), groupRowsIdx.get(i));
                allUniqueRows.get(i).parent = allUniqueRows.get(i - 1);
            }
        }
    }

    private List<List<RowData>> groupRootsList() {
        HashMapList<Integer, RowData> groupMap = new HashMapList<>();

        for(int i = 0; i < allUniqueRows.size(); i++) {
            int root = unionFind.find(i);
            groupMap.put(root, allUniqueRows.get(i));
        }

        return new ArrayList<>(groupMap.values());
    }

    private void sortGroups(List<List<RowData>> groups) {
        groups.sort((g1, g2) -> Integer.compare(g2.size(), g1.size()));
    }

    private int numberOfGroupsMoreThan2(List<List<RowData>> groups) {
        int num = 0;
        for(List<RowData> group : groups) {
            if(group.size() > 1) num++;
        }

        return num;
    }

    private void printOutput(List<List<RowData>> groups) {
        String outPath = Paths.get("output.txt").toString();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outPath, StandardCharsets.UTF_8))) {
            writer.write("Получившиееся число групп с более чем одним элементом = " + numberOfGroupsMoreThan2(groups));
            writer.newLine();
            int groupNum = 1;
            for (List<RowData> group : groups) {
                if(group.size() <= 1) continue;
                writer.write("Группа " + groupNum++);
                writer.newLine();

                for (RowData row : group) {
                    writer.write(row.getRowText());
                    writer.newLine();
                }
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private RowData makeRowData(String rowText, int id) {
        RowData newRow = new RowData(id, rowText);
        if(newRow.isValid()) {
            return newRow;
        }

        return null;
    }


}
