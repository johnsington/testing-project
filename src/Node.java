import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by johnsington on 2016-03-14.
 */
public class Node {
    private String name;
    private int support;
    public HashMap<Integer, Node> childNodes;
    public int id;
    static private int TOTAL_NODES = 0;
    public static HashMap<String, Integer> map = new HashMap<>();

    public Node(String f){
        this.name = f;
        this.support = 0;
        this.id = TOTAL_NODES++;

        this.map.put(this.name, this.id);
    }

    public Node(String f, Boolean isCallSite){
        this(f);

        if (isCallSite){
            this.childNodes = new HashMap<>();
        }
    }

    public void addChild(Node n){
        if (!this.childNodes.containsKey(n.id)){
            this.childNodes.put(n.id, n);
        }
    }

    public void increment(){
        this.support++;
    }

    public String getName(){
        return this.name;
    }

    public int getSupport(){
        return this.support;
    }

    @Override
    public String toString(){
        return this.name;
    }

    public boolean equals(Node o){
          return o!=null && o.id == this.id;
    }

}
