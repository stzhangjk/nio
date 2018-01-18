package com.stzhangjk.demo.nio;

import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;

/**
 * @author zhjk on 2018/1/18.
 */
public class TestBuffer {
    @Test
    public void testRead() {
        String file = ClassLoader.getSystemResource("test.txt").getFile();
        try (FileChannel in = new FileInputStream(file).getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            StringBuilder sb = new StringBuilder();
            while (in.read(buffer) != -1) {
                buffer.flip();
                byte[] temp = new byte[buffer.remaining()];
                buffer.get(temp);
                String tmp = new String(temp);
                sb.append(tmp);
                buffer.clear();
            }
            System.out.println(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWrite() {
        File file = new File(ClassLoader.getSystemResource("").getFile() + "testWrite.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileChannel out = new FileOutputStream(file).getChannel()) {
            byte[] content = "test string hello world".getBytes();
            ReadableByteChannel in = Channels.newChannel(new ByteArrayInputStream(content));
            ByteBuffer buffer = ByteBuffer.allocate(10);
            while (in.read(buffer) != -1) {
                buffer.flip();
                out.write(buffer);
                buffer.clear();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCopy() {
        String src = ClassLoader.getSystemResource("test.txt").getFile();
        String dst = ClassLoader.getSystemResource("").getFile() + "testCopy.txt";
        try(FileChannel in = new FileInputStream(src).getChannel();
            FileChannel out = new FileOutputStream(dst).getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            while (in.read(buffer) != -1) {
                buffer.flip();
                out.write(buffer);
                buffer.clear();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMappedBuffer() {
        File file = new File(ClassLoader.getSystemResource("").getFile() + "testBigFile.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try (FileChannel out = new FileInputStream(file).getChannel()) {
            MappedByteBuffer buffer = out.map(FileChannel.MapMode.READ_WRITE, 0, 1024);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCompact() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[]{1, 2, 3, 4, 5});
        buffer.flip();
        System.out.println(buffer.get());  //1
        System.out.println(buffer.get());  //2
        System.out.println(buffer.get());  //3
        buffer.compact();
//        buffer.flip();
        buffer.put(new byte[]{6, 7, 8});
        buffer.flip();
        byte[] tmp = new byte[buffer.remaining()];
        buffer.get(tmp);
        System.out.println(Arrays.toString(tmp));//45678
        buffer.clear();
    }

    @Test
    public void testMarkReset(){
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[]{1, 2, 3, 4, 5});
        buffer.flip();
        System.out.println(buffer.get());//1
        buffer.mark();
        System.out.println(buffer.get());//2
        System.out.println(buffer.get());//3
        buffer.reset();
        System.out.println(buffer.get());//2
        System.out.println(buffer.get());//3

        ByteBuffer b = ByteBuffer.allocate(10);
        b.put((byte)1);
        b.mark();
        b.put((byte)2);
        b.put((byte)3);
        b.reset();
        b.put((byte)4);
        b.put((byte)5);

        b.flip();
        byte[] tmp = new byte[b.remaining()];
        b.get(tmp);
        System.out.println(Arrays.toString(tmp));  //145
    }
}

