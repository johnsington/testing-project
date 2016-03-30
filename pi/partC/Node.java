import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by johnsington on 2016-03-14.
 */
public class Node {
    private String name;
    private int support;
    private Boolean isCallsite;
    public HashMap<String, Node> childNodes;
    public HashMap<String, Node> reachableNodes = new HashMap<String, Node>();

    public Node(String f){
        this.name = f;
        this.support = 0;
        this.isCallsite=false;
    }

    public Node(String f, Boolean isCallSite){
        this(f);
        this.isCallsite=isCallSite;
        if (isCallSite){
            this.childNodes = new HashMap<>();
        }
    }

    public void addChild(Node n){
        if (!this.childNodes.containsValue(n)){
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

    public boolean isCallsite(){
    	return isCallsite;
    }
    
    public boolean equals(Node o){
          return o!=null && (this.getName().equals(o.getName()));
    }
    
    public void addReachableNode(Node n){
    	 if (!this.reachableNodes.containsValue(n)){
             this.reachableNodes.put(n.getName(), n);
         }
    }

}
