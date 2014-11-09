/**
 * Created by Philipp on 10/25/2014.
 */
public class Main {
    public static void main(String[] argv) {
        if (argv.length < 2) {
            System.err.println("Usage: converter <input_file.[xls|xlsx]> <database.db>");
            return;
        } else {
            Converter cnv = new Converter(argv[0], argv[1]);
            cnv.convert();
        }
    }
}
