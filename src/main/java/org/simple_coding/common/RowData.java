package org.simple_coding.common;

import java.util.Arrays;

public class RowData implements Validator {
    private int id;
    private String rowText;
    private String[] columns;

    public RowData(int id, String rowText) {
        this.id = id;
        this.rowText = rowText;
        this.columns = rowText.split(";");
    }

    public boolean matches(RowData under) {
        if(under == null || !hasColumns() || !under.hasColumns())
            return false;

        for(int i = 0; i < Math.min(sizeColumns(), under.sizeColumns()); i++) {
            if(RowValidator.emptyOrBlank(columns[i]) || RowValidator.emptyOrBlank(under.getColumnAtI(i)))
                continue;

            if(columns[i].equals(under.getColumnAtI(i))) {
                return true;
            }
        }

        return false;
    }

    public String getColumnAtI(int i) {
        return columns[i];
    }

    public int sizeColumns() {
        return columns == null ? 0 : columns.length;
    }

    public boolean hasColumns() {
        return columns != null && columns.length > 0;
    }

    public int getId() {
        return id;
    }

    public String getRowText() {
        return rowText;
    }

    public boolean isValidColAtI(int i) {
        return !RowValidator.emptyOrBlank(columns[i]);
    }


    @Override
    public boolean equals(Object obj) {
        if(obj instanceof RowData) {
            return Arrays.equals(columns, ((RowData)obj).columns);
        }

        return false;
    }

    @Override
    public boolean isValid() {
        return !RowValidator.includesInvalidColumn(columns);
    }
}
