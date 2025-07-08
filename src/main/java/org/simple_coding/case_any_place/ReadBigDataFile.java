package org.simple_coding.case_any_place;

import org.simple_coding.common.HashMapList;
import org.simple_coding.common.RowData;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;

public class ReadBigDataFile {
    Set<RowData> allUniqueRows;
    String fileName;
    UnionFind unionFind;

    public ReadBigDataFile(String fileName) {
        allUniqueRows = new HashSet<>();
        this.fileName = fileName;
    }

    public void divideGroupsFileRows() {
        makeFileRowsByReading();
        unionGroups(collectColumnRowsInMap());

        List<List<RowData>> groups = groupRootsList();
        sortGroups(groups);
        printOutput(groups);
    }

    private void makeFileRowsByReading() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = null;
            int idNumber = -1;
            while((line = reader.readLine()) != null) {
                RowData currentRow = makeRowData(line, idNumber + 1);
                if(currentRow == null) continue;

                allUniqueRows.add(currentRow);
                idNumber++;
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private HashMapList<String, Integer> collectColumnRowsInMap() {
        HashMapList<String, Integer> colRowsMap = new HashMapList<>();
        for(RowData row: allUniqueRows) {
            for(int j = 0; j < row.sizeColumns(); j++) {
                if(row.isValidColAtI(j)) {
                    String col = row.getColumnAtI(j);
                    String key = col + ":" + j;
                    colRowsMap.put(key, row.getId());
                }
            }
        }

        return colRowsMap;
    }

    private void unionGroups(HashMapList<String, Integer> colRowsMap) {
        unionFind = new UnionFind(allUniqueRows.size());
        for(String key : colRowsMap.keySet()) {
            List<Integer> groupRowsIdx = colRowsMap.get(key);
            for(int i = 1; i < groupRowsIdx.size(); i++) {
                unionFind.union(groupRowsIdx.get(i - 1), groupRowsIdx.get(i));
            }
        }
    }

    private List<List<RowData>> groupRootsList() {
        HashMapList<Integer, RowData> groupMap = new HashMapList<>();

        for(RowData row : allUniqueRows) {
            int root = unionFind.find(row.getId());
            groupMap.put(root, row);
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
            writer.write("Получившиеся число групп с более чем одним элементом = " + numberOfGroupsMoreThan2(groups));
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
