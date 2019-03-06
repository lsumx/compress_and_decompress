import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main (String[] args) throws IOException, ClassNotFoundException {

        Scanner scanner =new Scanner(System.in);
        System.out.println("请选择您想要进行的操作：输入1进行压缩；输入2进行解压,输入3退出");
        String str =scanner.nextLine();
        if (str.equals("1")){
            compress();
            main(args);
        }
        else if (str.equals("2")){
            extract();
            main(args);
        }
        else if (str.equals("3")){
            System.exit(0);
        }else {
            System.out.println("无法识别");
            main(args);
        }


    }

    public static void compress() throws IOException {
        Scanner scanner =new Scanner(System.in);
        System.out.println("输入您想要压缩的文件（输入绝对路径）：");
        String string = scanner.nextLine();
        System.out.println("输入压缩后的文件名称（绝对路径）：");
        String zip =scanner.nextLine();
        File file1 =new File(string);
        File file2 =new File(zip);
        Compress compress =new Compress(file1,file2);
        Long time1 =System.currentTimeMillis();
        compress.compress();
        long time2 =System.currentTimeMillis();
        System.out.println("压缩时间为"+(time2-time1)+"ms");

    }

    public static void extract() throws IOException, ClassNotFoundException {
        Scanner scanner =new Scanner(System.in);
        System.out.println("输入您想要解压的文件（绝对路径）");
        String zip =scanner.nextLine();
        File file1 =new File(zip);
        System.out.println("输入解压后文件的位置");
        String string =scanner.nextLine();
        Depress depress =new Depress(file1,string);
        Long time1 =System.currentTimeMillis();
        depress.extract();
        Long time2 =System.currentTimeMillis();
        System.out.println("解压缩时间为"+(time2-time1)+"ms");

    }

//    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        File file =new File("C:\\Users\\bhlb\\Desktop\\1.docx");
//        String string ="C:\\Users\\bhlb\\Desktop\\17302010075.zip";
//        File file1 = new File(string);
//        Compress compress =new Compress(file,file1);
//        Long time =System.currentTimeMillis();
//        compress.compress();
//        Long time0 =System.currentTimeMillis();
//        System.out.println("解压缩时间为"+(time0-time)+"ms");
//        Depress depress = new Depress(file1,"C:\\Users\\bhlb\\Desktop\\test");
//        Long time1 =System.currentTimeMillis();
//        depress.extract();
//        Long time2 =System.currentTimeMillis();
//        System.out.println("解压缩时间为"+(time2-time1)+"ms");
//
//    }
}
