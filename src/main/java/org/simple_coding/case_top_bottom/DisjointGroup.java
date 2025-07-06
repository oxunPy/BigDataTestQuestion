package org.simple_coding.case_top_bottom;

import org.simple_coding.common.RowData;

import java.util.ArrayList;
import java.util.List;

public class DisjointGroup {
    List<RowData> rows;

    public DisjointGroup(RowData beginningRow) {
        rows = new ArrayList<>();
        if(beginningRow != null && beginningRow.hasColumns()) {
            rows.add(beginningRow);
        }
    }

    public boolean isPossibleToAdd(RowData row) {
        return !hasRows() || getLastRow().matches(row);
    }

    public void updateGroup(RowData row) {
        if(row != null && row.hasColumns()) {
            rows.add(row);
        }
    }

    public boolean notConsistedSingleRow() {
        return sizeRows() > 1;
    }

    public boolean hasRows() {
        return !rows.isEmpty();
    }

    private int sizeRows() {
        return rows.size();
    }

    private RowData getLastRow() {
        return rows.get(sizeRows() - 1);
    }

    public void printToConsole(int groupNumber) {
        System.out.println("Группа " + groupNumber);
        for(RowData row : rows)
            System.out.println(row.getRowText());
    }
}
