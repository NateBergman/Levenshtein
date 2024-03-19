import java.io.*; //Levenshtein by Nate Bergman
import java.util.*;
public class BothSideNoFile {
    public static void main (String[] args) throws FileNotFoundException {
        ArrayList<String> words = new ArrayList<String>(); //list of all words
        int[] lengths = new int[36]; //sores indexes of where the lengths change
        Arrays.fill(lengths,-1);
        Scanner dictionary = new Scanner(new File("src/DictionarySortedByLength"));
        while (dictionary.hasNext()) {
            words.add(dictionary.next().toLowerCase());
        }
        int longest = words.get(words.size() - 1).length(); //build map of what index each length is at
        lengths[words.get(0).length()] = 0;
        for (int i = 1; i < words.size(); i++) {
            if (words.get(i).length() > words.get(i - 1).length()) {
                lengths[words.get(i).length()] = i;
            }
        }
        lengths[longest + 2] = words.size();
        for (int i = longest + 1; i > -1; i--) {
            if (lengths[i] == -1) {
                lengths[i] = lengths[i + 1];
            }
        }

        Map<String,Set<String>> previous = new HashMap<>(); //shows what words can lead to this one in a shortest path
        Map<String,Integer> depths = new HashMap<>(); //positive represents from start, negative from end, 0 is a bridge
        Queue<String> queue1 = new LinkedList<>();
        Queue<String> queue2 = new LinkedList<>();

        Scanner console = new Scanner(System.in); //gets starting and ending words

        System.out.print("Starting word : ");
        String first = console.next();
        queue1.add(first);
        previous.put(first,new HashSet<>());
        depths.put(first,1);

        System.out.print("Ending word : ");
        String end = console.next();
        queue2.add(end);
        previous.put(end,new HashSet<>());
        depths.put(end,-1);

        int prevDepth1 = 1;
        int prevDepth2 = -1;
        boolean found = false;
        while (!queue1.isEmpty() && !queue2.isEmpty()) {
            if (queue1.size() > queue2.size()) { //search from queue2
                while (!queue2.isEmpty()) {
                    String word = queue2.element();
                    int depth = depths.get(word);
                    if (depth != prevDepth2) { //only goes one line at a time
                        System.out.println(prevDepth2);
                        prevDepth2--;
                        break;
                    }
                    Set<String> moves = getAdjacents(word,words,lengths);
                    for (String s : moves) {
                        if (!depths.containsKey(s)) { //unexplored words
                            previous.put(s, new HashSet<>());
                            previous.get(word).add(s);
                            queue2.add(s);
                            depths.put(s,depth - 1);
                        } else if (depth - 1 == depths.get(s)) {
                            previous.get(word).add(s);
                        } else if (depths.get(s) >= 0) {
                            depths.put(s,0);
                            previous.get(word).add(s);
                            queue1.clear();
                            found = true;
                        }
                    }
                    queue2.remove();
                }
            } else { //search from queue1
                while (!queue1.isEmpty()) {
                    String word = queue1.element();
                    int depth = depths.get(word);
                    if (depth != prevDepth1) { //only goes one line at a time
                        System.out.println(prevDepth1);
                        prevDepth1++;
                        break;
                    }
                    Set<String> moves = getAdjacents(word,words,lengths);
                    for (String s : moves) {
                        if (!depths.containsKey(s)) { //unexplored words
                            previous.put(s, new HashSet<>());
                            previous.get(s).add(word);
                            queue1.add(s);
                            depths.put(s,depth + 1);
                        } else if (depth + 1 == depths.get(s)) {
                            previous.get(s).add(word); //there might be multiple equally short paths to the same word
                        } else if (depths.get(s) <= 0) {
                            depths.put(s,0);
                            previous.get(s).add(word);
                            queue2.clear();
                            found = true;
                        }
                    }
                    queue1.remove();
                }
            }
        }
        if (!found) {
            System.out.println("No paths!");
        } else {
            System.out.println("digraph something{concentrate=true;");
            int pathCount = printPaths("",end,first,previous);
            System.out.println('}');
            System.out.print("Paths: " + pathCount);
        }
    }
    public static int printPaths (String currentString, String input, String start, Map<String,Set<String>> previous) {
        if (input.equals(start)) { //recursively turns the map of previous words into a bunch of paths and gets count
            System.out.println(input + currentString);
            return 1;
        } else {
            int pathCount = 0;
            String newCurrent = " -> " + input + currentString;
            for (String s : previous.get(input)) {
                pathCount += printPaths(newCurrent,s,start,previous);
            }
            return pathCount;
        }
    }
    public static Set<String> getAdjacents (String word, ArrayList<String> words, int[] lengths) { //returns all valid moves
        Set<String> moves = new HashSet<>();
        for (int i = lengths[word.length() - 1]; i < lengths[word.length() + 2]; i++) {
            String newWord = words.get(i); //goes through every word same length, 1 shorter, or 1 longer and...
            if (!word.equals(newWord) && isAdjacent(newWord,word)) { //tests if they are one character away and if so...
                moves.add(words.get(i)); //adds them to the list of legal moves
            }
        }
        return moves;
    }
    public static boolean isAdjacent (String s1, String s2) { //CAN ONLY CALL ON WORDS SAME SIZE OR 1 SIZE DIFFERENT
        if (s1.length() == s2.length()) {//if the words are same length it's simple, just comapre each and if there's >1 mismatch they don't work
            boolean mismatch = false;
            int l = s1.length();
            for (int i = 0; i < l; i++) {
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
            int l1 = s1.length();
            int l2 = s2.length();
            while (j < l2) { //algorithm increments both words if characters are the same, only longer word if they're different
                if (i == l1) { //assumes words are only one off and so if longer finishes before smaller there's 2+ mismatches
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
