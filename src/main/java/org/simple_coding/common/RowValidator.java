package org.simple_coding.common;

public class RowValidator {
    // invalid rows ex:  "8383"200000741652251";"79855053897"83100000580443402";"200000133000191"
    public static boolean includesInvalidColumn(String[] columns) {
        int emptySizes = 0;
        for (String col : columns) {
            if (isOdd(numberOfCharsIn(col, '"')))
                return true;

            if(emptyOrBlank(col))
                emptySizes++;
        }

        return emptySizes == columns.length;
    }

    public static boolean invalidLine(String line) {
        int numQuotMark = 0;

        for(int i = 0; i < line.length(); i++) {
            if(line.charAt(i) == ';') {
                if(isOdd(numQuotMark)) {
                    return true;
                }
                numQuotMark = 0;
            } else if(line.charAt(i) == '"') {
                numQuotMark++;
            }
        }

        return isOdd(numQuotMark);
    }

    public static boolean emptyOrBlank(String col) {
        return col == null || col.isEmpty() || col.equals("\"\"");
    }

    public static int numberOfCharsIn(String txt, char c) {
        int count = 0;
        for(int i = 0; i < txt.length(); i++) {
            if(txt.charAt(i) == c) count++;
        }

        return count;
    }

    private static boolean isOdd(int num) {
        return num % 2 == 1;
    }
}
