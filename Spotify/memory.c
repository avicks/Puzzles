/* Spotify Three-State Memory
*  Author: Alex Vickers
*  Email: a.vickers2@gmail.com
*/

#include <stdio.h>
#include <math.h>

int main(int argc, char *argv[]) {
	int c;
	int digit, decimal, count = 0;
	int binary[9999];
	
	// store binary input bit by bit
	while (isdigit((c = getc(stdin))))
	{
		binary[digit] = c;
		digit++;
	}
	
	// calculate the decimal representation of binary input
	for(int i = 0, i <= digit, i++) {
		if(binary[i] == 1) {
			decimal = (decimal + pow(2, i));
		}
	}
	
	// systemically converts a decimal to binary while checking for the third memory state
	while (decimal != 0) {
		if (decimal %  2 != 0) {
			decimal--;	
		}
		if (decimal % 4 == 0) {
			count++;
		}
		decimal = (decimal / 2);	
	}
	
	putc(count, stdout);
	
	return 0;
}