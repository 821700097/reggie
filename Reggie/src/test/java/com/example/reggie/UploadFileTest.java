package com.example.reggie;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UploadFileTest {

    @Test
    public void getFilenameSuffixTest(){
        String filename="asdfagqwe.jpg";
        String substring = filename.substring(filename.lastIndexOf("."));
        System.out.println(substring+""+"...... world");

    }
}
