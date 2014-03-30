package org.modsyn.util;

import org.modsyn.Context;

public class ContextUpdateThread extends Thread {
	
	private final Context context;

	public ContextUpdateThread(Context context) {
		super();
		this.context = context;
	}
	
	@Override
	public void run() {
		while (!isInterrupted()) {
			context.update();
		}
	}
}
