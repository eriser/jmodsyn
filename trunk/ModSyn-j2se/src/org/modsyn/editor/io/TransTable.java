package org.modsyn.editor.io;

import java.util.HashMap;
import java.util.Map;

class TransTable {

	private static final Map<String, String> map = new HashMap<>();

	static {
		for (int i = -1; i < 64; i++) {
			String nr = i == -1 ? "" : Integer.toString(i);
			map.put("velo" + nr, "vel" + nr);
			map.put("trig" + nr, "trg" + nr);
			map.put("v-sens" + nr, "sen" + nr);
			map.put("attack" + nr, "atk" + nr);
			map.put("decay" + nr, "dcy" + nr);
			map.put("release" + nr, "rel" + nr);
			map.put("freq" + nr, "frq" + nr);
			map.put("shape" + nr, "shp" + nr);
			map.put("filter" + nr, "fil" + nr);
			map.put("reso" + nr, "Q" + nr);
			map.put("mode" + nr, "typ" + nr);
			map.put("poles" + nr, "pol" + nr);
			map.put("bend" + nr, "pb" + nr);
			map.put("gain" + nr, "lvl" + nr);
			map.put("thrs" + nr, "thr" + nr);

			map.put("at-lvl" + nr, "atL" + nr);
			map.put("at-tim" + nr, "atT" + nr);
			map.put("dc-lvl" + nr, "dcL" + nr);
			map.put("dc-tim" + nr, "dcT" + nr);
			map.put("su-lvl" + nr, "suL" + nr);
			map.put("rl-lvl" + nr, "rlL" + nr);
			map.put("rl-tim" + nr, "rlT" + nr);

		}
	}

	public static String get(String name) {
		String s = map.get(name);
		if (s == null) {
			return name;
		} else {
			return s;
		}
	}
}
