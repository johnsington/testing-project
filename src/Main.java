/**
 * Created by johnsington on 2016-03-21.
 */
public class Main {

    public static void main(final String args[]){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Parser parser = new Parser(args[0]);
                parser.readFile();
            }
        });
    }
}
