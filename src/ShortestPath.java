import java.io.*; //Levenshtein by Nate Bergman
import java.util.*;
public class ShortestPath {
    public static void main (String[] args) throws FileNotFoundException {
        Map moveMap = new TreeMap<String,List<String>>(); //builds map of valid moves for each word from file
        Scanner lineScanner = new Scanner(new File("src/Moves"));
        while (lineScanner.hasNext()) {
            Scanner wordScanner = new Scanner(lineScanner.nextLine());
            String key = wordScanner.next();
            List<String> adjacents = new ArrayList<String>();
            wordScanner.useDelimiter(", |]| "); //I can't figure out how to use [ in the delimeter
            adjacents.add(wordScanner.next().substring(1)); //so I had to do this :(
            while (wordScanner.hasNext()) {
                adjacents.add(wordScanner.next());
            }
            moveMap.put(key,adjacents);
        }

        Set<String> previous = new HashSet<>(); //initializes objects used in searching
        Set<String> tempPrevious = new HashSet<>();
        List<List<String>> paths = new ArrayList<>();
        Queue<WordNode> queue = new LinkedList<>();

        Scanner console = new Scanner(System.in); //gets starting and ending words
        System.out.print("Starting word : ");
        queue.add(new WordNode(console.next(),0,new LinkedList<String>()));
        System.out.print("Ending word : ");
        String end = console.next();

        boolean found = false;
        int depth = 0;
        while (!queue.isEmpty() && depth < 20) { //evaluation function - goes one guess at a time until we run out of moves/hit depth limit
            WordNode word = queue.element();
            if (word.getDepth() > depth) { //if we are jumping to a new higher depth, then update previous list
                depth = word.getDepth();
                previous.addAll(tempPrevious);
                tempPrevious.clear(); //need to make it so that it removes everything from the queue

                if (found) { //if we jump up a level after one path has already been completed, remaining paths won't be the shortest
                    queue.clear();
                    break;
                }
            }
            if (word.getWord().equals(end)) { //if this is an end condition we stop adding more guesses
                found = true;
                paths.add(word.getPath());
            } else if (!found){ //otherwise, add all possible/good moves to the queue to be evaluated next
                List<String> moves = (List<String>) moveMap.get(word.getWord());
                for (String s : moves) {
                    if (!previous.contains(s)) { //there is a list of all words we've previously visited at a lower depth - don't want longer paths to same words
                        queue.add(new WordNode(s,depth + 1,word.getPath())); //stores path and depth in the node
                        tempPrevious.add(s); //adds to a list of words we've visited this depth to be added when we jump in depth
                    }
                }
            }
            queue.remove();
        }
        System.out.println("Depth (inclusive): " + depth); //displays results
        if (paths.isEmpty()) {
            System.out.println("No paths!");
        } else {
            //System.out.println("Paths : " + paths);
            System.out.println("Paths(" + paths.size() + "):");
            formatPaths(paths); //i like this version
        }
    }
    public static void formatPaths(List<List<String>> input) {
        System.out.println("digraph something{concentrate=true;");
        for (List<String> l : input) {
            System.out.print(l.get(0));
            for (int i = 1; i < l.size(); i++) {
                System.out.print(" -> ");
                System.out.print(l.get(i));
            }
            System.out.println();
        }
        System.out.println("}");
    }
}