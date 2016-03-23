/**
 * Created by jesshanta on 2016-03-14.
 */

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;


public class Parser {
    private String filename;
    private NodeCollection callSites = new NodeCollection();
    private NodeCollection functionNodes = new NodeCollection();

    Parser(String filename_) {
        filename = filename_;
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
                    System.out.println("functionName: " + functionName);
                    callSites.addCallSite(functionName);
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
    }

    private void readNewFunction(String fName, BufferedReader br){
        HashMap<String, Integer> names = new HashMap<String, Integer>();

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
}
