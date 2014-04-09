package org.modsyn.modules.ext;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockComponent;
import org.modsyn.editor.DspPatchModel;
import org.modsyn.editor.blocks.FromAsioModel;
import org.modsyn.editor.blocks.FromJavaSoundModel;

public class AudioInSupport {

	public static synchronized String normalizeClassName(String name) {
		if (FromAsioModel.class.getName().equals(name) || FromJavaSoundModel.class.getName().equals(name) || AudioInSupport.class.getName().equals(name)) {
			return AudioInSupport.class.getName();
		}

		return null;
	}

	public static synchronized DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
		if (AsioSupport.INSTANCE.isSupported()) {
			return new DspBlockComponent(c, new FromAsioModel(c, AsioSupport.INSTANCE), pm);
		} else {
			return new DspBlockComponent(c, new FromJavaSoundModel(c), pm);
		}
	}
}
