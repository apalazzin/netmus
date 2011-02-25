package it.unipd.netmus.shared;


public class FieldVerifier {
    
    public static boolean isValidNickName(String nickname) {
        
        if (nickname == null)
            return false;
        else return nickname.length() > 4;
    
        
    }

	public static boolean isValidPassword(String password) {
	    
		if (password == null)
			return false;
		else return password.length() > 4;
	}
	

	public static boolean isValidEmail(String email) {
	    
		return email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
		
	}
}
