public class Node implements Comparable<Node>
{
    int freq;
    char character;
    Node left;
    Node right;

    Node(char character, int freq, Node left, Node right)
    {
        this.character = character;
        this.freq = freq;
        this.left = left;
        this.right = right;
    }

    boolean isLeaf()
    {
        return left == null && right == null;
    }


    @Override
    public int compareTo(Node o)
    {
        return this.freq - o.freq;
    }


}
