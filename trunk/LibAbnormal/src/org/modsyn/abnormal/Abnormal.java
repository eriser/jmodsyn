package org.modsyn.abnormal;

/**
 * This class enables setting native floating point behaviour to 'flush-to-zero' mode. This can have great performance
 * benefits in applications where floating point denormals can occur.<br/>
 * Note: Enabling 'flush-to-zero' mode (i.e. calling Abnormal.setDenormals(false);) will make the JVM run out-of-spec,
 * and might cause unexpected behaviour. It is therefore recommended to only activate this mode where needed, and
 * re-enable denormals afterwards (i.e. calling Abnormal.setDenormals(true);).
 */
public class Abnormal {

	private static final AbnormalSupport impl;

	static {
		String osname = System.getProperty("os.name").toLowerCase();
		String osarch = System.getProperty("os.arch").toLowerCase();
		int bits = osarch.contains("64") ? 64 : 32;

		AbnormalSupport support = null;
		if (osname.contains("win")) {
			try {
				support = new WinAbnormalSupport(bits);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		if (support == null) {
			support = new NoAbnormalSupport();
		}

		impl = support;
	}

	/**
	 * Flag to signify if floating point flush-to-zero mode is supported.
	 */
	public static final boolean SUPPORTED = impl.isSupported();

	/**
	 * Enable floating point denormals.
	 * 
	 * @param enable
	 *            true=enable denormals (default), false=flush denormals to zero.
	 */
	public static synchronized void setDenormals(boolean enable) {
		impl.setDenormals(enable);
	}

	public static void main(String[] args) {
		System.out.println("supported:" + SUPPORTED);
		setDenormals(false);
		setDenormals(true);
	}
}
