import java.io.*;
import java.util.*;
public class ShortestPath {
    static ArrayList<String> words = new ArrayList<String>();
    static Map lengths = new HashMap<Integer,Integer>(); //gives you the index that each length starts at in the dictionary
    public static void main (String[] args) throws FileNotFoundException {
        Scanner dictionary = new Scanner(new File("src/DictionarySortedByLength")); //same as non-shortest
        while (dictionary.hasNext()) {
            words.add(dictionary.next().toLowerCase());
        }

        int longest = words.get(words.size() - 1).length(); //build map of what index each length is yay
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

        ArrayList<ArrayList<String>> paths = new ArrayList<>();
        ArrayList<ArrayList<String>> finalPaths = new ArrayList<>();
        paths.add(new ArrayList<>());
        Scanner console = new Scanner(System.in);
        System.out.print("Starting word : ");
        paths.get(0).add(console.next());
        System.out.print("Ending word : ");
        String end = console.next();

        int size = 0;
        boolean finished = false;
        while  (!finished && size < 20) {
            for (ArrayList<String> s : paths) {
                if (s.get(size).equals(end)) {
                    finished = true;
                    finalPaths.add(s);
                }
            }
            size++;
            paths = SearchNextDepth(paths);
        }
        System.out.println("Distance : " + size);
        System.out.println("Paths : " + finalPaths);
    }
    public static ArrayList<ArrayList<String>> SearchNextDepth (ArrayList<ArrayList<String>> paths) {
        ArrayList<ArrayList<String>> newPaths = new ArrayList<ArrayList<String>>();
        for (ArrayList<String> s : paths) {
            newPaths.addAll(wordSearch(s));
        }
        return newPaths;
    }
    public static ArrayList<ArrayList<String>> wordSearch (ArrayList<String> path) {
        ArrayList<ArrayList<String>> legalMoves = new ArrayList<ArrayList<String>>();
        String word = path.get(path.size() - 1);
        for (int i = (int) lengths.get(word.length() - 1); i < (int) lengths.get(word.length() + 2); i++) {
            if (isAdjacent(words.get(i),word) && !path.contains(words.get(i))) {
                ArrayList<String> newPath = new ArrayList<String>();
                newPath.addAll(path);
                newPath.add(words.get(i));
                legalMoves.add(newPath);
            }
        }
        return legalMoves;
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
