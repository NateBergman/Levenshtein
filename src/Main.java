// Make it work with monkey to business
import java.util.*;
import java.io.*;
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner dictionary = new Scanner(new File("src/DictionarySortedByLength"));
        ArrayList<String> words = new ArrayList<String>();
        while (dictionary.hasNext()) {
            words.add(dictionary.next().toLowerCase());
        }

        int longest = words.get(words.size() - 1).length(); //build map of what index each length is
        Map lengths = new HashMap<Integer,Integer>();
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
        String end = console.next();
        ArrayList<String> pastMoves = new ArrayList<String>();

        while(!current.equals(end)) {
            ArrayList<String> moves = new ArrayList<String>();
            for (int i = (int) lengths.get(current.length() - 1); i < (int) lengths.get(current.length() + 2); i++) {
                if (isAdjacent(current, words.get(i)) && !pastMoves.contains(words.get(i))) {
                    moves.add(words.get(i));
                }
            }
            Random r = new Random();
            current = moves.get(r.nextInt(moves.size())); //need to make it backtrack and actualy go towards the goal

            System.out.print(current);
            pastMoves.add(current);
            console.next();
        }
    }
    public static int scoreGuess (String guess, String end) {
        int score = -1 * Math.abs(guess.length() - end.length());
        for (int i = 0; i < guess.length() && i < end.length(); i++) {
            if (guess.charAt(i) == end.charAt(i)) {
                score++;
            }
        }
        return score;
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