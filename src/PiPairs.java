import java.util.ArrayList;
import java.util.HashMap;

public class PiPairs {
	public static HashMap<PiPairs, Integer> pair = new HashMap<PiPairs, Integer>();
	public static ArrayList<PiPairs> pairs = new ArrayList<PiPairs>();
	int id1, id2;
	int confidence; 
	
	public PiPairs(int id1_, int id2_){
		id1=id1_;
		id2=id2_;
		confidence=1;
	}
	
	public void add(){
		confidence++;
	}
	
	public boolean equals(PiPairs o){
        //a node is equal if o.id1=this.id1 and o.id2=this.id2 or o.id2=this.id1 and o.id1=this.id2 
		return o!=null && ((o.id1==this.id1 && o.id2== this.id2)||(o.id2==this.id1&&o.id1==this.id2));
	}
	
}
