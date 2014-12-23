package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

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
			//TODO
			System.err.println("Error occured in reflective operation while invoking JClient method");
			System.err.println(ex.getMessage());
			ex.printStackTrace();
		}	
	}
}