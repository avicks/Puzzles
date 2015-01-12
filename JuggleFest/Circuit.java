/*
    Alex Vickers
    Yodle Juggle Fest Puzzle
    http://www.yodlecareers.com/puzzles/jugglefest.html
 */

import java.util.ArrayList;

public class Circuit
{
    private final String name;
    private final int handEyeCoordination;
    private final int endurance;
    private final int pizzazz;
    private ArrayList<Juggler> jugglerList;
    /* create a Circuit object and set name, handEyeCoordination, endurance, pizzazz values.
            Also initialize an empty list that will hold the jugglers for the circuit
     */
    public Circuit(String name, int handEyeCoordination, int endurance, int pizzazz)
    {
        this.name = name;
        this.handEyeCoordination = handEyeCoordination;
        this.endurance = endurance;
        this.pizzazz = pizzazz;
        this.jugglerList = new ArrayList<Juggler>();
    }

    /*
        Add Juggler to the jugglerList at specified index.
     */
    public void addJugglerToList(Juggler juggler, int index)
    {
        jugglerList.add(index,juggler);
    }

    /*
        Remove Juggler from jugglerList.
        As the only time we will be removing one is when the length is (Circuits / Jugglers) + 1.
        Index will always be the last Juggler in the list.
    */
    public Juggler removeJugglerFromList()
    {
        return jugglerList.remove(jugglerList.size()-1);
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

    /*
        Returns a Juggler from the list of Jugglers matched to a Circuit at a given index on the list of Jugglers
     */
    public Juggler getJugglerAtIndex(int index)
    {
        return jugglerList.get(index);
    }

    public int getJugglerListLength()
    {
        return jugglerList.size();
    }

    /*
        Find the sum of the digits in Juggler names of a Circuit's list of Jugglers
     */
    public int getJugglerListNumbersSum()
    {
        int sum = 0;

        for (Juggler currentJuggler : jugglerList) {
            sum += Integer.parseInt(currentJuggler.getName().replaceAll("[^\\d]", ""));
        }

        return sum;
    }

    /*
        Return string of of jugglerList Juggler's and their preferred Circuits and match scores.
     */
    public String jugglerListToString()
    {
        String returnString = new String();

        if(!jugglerList.isEmpty()) {

            for (Juggler currentJuggler : jugglerList) {
                returnString += currentJuggler.toString() + ", ";
            }

            return returnString.substring(0,returnString.length()-2);
        } else {
            return "";
        }
    }

    /* return the name of the Circuit object followed by it's Juggler list, ordered by match Score.
            each Juggler in the list is accompanied with it's preferred Circuits with match scores, ordered by circuit preference.
    */
    public String toString()
    {
        return name + " " + jugglerListToString();
    }

}