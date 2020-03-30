package com.example.baselibrary;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws IOException {
        // Context of the app under test.
//        writeExcel();
        readExcel();
    }

    private void readExcel() throws IOException {
        File excel = new File("E:\\本人的项目\\单词笔记项目资源\\单词库\\高中词汇表-3500.xlsx");
        BufferedReader reader = new BufferedReader(new FileReader(excel));
        System.out.println(reader.readLine());
    }

    private void writeExcel() throws IOException {

        File excel = new File("D:\\example.xlsx");
        if (!excel.exists()) {
            boolean newFile = excel.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(excel));
        writer.write("a\tb\tc\n" +
                "e\tf\tg");
        writer.flush();
    }
}
