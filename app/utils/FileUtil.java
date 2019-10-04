package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class FileUtil {

    /*public void read() {
        FileInputStream fin = null;
                 try {
                    fin = new FileInputStream(new File(pathname));
                    FileChannel channel = fin.getChannel();

                    int capacity = 1000;// 字节
                    ByteBuffer bf = ByteBuffer.allocate(capacity);
                    System.out.println("限制是：" + bf.limit() + ",容量是：" + bf.capacity() + " ,位置是：" + bf.position());
                    int length = -1;

                    while ((length = channel.read(bf)) != -1) {

                            *//*
     * 注意，读取后，将位置置为0，将limit置为容量, 以备下次读入到字节缓冲中，从0开始存储
     *//*
                               bf.clear();
                               byte[] bytes = bf.array();
                               System.out.println("start..............");

                               String str = new String(bytes, 0, length);
                               System.out.println(str);
                               //System.out.write(bytes, 0, length);

                               System.out.println("end................");

                               System.out.println("限制是：" + bf.limit() + "容量是：" + bf.capacity() + "位置是：" + bf.position());

                           }

                      channel.close();

                  } catch (FileNotFoundException e) {
                      e.printStackTrace();
                  } catch (IOException e) {
                      e.printStackTrace();
                  } finally {
                      if (fin != null) {
                             try {
                                     fin.close();
                                 } catch (IOException e) {
                                     e.printStackTrace();
                                 }
                         }
              }
    }*/

    public static final String source = "/Users/yibozheng/Desktop/高中2/Untitled-128";
    public static final String target = "";

    public static List<String> read(String filePath) throws IOException {
        List<String> list = new ArrayList<>();
        AtomicInteger i = new AtomicInteger();
        final StringBuffer sb = new StringBuffer();
        Files.lines(Paths.get(filePath)).forEach(l -> {
            // i start as 0
            i.getAndIncrement();
            System.out.println(i);
            if (i.intValue() % 2 == 1) {
                l = l.replaceAll(" Speech from TTS", "");
            } else {
                sb.append("@");
            }
            System.out.println("read line" + i + ": " + l);
            sb.append(l);
            System.out.println("sb="+sb);
            if (i.intValue() % 2 == 0) {
                list.add(sb.toString());
                sb.delete(0, sb.length());
            }
            /*String[] arrs = l.split("\t");
            for (int n=0;n<arrs.length;n++) {
                System.out.println(arrs[n]);
            }*/
        });
/*
        Files.lines(source).forEach(per -> {
            logger.info("line: {}", per);
            try {
                Files.write(target, Collections.singleton(per), StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });*/
        return list;
    }

    public void write() {

    }

    public static void main(String[] args) throws IOException {
        System.out.println(Files.exists(Paths.get(source)));
        read(source);
    }
}
