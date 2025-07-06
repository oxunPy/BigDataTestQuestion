package org.simple_coding.common;

public class RowValidator {
    // invalid rows ex:  "8383"200000741652251";"79855053897"83100000580443402";"200000133000191"
    public boolean includesInvalidColumn(String[] columns) {
        for (String col : columns) {
            if (isOdd(numberOfCharsIn(col, '"')))
                return true;
        }

        return false;
    }

    public boolean emptyOrBlank(String col) {
        return col == null || col.isEmpty() || col.equals("\"\"");
    }

    private int numberOfCharsIn(String txt, char c) {
        int count = 0;
        for(int i = 0; i < txt.length(); i++) {
            if(txt.charAt(i) == c) count++;
        }

        return count;
    }

    private boolean isOdd(int num) {
        return num % 2 == 1;
    }
}
