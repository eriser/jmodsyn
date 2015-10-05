package org.modsyn.modules.ext;

import java.awt.Component;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockComponent;
import org.modsyn.editor.DspPatchModel;
import org.modsyn.editor.EditorTheme;
import org.modsyn.editor.blocks.ToAsioModel;
import org.modsyn.editor.blocks.ToJavaSoundModel;
import org.modsyn.gui.JColorLabel;

public class AudioOutSupport {

	public static synchronized String normalizeClassName(String name) {
		if (ToAsioModel.class.getName().equals(name) || ToJavaSoundModel.class.getName().equals(name) || AudioOutSupport.class.getName().equals(name)) {
			return AudioOutSupport.class.getName();
		}

		return null;
	}

	@SuppressWarnings("serial")
	public static synchronized DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
		if (AsioSupport.INSTANCE.isSupported()) {
			return new DspBlockComponent(c, new ToAsioModel(c, AsioSupport.INSTANCE), pm) {
				@Override
				public Component createCenterComponent() {
					return new JColorLabel("\u25cb\u27a0", EditorTheme.COLOR_EXT_BLOCK_BG);
				}
			};
		} else {
			return new DspBlockComponent(c, new ToJavaSoundModel(new ToJavaSound(c, 2, 1024, true)), pm) {
				@Override
				public Component createCenterComponent() {
					return new JColorLabel("\u25cb\u27a0", EditorTheme.COLOR_EXT_BLOCK_BG);
				}
			};
		}
	}
}
