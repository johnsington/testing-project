import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by johnsington on 2016-03-14.
 */
public class Node {
    private String name;
    private int support;
    public HashMap<String, Node> childNodes;

    public Node(String f){
        this.name = f;
        this.support = 0;
    }

    public Node(String f, Boolean isCallSite){
        this(f);

        if (isCallSite){
            this.childNodes = new HashMap<>();
        }
    }

    public void addChild(Node n){
        if (!this.childNodes.containsKey(n.getName())){
            this.childNodes.put(n.getName(), n);
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

    @Override
    public boolean equals(Object o){

        return o!=null && this.name.equals(o.toString());
    }

}
