/*
    Alex Vickers
    Yodle Juggle Fest Puzzle
    http://www.yodlecareers.com/puzzles/jugglefest.html
 */

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/*
    Main class in the program.
    First read from the input file, store Jugglers and Circuits.
    Then calculate match scores between Jugglers and their preferred Circuits.
    Next place Jugglers on Circuits.
    Finally output the Circuits and their Jugglers.
 */
public class JuggleFest
{
    private static final String FILE_NAME = "jugglefest.txt";
    private static final String OUTPUT_FILE = "out.txt";
    private static final String CIRCUIT_EMAIL_NUMBER = "C1970";

    private static LinkedHashMap<String,Circuit> circuitMap = new LinkedHashMap<String,Circuit>();
    private static Queue<Juggler> jugglerQueue = new LinkedList<Juggler>();
    private static Queue<Juggler> rejectedJugglerQueue = new LinkedList<Juggler>();

    private static int circuitCount;
    private static int jugglerCount;
    private static int jugglersPerCircuit;
    private static int maxRejectionCount;

    public static void main(String[] args) {

        //read input file and store Jugglers and Circuits
        try {
            readInput();
        } catch (IOException ioe) {
            System.out.println("Some kind of input problem occurred.");
            ioe.printStackTrace();
        }

        // calculate match scores
        calculateMatchScores();

        // place Jugglers onto Circuits
        placeJugglers();

        // output Circuit's to the specified output file.
        try {
            outputCircuits();
        } catch (IOException ioe) {
            System.out.println("Some kind of output problem occurred.");
            ioe.printStackTrace();
        }

        // Calculate sum of Jugglers on Circuit given by Yodle for email
        getEmailSum(CIRCUIT_EMAIL_NUMBER);

    }

    /* handle input file, parsing and storing details for Jugglers and Circuits.
     */
    private static void readInput() throws IOException {
        File inputFile = new File(FILE_NAME);
        FileInputStream fileInputStream = new FileInputStream(inputFile);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

        String currentLine = null;
        while((currentLine = bufferedReader.readLine()) != null) {
            if(!currentLine.isEmpty()) {
                if(currentLine.charAt(0) == 'C') {
                    String[] splitStrings = currentLine.split(" ");
                    String tempName = splitStrings[1];

                    // strip all characters from integers for hand eye coordination, endurance, and pizzazz values
                    //  (assuming values are non-zero, positive integers)
                    int tempHandEyeCoordination = Integer.parseInt(splitStrings[2].replaceAll("[^\\d]", ""));
                    int tempEndurance = Integer.parseInt(splitStrings[3].replaceAll("[^\\d]", ""));
                    int tempPizzazz = Integer.parseInt(splitStrings[4].replaceAll("[^\\d]", ""));

                    Circuit tempCircuit = new Circuit(tempName, tempHandEyeCoordination, tempEndurance, tempPizzazz);
                    circuitMap.put(tempName,tempCircuit);
                    circuitCount++;

                } else if (currentLine.charAt(0) == 'J') {

                    String[] splitStrings = currentLine.split(" ");
                    String tempName = splitStrings[1];

                    // strip all characters from integers for hand eye coordination, endurance, and pizzazz values
                    //  (assuming values are non-zero, positive integers)
                    int tempHandEyeCoordination = Integer.parseInt(splitStrings[2].replaceAll("[^\\d]", "" ));
                    int tempEndurance = Integer.parseInt(splitStrings[3].replaceAll("[^\\d]", "" ));
                    int tempPizzazz = Integer.parseInt(splitStrings[4].replaceAll("[^\\d]", "" ));

                    String[] splitCircuitPreferences = splitStrings[5].split(",");

                    Juggler tempJuggler = new Juggler(tempName, tempHandEyeCoordination, tempEndurance, tempPizzazz, splitCircuitPreferences);
                    jugglerQueue.add(tempJuggler);
                    jugglerCount++;
                }
            }
        }

        // Evenly displace Jugglers throughout Circuits
        jugglersPerCircuit = (jugglerCount / circuitCount);

        bufferedReader.close();
    }

    /*
        Iterate through the list of Jugglers and calculate each match score for their preferred circuits
        The match score is found by calculating the dot product of the hand eye coordination, pizzazz,
        and endurance values for a juggler and circuit.
     */
    private static void calculateMatchScores()
    {
        int matchScore;
        HashMap<String, Integer> jugglerCircuitsMap;
        Circuit currentCircuit;

        for(Juggler currentJuggler : jugglerQueue) {
            jugglerCircuitsMap = currentJuggler.getCircuitsMap();

            for (String circuitName : jugglerCircuitsMap.keySet()) {
                currentCircuit = circuitMap.get(circuitName);

                // calculate the dot product of the hand eye coordination, endurance, and pizzazz values for juggler and circuit
                matchScore = ((currentJuggler.getHandEyeCoordination() * currentCircuit.getHandEyeCoordination()) +
                                        (currentJuggler.getEndurance() * currentCircuit.getEndurance()) +
                                        (currentJuggler.getPizzazz() * currentCircuit.getPizzazz()));

                currentJuggler.setMatchScore(circuitName,matchScore);
            }
        }


    }

    /* Here we attempt to place a Juggler in a Circuit in order of the Juggler's preference.
        A few outcomes are possible:
            - A Circuit is found for a Juggler.  We place the Juggler in this Circuit.
                - If the Circuit is full, the Juggler with the lowest match score is kicked out.
                    We then search for a new home for the kicked out Juggler by calling this function recursively.
            - A Circuit is not found for the Juggler.
                - We add the Juggler to the rejected Jugglers to be handled once all Jugglers get a chance to find a preferred Circuit.
    */
   private static void findCircuitForJuggler(Juggler currentJuggler)
   {
        boolean jugglerWasPlaced;
        ArrayList<String> availablePreferredCircuits = currentJuggler.getPreferredCircuitsList();

        Circuit currentCircuit;
        int currentMatchScore;
        int currentCircuitIndex;
        jugglerWasPlaced = false;

        while(!jugglerWasPlaced && availablePreferredCircuits.size() > 0) {

            // always get the most preferred available circuit, sitting at index 0
            String circuitName = availablePreferredCircuits.get(0);

            // Mark this preferred Circuit as checked by removing it from the unchecked Circuits for this Juggler
            currentJuggler.removeCircuitFromAvailableList(circuitName);

            currentCircuit = circuitMap.get(circuitName);
            currentMatchScore = currentJuggler.getMatchScore(circuitName);

            // if the list is empty, place the juggler in the list.
            if(currentCircuit.getJugglerListLength() == 0) {
                currentCircuit.addJugglerToList(currentJuggler,0);
                jugglerWasPlaced = true;
            } else {

                // account for 0-index
                currentCircuitIndex = (currentCircuit.getJugglerListLength() - 1);

                /* Scan the circuitList of Jugglers from the bottom up to find a place to stick our Juggler.
                        - If we find a place to stick the juggler, do so.
                            After, check if the list of Jugglers exceeds our maximum allotment.
                                If so, remove the last Juggler and place it back in the queue of Jugglers to be added to Circuits.
                        - If we find no spot for the Juggler on this circuitList, move on to the next preferred Circuit of our Juggler.
                */
                while(currentCircuitIndex >= 0 && !jugglerWasPlaced) {

                    /* if the currentMatchScore is greater than the one we compare it to, keep moving up the list.
                              otherwise, we have found where to place our Juggler on the list.
                    */
                    if(currentMatchScore > currentCircuit.getJugglerAtIndex(currentCircuitIndex).getMatchScore(circuitName)) {
                        /* If we are at the head of the list, place the Juggler and end our search.
                                Otherwise, keep moving up the list.
                        */
                        if(currentCircuitIndex == 0) {
                            currentCircuit.addJugglerToList(currentJuggler,0);
                            jugglerWasPlaced = true;

                            // If we fill the Circuit's Juggler list over capacity, remove Juggler with smallest match score and find him new Cirucit.
                            if(currentCircuit.getJugglerListLength() > jugglersPerCircuit) {
                                currentJuggler = currentCircuit.removeJugglerFromList();
                                findCircuitForJuggler(currentJuggler);
                            }
                        } else {
                            currentCircuitIndex--;
                        }

                    } else {
                        /* If the current index indicates the list is already full, do not place the Juggler.
                                We set currentCircuitIndex to -1 to break the while loop scanning the list for where to place the Juggler.
                            Otherwise, add the Juggler to the jugglerList.  Then check to make sure our jugglerList does not exceed max length.
                                If it does, find a new home for the Juggler with the smallest match score.
                        */
                        if(currentCircuitIndex+1 >= jugglersPerCircuit) {
                            currentCircuitIndex = -1;
                        } else {
                            currentCircuit.addJugglerToList(currentJuggler,currentCircuitIndex+1);
                            jugglerWasPlaced = true;

                            if(currentCircuit.getJugglerListLength() > jugglersPerCircuit) {
                                currentJuggler = currentCircuit.removeJugglerFromList();
                                findCircuitForJuggler(currentJuggler);
                            }
                        }
                    }
                }
            }
        }

        // If a Juggler does not find a spot in any of it's preferred Circuits, put it with the rejected Jugglers.
        if(jugglerWasPlaced == false && availablePreferredCircuits.size() == 0) {
                rejectedJugglerQueue.add(currentJuggler);
        }
   }

    /* Place all Jugglers into their respective Circuits.  Jugglers are placed in the following way:
            - All Jugglers are distributed evenly among Circuits.  (Here we assume Jugglers / Circuits is an integer)

            - Jugglers are placed into Circuits first by their preference and second by their match score.
                - We first try to place a Juggler into it's most preferred Circuit.
                    - Circuits are ordered by match score.  So if a Juggler has a match score with a Circuit we are trying to place it in
                        that places it in the top X spots of the Circuit (where X is Jugglers / Circuits) then we place the Juggler.

                        - If a Juggler does not place in the top X spots, we attempt to place it in it's next most preferred Circuit.

                        - If a Juggler was placed in a Circuit but is no longer in the top X spots after another Juggler is added,
                            we attempt to place it in it's next most preferred Circuit.

                        - If a Juggler is not placed in any of it's preferred Circuits, it is added to the rejected Juggler queue.

            - Once all Jugglers get a chance to be placed in their most preferred available Circuit, we fill the remaining
                spots in Circuit Juggler lists with those Jugglers who were rejected from all of their preferred choices.
    */
    private static void placeJugglers()
    {
        Juggler currentJuggler;
        Circuit currentCircuit;

        // First attempt to place Jugglers in preferred Circuits
        while(!jugglerQueue.isEmpty()) {
            currentJuggler = jugglerQueue.remove();
            findCircuitForJuggler(currentJuggler);
        }

        // After all Jugglers get a chance to be placed in all preferred Circuits, place the rejects in non-full Circuits.
        while(!rejectedJugglerQueue.isEmpty()) {
            for(String circuitName: circuitMap.keySet()) {
                currentCircuit = circuitMap.get(circuitName);

                while(currentCircuit.getJugglerListLength() < jugglersPerCircuit) {
                    currentJuggler = rejectedJugglerQueue.remove();
                    currentCircuit.addJugglerToList(currentJuggler,currentCircuit.getJugglerListLength());
                }
            }
        }
    }

    /*
        Output each Circuit and it's list of Jugglers, ordered by match score, to a specified file
     */
    private static void outputCircuits() throws IOException
    {
        File outputFile = new File(OUTPUT_FILE);
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

        for (Map.Entry<String, Circuit> entry : circuitMap.entrySet()) {
            Circuit currentCircuit = entry.getValue();
            bufferedWriter.write(currentCircuit.toString());
            bufferedWriter.newLine();
        }

        bufferedWriter.close();
    }

    /*
        Sum the numbers of the juggler names for a given Circuit.
        (Used to submit the code to Yodle.)
     */
    private static void getEmailSum(String circuitName)
    {
        Circuit currentCircuit = circuitMap.get(circuitName);
        System.out.println("Sum of Circuit " + circuitName + "s Juggler name digits: " + currentCircuit.getJugglerListNumbersSum());
    }

}