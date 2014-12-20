package gui;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Names of the methods in {@link JClient} which contain actions for buttons and components.
 * Values must be consistent with methods of JClient or error will be shown in console.
 * @author Andrew Baliushin
 *
 */
enum JClientActionMethodNames {
	FIND_BUTTON_ACTION("findButtonAction"), 
	ADD_BUTON_ACTION("addButtonAction"), 
	EDIT_BUTTON_ACTION("editButtonAction"), 
	NEXT_BUTTON_ACTION("nextButtonAction"), 
	PREV_BUTTON_ACTION("prevButtonAction"),
	EXIT_BUTTON_ACTION("exitButtonAction"),
	CONNECT_BUTTON_ACTION("connectButtonAction"),
	CHOOSE_FILE_BUTTON_ACTION("chooseFileAction");
	
	private String methodName;

	private JClientActionMethodNames(String methodName) {
		this.methodName = methodName;
		testMethodExistanceAndAccessibility(methodName);
	}
	
	public String getName() {
		return methodName;
	}
	
	private void testMethodExistanceAndAccessibility(String methodName) {
		try {
			Method method = JClient.class.getDeclaredMethod(methodName);
			int mod = method.getModifiers();
			if(Modifier.isPrivate(mod)) {	
				throw new IllegalAccessException();
			}			
		} catch (NoSuchMethodException ex) {
			System.err.println("Exception in " + this.getClass().getName());
			System.err.println("There is no " + methodName + " method in JClient");
			System.err.println(ex.getMessage());
		} catch (IllegalAccessException e) {
			System.err.println("Exception in " + this.getClass().getName());
			System.err.println("Method " + methodName + " can't be private");
		}	
	}
}
