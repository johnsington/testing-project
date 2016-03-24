import java.util.ArrayList;
import java.util.HashMap;

public class PiPairs {
	Node n1, n2;
	int support; 
	Boolean n1_is_bug, n2_is_bug;
    float n1_confidence, n2_confidence;

	public PiPairs(Node n1_, Node n2_){
		support=1;
		n1=n1_;
		n2=n2_;
        n1_is_bug = false;
        n2_is_bug = false;
        n1_confidence = 0;
        n2_confidence = 0;

        if (n2_.getName().compareTo(n1.getName()) < 0){
            n1 = n2_;
            n2 = n1_;
        }
	}
	
	public void setBug(Node n, float c){
        if(n == n1){
            n1_is_bug = true;
            n1_confidence = c;
        }
        else if (n == n2){
            n2_is_bug = true;
            n2_confidence = c;
        }
        else{
            return;
        }
    }

	public void add(){
		support++;
	}
	
	public int getSupport(){
		return support;
	}

    public float getConfidence(Node n){
        if(n == n1){
            return n1_confidence;
        }
        else if (n == n2){
            return n2_confidence;
        }
        else{
            return 0;
        }
    }
	
	public boolean equals(PiPairs o){
        //a node is equal if o.id1=this.id1 and o.id2=this.id2 or o.id2=this.id1 and o.id1=this.id2 
		return o!=null && ((o.n1.id==this.n1.id && o.n2.id== this.n2.id)||(o.n2.id==this.n1.id&&o.n1.id==this.n2.id));
	}
	
}
