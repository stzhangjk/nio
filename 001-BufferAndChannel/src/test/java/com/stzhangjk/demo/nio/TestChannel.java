package com.stzhangjk.demo.nio;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author zhjk on 2018/1/18.
 */
public class TestChannel {
    @Test
    public void testTransfer(){
        String src = ClassLoader.getSystemResource("src.txt").getFile();
        String dst = ClassLoader.getSystemResource("").getFile() + "dst.txt";
        try(FileChannel from = new FileInputStream(src).getChannel();
            FileChannel to = new FileOutputStream(dst).getChannel()){
            from.transferTo(0,from.size(),to);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
