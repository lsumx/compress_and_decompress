class TempNode implements Comparable<TempNode>{
    public byte uch;
    public int weight;//频度
    @Override
    public int compareTo(TempNode tempNode){
        if (this.weight<tempNode.weight)
            return 1;
        else if (this.weight>tempNode.weight)
            return -1;
        return 0;
    }

}
