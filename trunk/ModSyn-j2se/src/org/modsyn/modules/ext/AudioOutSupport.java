package org.modsyn.modules.ext;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockComponent;
import org.modsyn.editor.DspPatchModel;
import org.modsyn.editor.blocks.ToAsioModel;
import org.modsyn.editor.blocks.ToJavaSoundModel;

public class AudioOutSupport {

	public static synchronized String normalizeClassName(String name) {
		if (ToAsioModel.class.getName().equals(name) || ToJavaSoundModel.class.getName().equals(name) || AudioOutSupport.class.getName().equals(name)) {
			return AudioOutSupport.class.getName();
		}

		return null;
	}

	public static synchronized DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
		if (AsioSupport.INSTANCE.isSupported()) {
			return new DspBlockComponent(c, new ToAsioModel(c, AsioSupport.INSTANCE), pm);
		} else {
			return new DspBlockComponent(c, new ToJavaSoundModel(new ToJavaSound(c, 2, 1024)), pm);
		}
	}
}
