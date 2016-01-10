package org.modsyn;

public interface ContextListener {

	void added(DspObject o);

	void removed(DspObject o);
}
