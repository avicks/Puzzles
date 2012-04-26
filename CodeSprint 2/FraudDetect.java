// Alex Vickers
// Codesprint 2
// Groupon Fraud Detection
// Jan 6, 2012

import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class FraudDetect
{	
	public static void main(String[] args)
	{
		Scanner input;				// new scanner object
		int orderCt;				// number of orders	
		int fraudCt;				// counts number of fraudulent orders
		
		input = new Scanner(System.in);
		input.useDelimiter(",|\n");
		orderCt = Integer.parseInt(input.nextLine());

		int[] orderID = new int[orderCt];
		int[] dealID = new int[orderCt];
		String[] email = new String[orderCt];
		String[] address = new String[orderCt];
		String[] city = new String[orderCt];
		String[] state = new String[orderCt];
		String[] zip = new String[orderCt];
		String[] credit = new String[orderCt];
		boolean[] fraud = new boolean[orderCt];
		fraudCt = 0;
						
		for(int y = 0; y < orderCt; y++)
		{
			orderID[y] = Integer.parseInt(input.next());
			dealID[y] = Integer.parseInt(input.next());
			email[y] = input.next().toLowerCase();
			address[y] = input.next().toLowerCase();
			city[y] = input.next().toLowerCase();
			state[y] = input.next().toLowerCase();
			zip[y] = input.next();
			credit[y] = input.next();
			fraud[y] = false;
		}
		
		// Cleans up emails and addresses based on specifications.
		for(int q = 0; q < orderCt; q++)
		{
			if(email[q].contains("+"))		// Deletes any part of the email after a "+" is found up until the @.
			{
				email[q] = email[q].substring(0, (email[q].indexOf("+") )) +
					email[q].substring(email[q].indexOf("@"), email[q].length());
			}
			while(email[q].indexOf(".") < email[q].indexOf("@"))		// Deletes and "." from the email up until the @.
			{
				email[q] = email[q].substring(0, (email[q].indexOf("."))) +
					email[q].substring((email[q].indexOf(".") + 1), email[q].length());
			}
			
			// Cleans up the road abbreviations to match regardless of user input
			if(address[q].contains("street"))
			{
				Pattern pattern = Pattern.compile("street");
				Matcher matcher = pattern.matcher(address[q]);
				address[q] = matcher.replaceAll("st.");
			}
			else if(address[q].contains("road"))
			{
				Pattern pattern = Pattern.compile("road");
				Matcher matcher = pattern.matcher(address[q]);
				address[q] = matcher.replaceAll("rd.");				
			}
			
			// Cleans up state names to match in every case regardless of user input
			if(state[q].contains("illinois"))
			{
				Pattern pattern = Pattern.compile("illinois");
				Matcher matcher = pattern.matcher(state[q]);
				state[q] = matcher.replaceAll("il");
				
			}
			else if(state[q].contains("new york"))
			{
				Pattern pattern = Pattern.compile("new york");
				Matcher matcher = pattern.matcher(state[q]);
				state[q] = matcher.replaceAll("ny");			
			}
			else if(state[q].contains("california"))
			{
				Pattern pattern = Pattern.compile("california");
				Matcher matcher = pattern.matcher(state[q]);
				state[q] = matcher.replaceAll("ca");				
			}
		}

		// Checks for fraud
		for(int i = 0; i < orderCt; i++)
		{
			for(int j = 0; j < orderCt; j++)
			{
				if( orderID[i] != orderID[j] && dealID[i] == dealID[j] && fraud[i] != true)
				{
					if(email[i].equals(email[j]) && credit[i] != credit[j])
					{
						fraud[i] = true;
						fraud[j] = true;
					}
					if(address[i].equals(address[j]) && city[i].equals(city[j]) && state[i].equals(state[j]) && zip[i].equals(zip[j]) && (credit[i] != credit[j]))
					{
						fraud[i] = true;
						fraud[j] = true;
					}
				}
			}
		}
		
		for(int k = 0; k < orderCt; k++)	// determines number of fraud orders
		{
			if(fraud[k] == true)
			{
				fraudCt++;
			}
		}

		for(int m = 0; m < orderCt; m++)	// outputs the fraud list
		{
			if((fraud[m] == true) && (fraudCt > 1))
			{
				System.out.print(orderID[m] + ",");
				fraudCt--;
			}
			else if(fraud[m] == true)
			{
					System.out.print(orderID[m]);
			}
		}
	}
}