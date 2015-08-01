package org.modsyn.abnormal;

class WinAbnormalSupport implements AbnormalSupport {

	WinAbnormalSupport(int bits) {
		System.loadLibrary("abnormal" + bits);
	}

	@Override
	public boolean isSupported() {
		return true;
	}

	@Override
	public native void setDenormals(boolean enable);
}