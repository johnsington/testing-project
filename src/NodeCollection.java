import java.util.HashMap;

/**
 * Created by johnsington on 2016-03-14.
 */
public class NodeCollection {
    public HashMap<String, Node> _nodes;

    public NodeCollection(){
        this._nodes = new HashMap<>();
    }

    public void add(Node n){
        if(!this._nodes.containsKey(n.toString())){
            this._nodes.put(n.toString(), n);
        }
    }

    public Node getNode(String s){
        return this._nodes.containsKey(s)?  this._nodes.get(s) : null;
    }
}
