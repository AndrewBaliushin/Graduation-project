package jhelp;

import java.util.Scanner;

public class KeyboardCommand {

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
