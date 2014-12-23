package common;

import java.util.Scanner;

/**
 * Class for working with keyboard in separate thread and calling commands
 * of {@link Commandable}
 * @author andrew
 *
 */
public class KeyboardCommandCaller {

	public static Runnable getListner(final Commandable objToCommand) {
    	return new Runnable() {
            @Override
            public void run() {
                @SuppressWarnings("resource")
				Scanner scan = new Scanner(System.in);
                String input = "";
                while (true) {                 
                    input = scan.nextLine();
                    objToCommand.executeCommand(input);                    
                }                
            }
    	};
    }

}
