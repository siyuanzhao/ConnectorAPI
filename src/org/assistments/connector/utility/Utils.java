package org.assistments.connector.utility;

public class Utils {
	
	static final String m_prefix = "PS";
    static final String m_version = "A";
	/**
	 * This method converts null to empty string.
	 * @param s - Any string
	 * @return if s is null, it returns empty string "". Otherwise, it returns itself
	 */
	public static String convertNullToEmptyString(String s) { 
		if(s == null) {
			return "";
		} else {
			return s;
		}
	}
	
	/**
	 * Convert encoded problem set string to problem set id
	 * @param psString -- encoded problem set string (PSxxxx)
	 * @return problem set number id (a String)
	 */
	public static String decodeProblemSetString(String psString) {
        if (psString.isEmpty()) {
            return null;
        }
        
        // decode prefix
        if (!psString.substring(0, m_prefix.length()).equalsIgnoreCase(m_prefix)) {
            return null;
        }
        
        // decode version
        if (!psString.substring(m_prefix.length(), m_prefix.length() + m_version.length()).equalsIgnoreCase(m_version)) {
            return null;
        }
        
        // decode problem id
        String code = "abcdefghjkmnpqrstuvwxyz23456789";
        int decodedId = 0;
        String psStringLowerCase = psString.toLowerCase();
        for (int i = m_prefix.length() + m_version.length(); i < psStringLowerCase.length(); i++) {
            char c = psStringLowerCase.charAt(i);
            int oldValue = decodedId;
            decodedId = decodedId * code.length() + code.indexOf(c);
            if (decodedId < oldValue) {
                throw new RuntimeException("Overflow decoded id");
            }
        }
        return new Integer(decodedId).toString();
    }

}
