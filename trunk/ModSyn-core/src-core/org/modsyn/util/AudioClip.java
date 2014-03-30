/*
 * Created on 6-apr-2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.modsyn.util;

/**
 * @author Erik Duijs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class AudioClip {
	
	private int channels;
	private int sampleRate;
	
	private float[] dataL;
	private float[] dataR;
	
	public AudioClip(int sampleRate, float[] data) {
		this.channels = 1;
		this.sampleRate = sampleRate;
		this.dataL = data;
	}

	public AudioClip(int sampleRate, float[] dataL, float[] dataR) {
		this.channels = 2;
		this.sampleRate = sampleRate;
		this.dataL = dataL;
		this.dataR = dataR;
	}
	/**
	 * @return Returns the channels.
	 */
	public int getChannels() {
		return channels;
	}

	/**
	 * @return Returns the data.
	 */
	public float[] getDataLeft() {
		return dataL;
	}

	/**
	 * @return Returns the data.
	 */
	public float[] getDataRight() {
		return dataR;
	}
	
	/**
	 * @return Returns the sampleRate.
	 */
	public int getSampleRate() {
		return sampleRate;
	}

}
