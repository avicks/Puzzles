/*
    Alex Vickers
    Yodle Juggle Fest Puzzle
    http://www.yodlecareers.com/puzzles/jugglefest.html
 */

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;

public class Juggler
{
    private final String name;
    private final int handEyeCoordination;
    private final int endurance;
    private final int pizzazz;

    private LinkedHashMap<String, Integer> circuitsMap;

    private ArrayList<String> remainingCircuits;  // a list of the preferred Circuits still available for a Juggler to be placed on

    /* create a Juggler object and set name, handEyeCoordination, endurance, pizzazz,
            and a list of perferred circuits of the juggler in the order the juggler specified.
     */
    public Juggler(String name, int handEyeCoordination, int endurance, int pizzazz, String[] circuitPreferences)
    {
        this.name = name;
        this.handEyeCoordination = handEyeCoordination;
        this.endurance = endurance;
        this.pizzazz = pizzazz;

        this.circuitsMap = new LinkedHashMap<String, Integer>();
        this.remainingCircuits = new ArrayList<String>();

        // initally, all match scores are set to 0 for Juggler-Circuit relations.
        // also add the circuits, in preference order, to a list we will use for Juggler placement
        for(int i = 0; i < circuitPreferences.length; i++) {
            circuitsMap.put(circuitPreferences[i],0);
            remainingCircuits.add(circuitPreferences[i]);
        }
    }

    /*
        Sets a Juggler and Circuit's match score when given the Circuit name and score value.
     */
    public void setMatchScore(String circuitName, int matchScore)
    {
        circuitsMap.put(circuitName,matchScore);
    }

    /*
        Remove a Circuit from the Juggler's available preferred circuits
     */
    public void removeCircuitFromAvailableList(String circuitName)
    {
        int index = remainingCircuits.indexOf(circuitName);
        remainingCircuits.remove(index);
    }

    public String getName()
    {
        return name;
    }

    public int getHandEyeCoordination()
    {
        return handEyeCoordination;
    }

    public int getEndurance()
    {
        return endurance;
    }

    public int getPizzazz()
    {
        return pizzazz;
    }

    public ArrayList<String> getPreferredCircuitsList()
    {
        return remainingCircuits;
    }

    public int getPreferredCircuitsListSize()
    {
        return remainingCircuits.size();
    }

    /*
        Return a map of the preferred Circuits for a Juggler.
     */
    public HashMap<String,Integer> getCircuitsMap()
    {
        return circuitsMap;
    }

    /*
        Given a Circuit name, returns the match score between the Juggler and Circuit
     */
    public int getMatchScore(String circuitName)
    {
        return circuitsMap.get(circuitName);
    }

    /* return the name of the Juggler object followed by it's circuit list, ordered by juggler preference.
            each circuit in the list is accompanied with the match score for that circuit.
    */
    public String toString()
    {
        return name + " " + circuitsMapToString();
    }

    /* return a string ofthe list of preferred circuits for a juggler along with the match score for
            the juggler and circuit. list is returned in order of juggler preference
    */
    public String circuitsMapToString()
    {
        String returnString = new String();

        for (Map.Entry<String, Integer> entry : circuitsMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            returnString += key + ":" + value + " ";
        }

        // trim last space added by iterator, return the list.
        return returnString.substring(0,returnString.length()-1);
    }
}