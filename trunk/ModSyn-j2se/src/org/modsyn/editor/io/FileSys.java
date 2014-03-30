package org.modsyn.editor.io;

import java.io.File;

public class FileSys {

	public static final File dirPatches = new File(System.getProperty("modsyn.patches", System.getProperty("user.home") + "/ModSyn"));
	public static final File dirMeta = new File(dirPatches, "meta");

	static {
		if (!dirPatches.exists()) {
			dirPatches.mkdirs();
		}
		if (!dirMeta.exists()) {
			dirMeta.mkdirs();
		}
	}
}
