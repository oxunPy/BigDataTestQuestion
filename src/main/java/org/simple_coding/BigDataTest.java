package org.simple_coding;

import org.simple_coding.case_any_place.ReadBigDataFileV3;
import org.simple_coding.common.ExecutingTimeInSeconds;

import java.io.File;

public class BigDataTest {
    public static void main(String[] args) {
        String fileName;
        if(args.length >= 1) {
            fileName = args[0];
        } else {
            fileName = "C:/Users/acer/Downloads/lng-4/lng.txt";
        }

        if (fileName == null || fileName.trim().isEmpty() || !new File(fileName).exists()) {
            System.out.println("File not found");
            return;
        }

        ReadBigDataFileV3 readerBigData = new ReadBigDataFileV3(fileName);
        int doneInSeconds = ExecutingTimeInSeconds.callMethod(() -> readerBigData.divideGroupsFileRows(true));
        System.out.println("Task is done in " + doneInSeconds + " seconds");
    }
}