import java.io.*; //Levenshtein by Nate Bergman
import java.util.*;
public class NoNodeShortestPath {
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
            if (depth > finalDepth) {
                break;
            }
            if (word.equals(end)) { //if this is an end condition we stop adding more guesses
                finalDepth = depth;
            } else if (depth < finalDepth) {
                Set<String> moves = moveMap.get(word);
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
}