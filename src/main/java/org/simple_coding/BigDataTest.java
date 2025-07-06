package org.simple_coding;

import org.simple_coding.case_any_place.ReadBigDataFile;
import org.simple_coding.common.ExecutingTimeInSeconds;

public class BigDataTest {
    public static void main(String[] args) {

        ReadBigDataFile readerBigData = new ReadBigDataFile("C:/Users/acer/Downloads/lng-4.txt/lng.txt");
        int doneInSeconds = ExecutingTimeInSeconds.callMethod(readerBigData::divideGroupsFileRows);
        System.out.println("Задача выполнена за " + doneInSeconds + " секунд");
    }


}