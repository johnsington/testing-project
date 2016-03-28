import java.util.HashMap;

/**
 * Created by johnsington on 2016-03-14.
 */
public class NodeCollection {
    public HashMap<String, Node> _nodes;

    public NodeCollection(){
        this._nodes = new HashMap<>();
    }

    public void add(String n){
        if(!this._nodes.containsKey(n)){
            this._nodes.put(n, new Node(n));
        }
    }

    public void addCallSite(String n){
        if(!this._nodes.containsKey(n)){
            this._nodes.put(n, new Node(n, true));
        }
    }

    public Node getNode(String s){
        return this._nodes.containsKey(s)?  this._nodes.get(s) : null;
    }
}
