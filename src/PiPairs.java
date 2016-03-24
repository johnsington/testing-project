import java.util.ArrayList;
import java.util.HashMap;

public class PiPairs {
	Node n1, n2;
	int confidence; 
	
	public PiPairs(Node n1_, Node n2_){
		confidence=1;
		n1=n1_;
		n2=n2_;
	}
	
	
	public void add(){
		confidence++;
	}
	
	public boolean equals(PiPairs o){
        //a node is equal if o.id1=this.id1 and o.id2=this.id2 or o.id2=this.id1 and o.id1=this.id2 
		return o!=null && ((o.n1.id==this.n1.id && o.n2.id== this.n2.id)||(o.n2.id==this.n1.id&&o.n1.id==this.n2.id));
	}
	
}
