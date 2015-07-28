package org.modsyn.modules.ext;

import java.awt.Component;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockComponent;
import org.modsyn.editor.DspPatchModel;
import org.modsyn.editor.EditorTheme;
import org.modsyn.editor.blocks.FromAsioModel;
import org.modsyn.editor.blocks.FromJavaSoundModel;
import org.modsyn.gui.JColorLabel;

public class AudioInSupport {

	public static synchronized String normalizeClassName(String name) {
		if (FromAsioModel.class.getName().equals(name) || FromJavaSoundModel.class.getName().equals(name) || AudioInSupport.class.getName().equals(name)) {
			return AudioInSupport.class.getName();
		}

		return null;
	}

	@SuppressWarnings("serial")
	public static synchronized DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
		if (AsioSupport.INSTANCE.isSupported()) {
			return new DspBlockComponent(c, new FromAsioModel(c, AsioSupport.INSTANCE), pm) {
				@Override
				public Component createCenterComponent() {
					return new JColorLabel("\u27a0\u25cb", EditorTheme.COLOR_EXT_BLOCK_BG);
				}
			};
		} else {
			return new DspBlockComponent(c, new FromJavaSoundModel(c), pm) {
				@Override
				public Component createCenterComponent() {
					return new JColorLabel("\u27a0\u25cb", EditorTheme.COLOR_EXT_BLOCK_BG);
				}
			};
		}
	}
}
