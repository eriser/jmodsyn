package org.modsyn.abnormal;

class NoAbnormalSupport implements AbnormalSupport {

	@Override
	public void setDenormals(boolean enable) {
	}

	@Override
	public boolean isSupported() {
		return false;
	}
}
