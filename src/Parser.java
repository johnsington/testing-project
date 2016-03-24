/**
 * Created by jesshanta on 2016-03-14.
 */

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.Stack;


public class Parser {
    private String filename;
    private NodeCollection callSites = new NodeCollection();
    private NodeCollection functionNodes = new NodeCollection();
    private ArrayList<PiPairs> pairs = new ArrayList<>();

    private ArrayList<PiPairs> bugPairs = new ArrayList<>();

    public int t_support = 3;
    public double t_confidence = 0.65;

    public Parser(String filename_) {
        this.filename = filename_;
    }

    public Parser(String filename_, int T_SUPPORT, int T_CONFIDENCE){
        this(filename_);
        this.t_support = T_SUPPORT;
        this.t_confidence = T_CONFIDENCE / 100;
    }

    public void readFile() {
    	try {
            // Open the file that is the first
            // command line parameter
        	FileInputStream fstream = new FileInputStream(filename);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            //Read File Line By Line
            while ((line = br.readLine()) != null) {
                //interpretLine(line);
                //new function occurs
                if(line.contains("Call graph node for function")){
                    String[] split = line.split(" "); //alt \\s+
                    String functionName = split[5].substring(1, split[5].length()-14); //to get rid of single quotes
                    callSites.addCallSite(functionName);
                    readNewFunction(functionName, br);
                }
            }
            //Close the input stream
            in.close();




        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            System.out.println("functionNodes:");
            for (Node n : functionNodes._nodes.values()){
                System.out.println("  "+ n.getName() + " support: " + n.getSupport());
            }

            System.out.println("callSites (id): ");
            for (Node n : callSites._nodes.values()){
                System.out.println("  "+ n.getName());
                System.out.println("  children:");
                for( int j : n.childNodes.keySet()){
                    System.out.println("    - "+ j);
                }
            }

        }
        computePairConfidence();
    }

    private void readNewFunction(String fName, BufferedReader br){

        try {
            String line = br.readLine();

            while (line != null){

                String[] split = line.split(" ");
                System.out.println(line);

                if (split.length < 6){
                    return;
                }

                for(String i : split){
                    System.out.println("+ " + i);
                }

                //it is a function definition, we want to make a new node
                if(split[4].equals("function")) {
                    String name = split[5].substring(1, split[5].length() - 1);

                    //we only want unique function calls per function
                    functionNodes.add(name);

                    if (!callSites.getNode(fName).childNodes.containsKey(Node.map.get(name))){
                        //register fn to call site and increment support
                        Node n = functionNodes.getNode(name);
                        callSites.getNode(fName).addChild(n);
                        n.increment();
                    }
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void computePairConfidence(){
    	//iterate through the children of each callsite and increment their pipair count
    	
    	for (Node curr : callSites._nodes.values()){
    		//need to iterate through all of currs children
    		HashMap<Integer, Node> children = curr.childNodes;
    		HashMap<Node, Integer> processed = new HashMap<Node,Integer>();
    		for(Node currChild: children.values()){
    			//iterate through all of the children again, this time making pipairs
    			for(Node pair: children.values()){
    				if(pair==currChild)
    					continue;
    				if(processed.containsKey(pair))
    					continue;
    				
    				
    				PiPairs nPair = new PiPairs(currChild, pair);
    				//check to see if this pair is already contained if it is then we just increment
    				//else we add
    				boolean found = false;
    				for(int i=0; i<pairs.size(); i++){
    					if(pairs.get(i).equals(nPair)){
    						//contained so increment confidence
    						pairs.get(i).add();
    						found=true;
    						break;
    					}
    				}
    				if(!found){
    					pairs.add(nPair);
    				}
    			}
    			processed.put(currChild, currChild.id);
    		}
    	}

        for(PiPairs p : pairs){
            if (p.getSupport() >= t_support){
                float n1_confidence = p.getSupport() / p.n1.getSupport();
                float n2_confidence = p.getSupport() / p.n2.getSupport();

                if(n1_confidence >= t_confidence){
                    p.setBug(p.n1, n1_confidence);
                }
                if(n2_confidence >= t_confidence){
                    p.setBug(p.n2, n2_confidence);
                }

                if(p.n1_is_bug || p.n2_is_bug){
                    for(Node callSite : callSites._nodes.values()){
                        if(p.n1_is_bug){
                            if(callSite.childNodes.containsKey(p.n1.id) && !callSite.childNodes.containsKey(p.n2.id)){
                                reportBug(p, callSite, p.n1);
                            }
                        }
                        if(p.n2_is_bug){
                            if(callSite.childNodes.containsKey(p.n2.id) && !callSite.childNodes.containsKey(p.n1.id)){
                                reportBug(p, callSite, p.n2);
                            }
                        }
                    }
                }
            }
        }

    	for(int i=0; i<pairs.size(); i++){
    		PiPairs curr = pairs.get(i);
    		System.out.println("id1 "+curr.n1.getName() + " id2 "+ curr.n2.getName() + " confidence " + curr.getSupport());
    	}
    }

    void reportBug(PiPairs p, Node callSite, Node n){
        System.out.println("bug: " + n.getName() + " in " + callSite.getName() + ", pair: (" +p.n1.getName()
                + ", " + p.n2.getName()+"), support: " + p.getSupport() + ", confidence: " + p.getConfidence(n)*100 + "%");
    }
    
}
