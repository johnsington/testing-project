/**
 * Created by jesshanta on 2016-03-14.
 */

import com.sun.deploy.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
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
                    String functionName = split[5].substring(1, split[5].length()-13); //to get rid of single quotes
                    callSites.add(new Node(functionName, true));
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
    }

    private void readNewFunction(String fName, BufferedReader br){
        HashMap<String, Integer> names = new HashMap<String, Integer>();

        try {
            String line = br.readLine();

            while (line != null || line.equals("and\n")){
                String[] split = line.split(" ");
                //it is a function definition, we want to make a new node
                if(split[2].equals("function")) {
                    String name = split[3].substring(1, split[5].length() - 2);

                    //we only want unique function calls per function
                    functionNodes.add(new Node(name));

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
}
