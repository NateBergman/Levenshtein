import java.io.*; //Levenshtein by Nate Bergman
import java.util.*;
public class NoNodeShortestPath {
    public static void main (String[] args) throws FileNotFoundException {
        Map moveMap = new TreeMap<String,Set<String>>(); //builds map of valid moves for each word from file
        Scanner lineScanner = new Scanner(new File("src/Moves"));
        while (lineScanner.hasNext()) {
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
        Map<String,Integer> depths = new TreeMap<>();
        Queue<String> queue = new LinkedList<>();

        Scanner console = new Scanner(System.in); //gets starting and ending words
        System.out.print("Starting word : ");
        String first = console.next();
        queue.add(first);
        depths.put(first,0);
        previous.put(first,new HashSet<>());
        System.out.print("Ending word : ");
        String end = console.next();

        int finalDepth = 20;
        while (!queue.isEmpty()) { //evaluation function - goes one guess at a time until we run out of moves/hit depth limit
            String word = queue.element();
            int depth = depths.get(word);
            if (depth > finalDepth) {
                queue.clear();
                break;
            }
            if (word.equals(end)) { //if this is an end condition we stop adding more guesses
                finalDepth = depth;
            } else if (depth < finalDepth){ //otherwise, add all possible/good moves to the queue to be evaluated next
                Set<String> moves = (Set<String>) moveMap.get(word); //idk why this cast is necessary but otherwise it gets mad
                moves.removeAll(previous.get(word));
                for (String s : moves) {
                    queue.add(s); //stores path and depth in the node
                    depths.put(s,depth + 1);
                    if (!previous.containsKey(s)) {
                        previous.put(s,new HashSet<>());
                    }
                    previous.get(s).add(word);
                }
            }
            queue.remove();
        }
        System.out.println("Depth: " + finalDepth); //displays results
        if (!previous.containsKey(end)) {
            System.out.println("No paths!");
        } else {
            System.out.println("Paths(" + /*paths.size() +*/ "):\ndigraph something{concentrate=true;");
            printPaths("",end,first,previous);
            System.out.print('}');
        }
    }
    public static void printPaths (String currentString, String input, String start, Map<String,Set<String>> previous) {
        if (input.equals(start)) {
            System.out.println(input + currentString);
        } else {
            String newCurrent = " -> " + input + currentString;
            for (String s : previous.get(input)) {
                printPaths(newCurrent,s,start,previous);
            }
        }
    }
}