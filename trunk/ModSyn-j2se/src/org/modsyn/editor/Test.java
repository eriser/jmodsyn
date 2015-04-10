package org.modsyn.editor;

import java.io.File;

import org.modsyn.Context;
import org.modsyn.ContextFactory;
import org.modsyn.editor.io.FileSys;
import org.modsyn.editor.io.XmlImport;

public class Test {

	public static void main(String[] args) throws Exception {
		Context c = ContextFactory.create();
		DspPatchCombinationModel pm = new DspPatchCombinationModel(c);
		new XmlImport(new File(FileSys.dirPatches, "dirty-rhodes.dsp-patch"), c, pm);
	}
}
