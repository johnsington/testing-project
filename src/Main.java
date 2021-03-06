/**
 * Created by johnsington on 2016-03-21.
 */
public class Main {

    public static void main(final String args[]){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Parser parser;
                if (args.length > 0){

                    if(args.length == 3){
                        parser = new Parser(args[0], Integer.parseInt(args[1]), Double.parseDouble(args[2]));
                    }
                    else if (args.length==2){
                        parser = new Parser(args[0], Integer.parseInt(args[1]));
                    }
                    else if(args.length==4){
                    	parser = new Parser(args[0], Integer.parseInt(args[1]), Double.parseDouble(args[2]), Integer.parseInt(args[3]));
                    }
                    else{
                    	parser = new Parser(args[0]);
                    }
                    parser.readFile();
                }
                //only for testing purposes
                //parser = new Parser("test");
                //parser.readFile();
            }
        });
    }
}
