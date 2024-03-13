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

        Map<String,Set<String>> previous1 = new HashMap<>(); //shows what words can lead to this one in a shortest path
        Map<String,Set<String>> previous2 = new HashMap<>();
        Set<String> explored1 = new HashSet<>();
        Set<String> explored2 = new HashSet<>();
        Queue<String> queue1 = new LinkedList<>();
        Queue<String> queue2 = new LinkedList<>();

        Scanner console = new Scanner(System.in); //gets starting and ending words

        //signpost of null

        System.out.print("Starting word : ");
        String first = console.next();
        queue1.add(first);
        queue1.add("");
        previous1.put(first,new HashSet<>());

        System.out.print("Ending word : ");
        String end = console.next();
        queue2.add(end);
        queue2.add("");
        previous2.put(end,new HashSet<>());

        while (!queue1.isEmpty() || !queue2.isEmpty()) {
            if (queue1.size() > queue2.size()) { //search from queue2
                Set<String> frontier = new HashSet<>();
                while(true) {
                    String word = queue2.element();
                    if (word.equals("")) {
                        queue2.remove();
                        queue2.add("");
                        explored2.addAll(frontier);
                        break;
                    }
                    Set<String> moves = moveMap.get(word);
                    for (String s : moves) {
                        if (explored1.contains(s)) {
                            //we've found a bridge
                        }
                        if (!explored2.contains(s)) {
                            if (!previous2.containsKey(s)) {
                                previous2.put(s, new HashSet<>());
                                queue2.add(s);
                                frontier.add(s);
                            }
                            previous2.get(s).add(word);
                        }
                    }
                }
            } else { //search from queue1

            }
        }

        //int finalDepth = 1000;
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