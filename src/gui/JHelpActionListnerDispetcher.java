package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

import static localization.ClientLabelAndMsgs.*;

/**
 * Assigns and invoke methods of {@link JClient}.
 * Methods names must be defined in {@link JClientActionMethodNames}
 * @author Andrew Baliushin
 */
public class JHelpActionListnerDispetcher {
	
	private JClient jClient;
	
	public JHelpActionListnerDispetcher(JClient jClient) {
		this.jClient = jClient;
	}

	public ActionListener createListnerWithAttachment(final JClientActionMethodNames methodName) {
		ActionListener l = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				invokeMethodOfJClient(methodName);				
			}
		};
		
		return l;
	}
	
	private void invokeMethodOfJClient(JClientActionMethodNames methodName) {
		try {
			Method method = jClient.getClass().getDeclaredMethod(methodName.getName());			
			method.invoke(jClient);
		} catch (ReflectiveOperationException ex) {
			System.err.println(REFLECTIVE_INVOKE_ERR);
			System.err.println(ex.getMessage());
			ex.printStackTrace();
		}	
	}
}