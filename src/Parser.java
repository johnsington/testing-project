/**
 * Created by jesshanta on 2016-03-14.
 */

import com.sun.deploy.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;


public class Parser {
    private String filename;
    private ArrayList<String> functionNames = new ArrayList<String>();
    private HashMap<String, Node> functionNodes = new HashMap<String, Node>();

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
                    String functionName = split[5].substring(1, split[5].length()-2); //to get rid of single quotes
                    functionNames.add(functionName);
                }
            }
            //Close the input stream
            in.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readNewFunction(String fName, BufferedReader br){
        String line;
        HashMap<String, Integer> names = new HashMap<String, Integer>();
        try {
            while ((line = br.readLine()) != null) {
                //it is a function definition, we want to make a new node
                if(line.contains("function")) {
                    String[] split = line.split(" "); //alt \\s+
                    String name = split[3].substring(1, split[5].length() - 2);

                    //we only want unique function calls per function
                    if (names.get(name) == null) {
                        //check to see if node has already occurred
                        Node n = functionNodes.get(name);
                        //we need to create a new node
                        if (n == null) {
                            n = new Node(name);
                            n.increment();
                        } else {
                            n.increment();
                        }
                        //update function map with node/new node value
                        functionNodes.put(name, n);
                        names.put(name, 0);
                    }
                }
                //and marks end of a function
                if(line.contains("and")){
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
