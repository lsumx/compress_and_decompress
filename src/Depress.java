import java.io.*;
import java.util.PriorityQueue;

public class Depress {
    private PriorityQueue<HuffmanTree> queue =null;
    File inputFile;
    File outputFile;
    Compare cmp = new Compare();
    BufferedInputStream fis;
    ObjectInputStream ois;
    String base;
    Depress(File inputFile, String base) throws IOException {
        this.inputFile = inputFile;
        this.base = base;//解压目录
        fis = new BufferedInputStream(new FileInputStream(inputFile));
        ois = new ObjectInputStream(fis);
    }

    public void extract() throws IOException, ClassNotFoundException {
        byte type;//
        while((type = ois.readByte()) != 5){
            String path = (String)ois.readObject();
            check(path);
            if(type == 6){
                System.out.println(path);
                File newFile = new File(base + "\\" + path);
                if(newFile.exists()){
                    newFile.delete();
                }
                newFile.createNewFile();
            }
            else if(type == 4){
                outputFile = new File(base + "\\" + path);
                Huffman huffman =new Huffman();
                queue = new PriorityQueue<HuffmanTree>(12,cmp);

                int i;
                int file_len = 0;
                int writen_len = 0;
                BufferedOutputStream fos = null;


                int char_kinds = 0;
                int node_num;
                HuffmanTree[] huf_tree = null;
                byte code_temp;
                int root;
                try{
                    fos = new BufferedOutputStream(new FileOutputStream(outputFile));

                    char_kinds = ois.readInt();
                    //字节只有一种的情况
                    if(char_kinds == 1){
                        code_temp = ois.readByte();
                        file_len = ois.readInt();
                        while((file_len--) != 0){
                            fos.write(code_temp);
                        }
                        //字节多于一种的情况
                    }else{
                        node_num = 2 * char_kinds - 1; //计算哈夫曼树所有节点个数
                        huf_tree = new HuffmanTree[node_num];
                        for(i = 0; i < char_kinds; ++i){
                            huf_tree[i] = new HuffmanTree();
                            huf_tree[i].uch = ois.readByte();
                            huf_tree[i].weight = ois.readInt();
                            huf_tree[i].parent = 0;

                            huf_tree[i].index = i;
                            queue.add(huf_tree[i]);
                        }
                        for(;i < node_num; ++i){
                            huf_tree[i] = new HuffmanTree();
                            huf_tree[i].parent = 0;
                        }
                        huffman.createTree(huf_tree, char_kinds, node_num,queue);

                        file_len = ois.readInt();
                        root = node_num-1;
                        while(true){
                            code_temp = ois.readByte();
                            for(i = 0; i < 8; ++i){//从根向下知道叶子结点正向匹配对应字符
                                if((code_temp & 128) == 128){  //按位与操作，是压缩是按位或的逆过程，最高位是1，向右走
                                    root = huf_tree[root].rightChild;
                                }else{
                                    root = huf_tree[root].leftChild;
                                }

                                if(root < char_kinds){  //到达叶子结点
                                    fos.write(huf_tree[root].uch);
                                    ++writen_len; //记录文件长度
                                    if(writen_len == file_len) break;
                                    root = node_num - 1; //恢复为根节点的下标，匹配下一个字节
                                }
                                code_temp <<= 1;   //将编码缓存的下一位移到最高位，供匹配
                            }
                            //在压缩的时候如果最后一个哈夫曼编码位数不足八位则补0
                            //在解压的时候，补上的0之前的那些编码肯定是可以正常匹配到和他对应的字节
                            //所以一旦匹配完补的0之前的那些编码，写入解压文件的文件长度就和压缩之前的文件长度是相等的
                            //所以不需要计算补的0的个数
                            if(writen_len == file_len) break;
                        }
                    }
                    fos.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        fis.close();

    }

    void check(String path){//检查目录结构是否存在
        int length = path.length();
        int start = 0;
        int end = 0;
        for(int i = 0; i < length; i++){
            if(path.charAt(i) == '\\'){
                end = i;
                File file = new File(base + "\\" + path.substring(0,end));
                if(!file.exists()){
                    file.mkdir();
                }
            }
        }
    }
}
