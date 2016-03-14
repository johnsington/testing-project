/**
 * Created by johnsington on 2016-03-14.
 */
public class Node {
    private String name;
    private int count;

    public Node(String f){
        this.name = f;
        this.count = 0;
    }

    public void increment(){
        this.count++;
    }

    public String getName(){
        return this.name;
    }

    public Boolean equals(String f){
        return this.name.equals(f);
    }

    public int getCount(){
        return this.count;
    }
}
