/* Spotify Troll Hunt
*  Author: Alex Vickers
*  Email: a.vickers2@gmail.com
*/

import java.util.Scanner;

public class troll {
	public static void main(String [] args) {
		Scanner input;		// new scanner object for input
		int b, k, g;		// b = bridges, k = knights, g = knights per group
		int groups;			// number of groups of knights
		int days;			// assured date of troll's capture
		
		input = new Scanner(System.in);
		b = input.nextInt();
		k = input.nextInt();
		g = input.nextInt();
		
		groups = (k / g); // doesn't matter if there is a remainder, so int division works
		
		// presumably the original bridge isn't checked, so we use b-1
		if(((b-1) % groups) == 0) {
		days = ((b - 1) / groups);
		}
		else { // if there is a remainder of bridges, add one extra day
			days = (((b-1) / groups) + 1);
		}
		System.out.print(days);
	}
}