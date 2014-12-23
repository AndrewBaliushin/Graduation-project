package common;

/**
 * Interface for objects that can be controlled by String commands.
 * @author Andrew Baliushin
 */
public interface Commandable {
	
	/**
	 * Send to {@link Commandable} object command.
	 * @param cmd
	 */
	void executeCommand(String cmd);
}
