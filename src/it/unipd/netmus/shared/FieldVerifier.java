package it.unipd.netmus.shared;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * FieldVerifier validates that the name the user enters is valid.
 * </p>
 * <p>
 * This class is in the <code>shared</code> package because we use it in both
 * the client code and on the server. On the client, we verify that the name is
 * valid before sending an RPC request so the user doesn't have to wait for a
 * network round trip to get feedback. On the server, we verify that the name is
 * correct to ensure that the input is correct regardless of where the RPC
 * originates.
 * </p>
 * <p>
 * When creating a class that is used on both the client and the server, be sure
 * that all code is translatable and does not use native JavaScript. Code that
 * is note translatable (such as code that interacts with a database or the file
 * system) cannot be compiled into client side JavaScript. Code that uses native
 * JavaScript (such as Widgets) cannot be run on the server.
 * </p>
 */
public class FieldVerifier {

	/**
	 * Verifies that the specified name is valid for our service.
	 * 
	 * @param name the name to validate
	 * @return true if valid, false if invalid
	 */
	public static boolean isValidName(String name) {
		if (name == null) {
			return false;
		}
		return name.length() > 3;
	}
	
	/**
	 * Verifies that the specified password is valid for our service.
	 * 
	 * @param name the password to validate
	 * @return true if valid, false if invalid
	 */
	public static boolean isValidPassword(String password) {
		if (password == null) {
			return false;
		}
		return password.length() > 4;
	}
	
	/**
	 * Verifies that the specified e-mail is valid for e-mail common addresses.
	 * 
	 * @param name the e-mail to validate
	 * @return true if valid, false if invalid
	 */
	public static boolean isValidEmail(String email) {
		Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
		
		Matcher matcher = pattern.matcher(email);
		
		return matcher.matches();
	}
}
