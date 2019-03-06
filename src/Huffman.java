import java.util.PriorityQueue;

public class Huffman {
    public void createTree(HuffmanTree[] huf_tree, int char_kinds, int node_num, PriorityQueue<HuffmanTree> queue){
        int i;
        int[] arr = new int[2];
        for(i = char_kinds; i < node_num; ++i){
            arr[0] = queue.poll().index;
            arr[1] = queue.poll().index;
            huf_tree[arr[0]].parent = huf_tree[arr[1]].parent = i;
            huf_tree[i].leftChild = arr[0];
            huf_tree[i].rightChild = arr[1];
            huf_tree[i].weight = huf_tree[arr[0]].weight + huf_tree[arr[1]].weight;

            huf_tree[i].index = i;
            queue.add(huf_tree[i]);
        }
    }

    //获取哈夫曼编码
    public void hufCode(HuffmanTree[] huf_tree, int char_kinds) {
        int i;
        int cur, next;

        for (i = 0; i < char_kinds; ++i) {
            String code_tmp = "";
            for (cur = i, next = huf_tree[i].parent; next != 0; cur = next, next = huf_tree[next].parent) {
                if (huf_tree[next].leftChild == cur)
                    code_tmp += "0";
                else
                    code_tmp += "1";
            }
            huf_tree[i].code = (new StringBuilder(code_tmp)).reverse().toString();
        }
    }
}
