import java.io.*;
import java.util.*;
public class ShortestPath {
    static int currentBestSize = Integer.MAX_VALUE;
    static List<String[]> finalPaths = new ArrayList<>();
    static ArrayList<String> words = new ArrayList<String>();
    static Map lengths = new HashMap<Integer,Integer>();
    static String end;
    public static void main (String[] args) throws FileNotFoundException {
        Scanner dictionary = new Scanner(new File("src/DictionarySortedByLength"));
        while (dictionary.hasNext()) {
            words.add(dictionary.next().toLowerCase());
        }

        int longest = words.get(words.size() - 1).length(); //build map of what index each length is
        lengths.put(words.get(0).length(),0);
        for (int i = 1; i < words.size(); i++) {
            if (words.get(i).length() > words.get(i - 1).length()) {
                lengths.put(words.get(i).length(),i);
            }
        }
        lengths.put(longest + 2, words.size());
        for (int i = longest + 1; i > -1; i--) {
            if (!lengths.containsKey(i)) {
                lengths.put(i,lengths.get(i + 1));
            }
        }

        Scanner console = new Scanner(System.in);
        System.out.print("Starting word : ");
        String current = console.next();
        System.out.print("Ending word : ");
        end = console.next();
    }
    public static void evaluateNodes (String[] path) {
        //if end, add path and set pb
        //terminate if depth >= pb
        //otherwise, get moves, rank them, and recursively call this in order with those added to the path
        //perhaps make a comparator to sort them and rank by this.
    }
    public static boolean isAdjacent (String s1, String s2) { //CAN ONLY CALL ON WORDS SAME SIZE OR 1 SIZE DIFFERENT
        if (s1.length() == s2.length()) {//if the words are same length it's simple, just comapre each and if there's >1 mismatch they don't work
            boolean mismatch = false;
            for (int i = 0; i < s1.length(); i++) {
                if (s1.charAt(i) != s2.charAt(i)) {
                    if (mismatch) {
                        return false;
                    } else {
                        mismatch = true;
                    }
                }
            }
        } else {
            if (s1.length() < s2.length()) { //1 is always longer, so if 2 input is need to flip them.
                String s3 = s2;
                s2 = s1;
                s1 = s3;
            }
            int i = 0;
            int j = 0;
            while (j < s2.length()) { //algorithm increments both words if characters are the same, only longer word if they're different
                if (i == s1.length()) { //assumes words are only one off and so if longer finishes before smaller there's 2+ mismatches
                    return false;
                }
                if (s1.charAt(i) == s2.charAt(j)) {
                    j++;
                }
                i++;
            }
        }
        return true; //if not enough mismatches then they work
    }
}
