import java.io.*; //Levenshtein by Nate Bergman
import java.util.*;
public class NoFileOneSideNoNodeShortestPath {
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
        Queue<String> queue = new LinkedList<>();
        Map<String,Integer> depths = new HashMap<>();

        Scanner console = new Scanner(System.in); //gets starting and ending words
        System.out.print("Starting word : ");
        String first = console.next();
        queue.add(first);
        previous.put(first,new HashSet<>());
        depths.put(first,0);
        System.out.print("Ending word : ");
        String end = console.next();

        int finalDepth = 1000;
        while (!queue.isEmpty()) { //evaluation function - goes one guess at a time until we run out of moves/hit depth limit
            String word = queue.element();
            int depth = depths.get(word);
            System.out.println(depth);
            if (depth > finalDepth) {
                break;
            }
            if (word.equals(end)) { //if this is an end condition we stop adding more guesses
                finalDepth = depth;
            } else if (depth < finalDepth) {
                Set<String> moves = getAdjacents(word,words,lengths);
                for (String s : moves) {
                    if (!previous.containsKey(s)) { //unexplored words
                        previous.put(s, new HashSet<>());
                        previous.get(s).add(word);
                        queue.add(s); //great thing about this method is only need to look at each word once
                        depths.put(s,depth + 1); //which is the biggest timesave from not using nodes I've found so far
                    } else if (depth + 1 == depths.get(s)) {
                        previous.get(s).add(word); //there might be multiple equally short paths to the same word
                    }
                }
            }
            queue.remove();
        }
        System.out.println("Depth: " + finalDepth); //displays results
        if (!previous.containsKey(end)) {
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