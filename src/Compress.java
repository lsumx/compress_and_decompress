import java.io.*;

import java.util.*;


public class Compress {
    private PriorityQueue<HuffmanTree> queue =null;
    Compare compare =new Compare();
    Huffman huffman = new Huffman();
    File inputFile, outputFile;
    BufferedOutputStream fileOutputStream;
    ObjectOutputStream objectOutputStream =null;
    Compress(File inputFile, File outputFile) throws IOException {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        fileOutputStream =new BufferedOutputStream(new FileOutputStream(outputFile));
        objectOutputStream =new ObjectOutputStream(fileOutputStream);
    }
    public void compress() throws IOException {
        compress(inputFile,inputFile.getName());
        close();
    }
    public void compress(File inputFile,String path) throws IOException {
        if (inputFile.exists()){
        if(inputFile.isFile()){
            if (inputFile.length() == 0){
                objectOutputStream.writeByte(6);
                objectOutputStream.writeObject(path);
                return;
            }
            queue=new PriorityQueue<>(12,compare);

            HashMap<Byte,String> map =new HashMap<>();
            int i,char_kinds=0;
            int char_temp,file_length=0;
            BufferedInputStream fileInputStream=null;


            int node_num;
            HuffmanTree[] hufTrees=null;
            String code_buf=null;//缓冲区

            TempNode [] tempNodes =new TempNode[256];
            for (i=0;i<256;++i){
                tempNodes[i]=new TempNode();
                tempNodes[i].weight=0;
                tempNodes[i].uch=(byte)i;
            }
            try {
                fileInputStream = new BufferedInputStream(new FileInputStream(inputFile));
                while ((char_temp =fileInputStream.read())!=-1){
                    ++tempNodes[char_temp].weight;
                    ++file_length;
                }
                fileInputStream.close();
                Arrays.sort(tempNodes);

                for (i=0;i<256;++i){
                    if (tempNodes[i].weight==0)
                        break;
                }
                char_kinds=i;
                objectOutputStream.writeByte(4);//表示文件

             if (char_kinds==1){
                    objectOutputStream.writeObject(path);//文件名字
                    objectOutputStream.writeInt(char_kinds);
                    objectOutputStream.writeByte(tempNodes[0].uch);
                    objectOutputStream.writeInt(tempNodes[0].weight);
                }else {
                    objectOutputStream.writeObject(path);//文件名字
                    node_num = 2*char_kinds-1;//计算哈夫曼树所有节点个数
                    hufTrees = new HuffmanTree[node_num];
                    for(i = 0; i < char_kinds; ++i){
                        hufTrees[i] = new HuffmanTree();
                        hufTrees[i].uch = tempNodes[i].uch;
                        hufTrees[i].weight = tempNodes[i].weight;
                        hufTrees[i].parent = 0;

                        hufTrees[i].index = i;
                        queue.add(hufTrees[i]);
                    }

                    for(; i < node_num; ++i){
                        hufTrees[i] = new HuffmanTree();
                        hufTrees[i].parent = 0;
                    }
                    //创建哈夫曼树
                    huffman.createTree(hufTrees, char_kinds, node_num,queue);
                    //生成哈夫曼编码
                    huffman.hufCode(hufTrees, char_kinds);
                    //写入字节种类
                    objectOutputStream.writeInt(char_kinds);
                    for(i = 0; i < char_kinds; ++i){
                        objectOutputStream.writeByte(hufTrees[i].uch);
                        objectOutputStream.writeInt(hufTrees[i].weight);

                        map.put(hufTrees[i].uch, hufTrees[i].code);
                    }
                    objectOutputStream.writeInt(file_length);
                    fileInputStream = new BufferedInputStream(new FileInputStream(inputFile));
                    code_buf = "";
                    //将读出的字节对应的哈夫曼编码转化为二进制存入文件
                    while((char_temp = fileInputStream.read()) != -1){

                        code_buf += map.get((byte)char_temp);

                        while(code_buf.length() >= 8){
                            char_temp = 0;
                            for(i = 0; i < 8; ++i){
                                char_temp <<= 1;// 左移一位，为下一个bit腾出位置
                                if(code_buf.charAt(i) == '1')
                                    char_temp |= 1;   //当编码为1，通过或操作添加到字节的最低位
                            }
                            objectOutputStream.writeByte((byte)char_temp);//存入文件
                            code_buf = code_buf.substring(8);
                        }
                    }
                    //最后编码长度不够8位的时候，用0补齐
                    if(code_buf.length() > 0){
                        char_temp = 0;
                        for(i = 0; i < code_buf.length(); ++i){
                            char_temp <<= 1;
                            if(code_buf.charAt(i) == '1')
                                char_temp |= 1;
                        }
                        char_temp <<= (8-code_buf.length());
                        objectOutputStream.writeByte((byte)char_temp);
                    }
                }
                fileInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            File files[] = inputFile.listFiles();
            if(files.length == 0){//空文件夹
                objectOutputStream.writeByte(3);
                objectOutputStream.writeObject(path + "\\");
            }else {
                for(File file:files){
                    compress(file,path + "\\" + file.getName());
                }
            }
        }

    }else {
            System.out.println("您输入的文件不存在");
        }
    }

    void close() throws IOException {
        objectOutputStream.writeByte(5);//文件末尾
        objectOutputStream.close();
//        fileOutputStream.flush();
        fileOutputStream.close();
    }





    


}
