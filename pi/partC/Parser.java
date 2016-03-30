/**
 * Created by jesshanta on 2016-03-14.
 */

import java.io.*;
import java.text.DecimalFormat;
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
    public int t_depth = 1;

    public Parser(String filename_) {
        this.filename = filename_;
    }
    
    public Parser(String filename_, int t_depth) {
        this.filename = filename_;
        this.t_depth=t_depth;
    }

    public Parser(String filename_, int T_SUPPORT, double T_CONFIDENCE){
        this(filename_);
        this.t_support = T_SUPPORT;
        this.t_confidence = T_CONFIDENCE / 100;
    }

    public Parser(String filename_, int T_SUPPORT, double T_CONFIDENCE, int t_depth){
        this(filename_);
        this.t_support = T_SUPPORT;
        this.t_confidence = T_CONFIDENCE / 100;
        this.t_depth=t_depth;
    }
    
    public void readFile() {
    	try {
            // Open the file that is the first
            // command line parameter
            FileReader fstream = new FileReader(filename);
            BufferedReader br = new BufferedReader(fstream);
            String line;
            //Read File Line By Line
            while ((line = br.readLine()) != null) {
                //interpretLine(line);
                //new function occurs
                if(line.contains("Call graph node for function")){
		    int start = line.indexOf("\'")+1;
		    int end   = line.indexOf("\'", start);
                    String functionName = line.substring(start,end);
                    callSites.addCallSite(functionName);
                    readNewFunction(functionName, br);
                }
            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
//            System.out.println("functionNodes:");
//            for (Node n : functionNodes._nodes.values()){
//                System.out.println("  "+ n.getName() + " support: " + n.getSupport());
//            }
//
//            System.out.println("callSites (id): ");
//            for (Node n : callSites._nodes.values()){
//                System.out.println("  "+ n.getName());
//                System.out.println("  children:");
//                for( int j : n.childNodes.keySet()){
//                    System.out.println("    - "+ j);
//                }
//            }

        }
    	//interProceduralAnalysis();
    	analysis();
        computePairConfidence();
    }

    private void readNewFunction(String fName, BufferedReader br){

        try {
            String line = br.readLine();

            while (line != null){

                String[] split = line.split(" ");

                if (split.length != 6){
                    return;
                }

                //it is a function definition, we want to make a new node
                if(split[4].equals("function")) {
                    int start = line.indexOf("\'")+1;
                    int end   = line.indexOf("\'", start);
                    String name = line.substring(start,end);

                    //we only want unique function calls per function
                    functionNodes.add(name);

                    if (!callSites.getNode(fName).childNodes.containsKey(name)){
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
    
    private void analysis(){
    	
    	for(Node currCallsite : callSites._nodes.values()){
    		//first we need to do preprocessing and determine all of the reachable nodes from every callsite with the given depth
    		//then reachableNodes are the reachable nodes of a given node and is dependent on the depth
    		//we will add all nodes along a path to the starting nodes (callsite's) reachable node map
    		//then we will iterate through the values to update the currCallsites child nodes and update a nodes support if its new
    		int depth = t_depth+2; //we add 2 because it starts from the root and we interpret depth 1 as being the child's child so this guarantees it starts at 1
        	expand(currCallsite, currCallsite, depth);
        	
        	/*System.out.println("Reachable from: "+currCallsite.getName());
        	HashMap<String, Node> children = currCallsite.reachableNodes;
        	for(Node n: children.values()){
        		System.out.println("  " + n.getName());
        		
        	}*/	
    	}
    	//preprocesing is done, now we need to iterate through all the callsite's children and add the nodes that are in the reachable lists for all the children to the current callsites children
    	for(Node currCallsite: callSites._nodes.values()){
    		HashMap<String, Node> children = currCallsite.childNodes;
    		ArrayList<Node> addNode = new ArrayList<>();
    		//expand children and their reachable maps
    		for(Node child: children.values()){
    			Node realChild = callSites._nodes.get(child.getName());
    			for(Node reachableChild: realChild.reachableNodes.values()){
    				//need to check if this reachable is in the currCalsites child, it so we need to add it
    				if(!currCallsite.childNodes.containsKey(reachableChild.getName())){
    					Node n = functionNodes.getNode(reachableChild.getName());
    					addNode.add(n);
    					
    				}

    			}
    		}
    		//to avoid modifying currCallsite while iterating through the children, we use the addNode array as the buffer
    		//so we will add all of the nodes in addNode to child nodes of currCallsite
    		for(int i=0; i<addNode.size(); i++){
    			//check for duplicates again
    			if(!currCallsite.childNodes.containsKey(addNode.get(i).getName())){
    				Node n = addNode.get(i);
    				n.increment();
    				currCallsite.addChild(addNode.get(i));
    			}
    		}
    	}	
    }

    private void expand(Node starting, Node curr, int depth){
    	if(depth==0)
    		return;
    	
    	if(starting.getName()!=curr.getName()){
    		//System.out.println(starting.getName());
    		//we only want to add if the function actually exists
    		//if(functionNodes.getNode(starting.getName())!=null)
    			//functionNodes.getNode(starting.getName()).addReachableNode(curr);
    		starting.addReachableNode(curr); 
    	}
    	
    	//need to retrieve node if it is actually a callsite and is expandable
    	Node subCallsite = callSites.getNode(curr.getName());
    	if(subCallsite!=null){
    		HashMap<String, Node> children = subCallsite.childNodes;
    		//expand on children
	    	for(Node child: children.values()){
	    		//check if a child is a callsite, if so we want to expand it (the recursion)
	    		if(callSites.getNode(child.getName())!=null){
	    			expand(starting, child, depth-1);
	    		}
	    	}
    	}
    	
    }
    
    private void computePairConfidence(){
    	//iterate through the children of each callsite and increment their pipair count
    	for (Node curr : callSites._nodes.values()){
    		//need to iterate through all of currs children
    		HashMap<String, Node> children = curr.childNodes;
    		HashMap<String, Node> processed = new HashMap<>();
    		for(Node currChild: children.values()){
    			//iterate through all of the children again, this time making pipairs
    			for(Node pair: children.values()){
    				if(pair==currChild){
    					continue;
                    }
    				if(processed.containsKey(pair.getName())){
                        continue;
                    }
    				
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
    			processed.put(currChild.getName(), currChild);
    		}
    	}

        for(PiPairs p : pairs){
            if (p.getSupport() >= t_support){
                float n1_confidence = ((float)p.getSupport() / (float)p.n1.getSupport());
                float n2_confidence = ((float)p.getSupport() / (float)p.n2.getSupport());

                if(n1_confidence >= t_confidence){
                    p.setBug(p.n1, n1_confidence);
                }
                if(n2_confidence >= t_confidence){
                    p.setBug(p.n2, n2_confidence);
                }

                if(p.n1_is_bug || p.n2_is_bug){
                    for(Node callSite : callSites._nodes.values()){
                        if(p.n1_is_bug){
                            if(callSite.childNodes.containsKey(p.n1.getName()) && !callSite.childNodes.containsKey(p.n2.getName())){
                                reportBug(p, callSite, p.n1);
                            }
                        }
                        if(p.n2_is_bug){
                            if(callSite.childNodes.containsKey(p.n2.getName()) && !callSite.childNodes.containsKey(p.n1.getName())){
                                reportBug(p, callSite, p.n2);
                            }
                        }
                    }
                }
            }
        }

//        System.out.println(a + " callSites avoided");

//    	for(int i=0; i<pairs.size(); i++){
//    		PiPairs curr = pairs.get(i);
//    		System.out.println("id1 "+curr.n1.getName() + " id2 "+ curr.n2.getName() + " confidence " + curr.getSupport());
//    	}
    }

    void reportBug(PiPairs p, Node callSite, Node n){
    	System.out.println(String.format("bug: %s in %s, pair: (%s, %s), support: %d, confidence: %.2f%%", n.getName(),callSite.getName(), p.n1.getName(),p.n2.getName(),p.getSupport(), p.getConfidence(n)*100));
    }
    
}
