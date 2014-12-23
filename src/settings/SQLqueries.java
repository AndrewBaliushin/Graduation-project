package settings;

public class SQLqueries {
	public static final String FIND_QRY = "SELECT definition, term, tblDefinitions.id, tblTerms.id" +
			", definition FROM  tblDefinitions" +
			" INNER JOIN tblTerms" +
			" ON term_id = tblTerms.id" +
			" WHERE term = ?";
	
	public static final String MAX_TERM_ID = "SELECT MAX(id) FROM tblTerms";
	public static final String MAX_DEF_ID = "SELECT MAX(id) FROM tblDefinitions";
	
	public static final String INSERT_NEW_TERM = "INSERT INTO tblTerms VALUES(?, ?)";
	public static final String INSERT_NEW_DEF = "INSERT INTO tblDefinitions VALUES(?, ?, ?)";
	
	public static final String FIND_TERM = "SELECT term" +
			" FROM tblTerms" +
			" WHERE term = ?";
	
	
	
	
}
