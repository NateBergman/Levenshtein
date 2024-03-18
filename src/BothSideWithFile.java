import java.io.*; //Levenshtein by Nate Bergman
import java.util.*;
public class BothSideWithFile {
    public static void main (String[] args) throws FileNotFoundException {
        Map<String,Set<String>> moveMap = new TreeMap<>(); //builds map of valid moves for each word from file
        Scanner lineScanner = new Scanner(new File("src/Moves"));
        while (lineScanner.hasNext()) { //slowest part by far, should probably clean up
            Scanner wordScanner = new Scanner(lineScanner.nextLine());
            String key = wordScanner.next();
            Set<String> adjacents = new HashSet<String>();
            wordScanner.useDelimiter(", |]| "); //I can't figure out how to use [ in the delimeter
            adjacents.add(wordScanner.next().substring(1)); //so I had to do this :(
            while (wordScanner.hasNext()) {
                adjacents.add(wordScanner.next());
            }
            moveMap.put(key,adjacents);
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
        while (!queue1.isEmpty() && !queue2.isEmpty()) {
            if (queue1.size() > queue2.size()) { //search from queue2
                while (true) {
                    String word = queue2.element();
                    int depth = depths.get(word);
                    if (depth != prevDepth2) { //only goes one line at a time
                        System.out.println(prevDepth2);
                        prevDepth2--;
                        break;
                    }
                    Set<String> moves = moveMap.get(word);
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
                        }
                    }
                    queue2.remove();
                }
            } else { //search from queue1
                while (true) {
                    String word = queue1.element();
                    int depth = depths.get(word);
                    if (depth != prevDepth1) { //only goes one line at a time
                        System.out.println(prevDepth1);
                        prevDepth1++;
                        break;
                    }
                    Set<String> moves = moveMap.get(word);
                    for (String s : moves) {
                        if (!depths.containsKey(s)) { //unexplored words
                            previous.put(s, new HashSet<>());
                            previous.get(s).add(word);
                            queue1.add(s); //great thing about this method is only need to look at each word once
                            depths.put(s,depth + 1); //which is the biggest timesave from not using nodes I've found so far
                        } else if (depth + 1 == depths.get(s)) {
                            previous.get(s).add(word); //there might be multiple equally short paths to the same word
                        } else if (depths.get(s) <= 0) {
                            depths.put(s,0);
                            previous.get(s).add(word);
                            queue2.clear();
                        }
                    }
                    queue1.remove();
                }
            }
        }
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
}