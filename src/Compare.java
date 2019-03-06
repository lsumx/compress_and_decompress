import java.util.Comparator;

//实现优先队列
public class Compare implements Comparator<HuffmanTree> {
    public int compare(HuffmanTree t1,HuffmanTree t2){
        if (t1.weight>t2.weight)
            return 1;
        if (t1.weight<t2.weight)
            return -1;
        return 0;
    }
}
