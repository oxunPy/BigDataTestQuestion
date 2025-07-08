package org.simple_coding.common;

import java.util.Arrays;

public class RowDataV2 implements Validator {
    private int id;
    private Long[] columns;

    public RowDataV2(int id, String rowText) {
        this.id = id;
        String[] elems = rowText.split(";");
        columns = new Long[elems.length];
        for(int i = 0; i < elems.length; i++) {
            String elem = elems[i];
            if(RowValidator.emptyOrBlank(elem)) columns[i] = null;
            else columns[i] = Long.parseLong(elem);
        }
    }

    public Long getColumnAtI(int i) {
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

    public boolean isValidColAtI(int i) {
        return columns[i] != null;
    }


    @Override
    public boolean equals(Object obj) {
        if(obj instanceof RowDataV2) {
            return Arrays.equals(columns, ((RowDataV2)obj).columns);
        }

        return false;
    }

    @Override
    public boolean isValid() {
        for (Long column : columns) {
            if (column == null) return false;
        }

        return true;
    }
}
