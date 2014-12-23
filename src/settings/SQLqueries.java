package settings;

public class SQLqueries {
	public static final String FIND_QRY = "SELECT definition, term, tblDefinitions.id, tblTerms.id" +
			", definition FROM  tblDefinitions" +
			" INNER JOIN tblTerms" +
			" ON term_id = tblTerms.id" +
			" WHERE term = ?";
}
