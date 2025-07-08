package org.simple_coding;

import org.simple_coding.case_any_place.ReadBigDataFileV2;
import org.simple_coding.common.ExecutingTimeInSeconds;

import java.io.File;

public class BigDataTest {
    public static void main(String[] args) {
        String fileName;
        if(args.length >= 1) {
            fileName = args[0];
        } else {
            fileName = "C:/Users/acer/Downloads/lng-4 (1).txt/lng.txt";
        }

        if(fileName == null || fileName.trim().isEmpty() || !new File(fileName).exists()) {
            System.out.println("File not found");
            return;
        }

        ReadBigDataFileV2 readerBigData = new ReadBigDataFileV2(fileName);
        int doneInSeconds = ExecutingTimeInSeconds.callMethod(readerBigData::divideGroupsFileRows);
        System.out.println("Task is done in " + doneInSeconds + " seconds");
    }
}