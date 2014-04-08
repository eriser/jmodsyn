package org.modsyn.editor;

import java.io.File;

import org.modsyn.Context;
import org.modsyn.editor.blocks.MetaModel;
import org.modsyn.editor.io.FileSys;
import org.modsyn.editor.io.XmlImportMeta;

public class DspBlockComponentFactory {

	public static DspBlockComponent create(String className, String name, Context c, DspPatchModel pm, int channels) {
		if (className.equals(MetaModel.class.getName())) {
			try {
				XmlImportMeta im = new XmlImportMeta(new File(FileSys.dirMeta, name + ".dsp-patch"), c, pm);
				return im.importedMetaBlock;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		} else {
			return DspPalette.createFromModelName(className, c, pm, channels);
		}
	}
}
