package org.simple_coding.case_top_bottom;

import org.simple_coding.common.RowData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadBigDataFile {
    DisjointGroup currentGroup;
    RowData prevRow;

    public ReadBigDataFile() {
        currentGroup = new DisjointGroup(null);
    }

    public void readFileByRow(final String fileName) {
        int groupNumber = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = null;
            while((line = reader.readLine()) != null) {
                RowData currentRow = makeRowData(line);
                if(currentRow == null) continue;

                if(currentGroup.isPossibleToAdd(currentRow)) {
                    currentGroup.updateGroup(currentRow);
                    prevRow = currentRow;
                } else {
                    if(currentGroup.notConsistedSingleRow()) {
                        groupNumber++;
                        currentGroup.printToConsole(groupNumber);
                    }
                    currentGroup = new DisjointGroup(currentRow);
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("Количество групп: " + groupNumber);
    }


    private RowData makeRowData(String rowText) {
        RowData newRow = new RowData(0, rowText);
        if(newRow.isValid()) {
            return newRow;
        }

        return null;
    }

    // invalid rows ex:  "8383"200000741652251";"79855053897"83100000580443402";"200000133000191"
    boolean includesInvalidElement(String[] rowElements) {
        for(int i = 0; i < rowElements.length; i++) {
            String element = rowElements[i];
            if(isOdd(numberOfCharsIn(element, '"')))
                return true;
        }

        return false;
    }

    int numberOfCharsIn(String txt, char c) {
        int count = 0;
        for(int i = 0; i < txt.length(); i++) {
            if(txt.charAt(i) == c) count++;
        }

        return count;
    }

    boolean isOdd(int num) {
        return num % 2 == 1;
    }
}
