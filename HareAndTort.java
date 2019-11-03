/**A program to find collisions in MY60SHA hashes
 * @author Eva Morton
 * @version 03-11-2019
 */
  
import java.math.BigInteger; 
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException; 

public class HareAndTort { 
	

    //Uses original SHA-1 code from https://www.geeksforgeeks.org/sha-1-hash-in-java/
    //Extra line of code added to cut the hash down to the required length before returning
    public static String MY60SHA(String input) 
    { 
        try { 
            // getInstance() method is called with algorithm SHA-1 
            MessageDigest md = MessageDigest.getInstance("SHA-1"); 
  
            // digest() method is called 
            // to calculate message digest of the input string 
            // returned as array of byte 
            byte[] messageDigest = md.digest(input.getBytes()); 
  
            // Convert byte array into signum representation 
            BigInteger no = new BigInteger(1, messageDigest); 
  
            // Convert message digest into hex value 
            String hashtext = no.toString(16); 
  
            // Add preceding 0s to make it 32 bit 
            while (hashtext.length() < 32) { 
                hashtext = "0" + hashtext; 
            } 

	    //cut full-length SHA-1 hash to 60 bits
            hashtext = hashtext.substring(0, 15);

            // return the HashText 
            return hashtext; 
        } 
  
        // For specifying wrong message digest algorithms 
        catch (NoSuchAlgorithmException e) { 
            throw new RuntimeException(e); 
        } 
    } 
    
    
    public static void main(String args[]) throws 
                                       NoSuchAlgorithmException 
    {         
	String tortoiseHash = "X";
        String hareHash = "X";
        boolean found = false;
        
	/* Tests for match of H^n("X") with H^2n("X"), stops when a match is found
	 * n is an integer starting at 1 and incrementing each round, i.e.
	 * the first round compares H("X") to HH("X"), the second compares
	 * HH("X") to HHHH("X")
	 */
        while (found == false) {
        	tortoiseHash = MY60SHA(tortoiseHash);
        	hareHash = MY60SHA(MY60SHA(hareHash));
        	if (tortoiseHash.equals(hareHash)) {
        		found = true;
        	}
        }

        //Sets up two new strings to be hashed for comparison
        //One is "X" again, the other is H^n("X") where H^n("X") = H^2n("X")
        boolean found2 = false;
        String hash1 = "X";
        String hash2 = tortoiseHash;
        String storedHash1;
        String storedHash2;

	/* Checks for matches between H^m("X") and H^m(H^n("X"), storing the previous pairs
	 * as it goes. n is based on the first while loop: H^n("X") = H^2n("X").
	 * m is an integer starting as 1 which increments each round, so the first
	 * round compares H("X") with H(H^n("X")), the second compares HH("X") to
	 * HH(H^n("X")) and so on.
	 * When a match is found, it outputs the two stored hashes, which are the 
	 * non-matching strings that produce the same hash.
	 * Also prints the hash resulting from each of these strings, which should match.
	 */
        while (found2 == false) {
        	storedHash1 = hash1;
        	storedHash2 = hash2;
        	hash1 = MY60SHA(hash1);
        	hash2 = MY60SHA(hash2);
        	if (hash1.equals(hash2)) {
        		System.out.println(storedHash1);
        		System.out.println(storedHash2);
        		System.out.println(MY60SHA(storedHash1));
        	    	System.out.println(MY60SHA(storedHash2));
        		found2 = true;
        	}
        }
    }    
} 
