package com.stzhangjk.demo.nio;

import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

/**
 * @author zhjk on 2018/1/18.
 */
public class TestScatterGather {
    @Test
    public void testScatter(){
        File file = new File(ClassLoader.getSystemResource("test.txt").getFile());
        try(FileChannel in = new FileInputStream(file).getChannel()){
            ByteBuffer[] buffers = new ByteBuffer[]{
                ByteBuffer.allocate(10),
                ByteBuffer.allocate(10),
                ByteBuffer.allocate(10)
            };
            while(in.read(buffers) != -1){
                for(ByteBuffer buffer : buffers){
                    buffer.flip();
                    byte[] tmp = new byte[buffer.remaining()];
                    buffer.get(tmp);
                    System.out.println(new String(tmp));
                    buffer.clear();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGather(){
        File src = new File(ClassLoader.getSystemResource("test.txt").getFile());
        File dst = new File(ClassLoader.getSystemResource("").getFile() + "testGather.txt");
        ByteBuffer[] buffers = new ByteBuffer[]{
                ByteBuffer.allocate(10),
                ByteBuffer.allocate(10),
                ByteBuffer.allocate(10)
        };
        try(FileChannel in = new FileInputStream(src).getChannel();
            FileChannel out = new FileOutputStream(dst).getChannel()){
            while(in.read(buffers) != -1){
                Arrays.stream(buffers).forEach(ByteBuffer::flip);
                out.write(buffers);
                Arrays.stream(buffers).forEach(ByteBuffer::clear);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}