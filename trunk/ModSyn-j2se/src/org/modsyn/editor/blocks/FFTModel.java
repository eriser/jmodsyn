package org.modsyn.editor.blocks;

import javax.swing.SwingUtilities;

import org.modsyn.Context;
import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInsert;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;

import com.sun.media.sound.FFT;

public class FFTModel extends DspBlockModel<FFTModel.FFTAnalysis> {

	Runnable updater = new Runnable() {
		@Override
		public void run() {
			if (component != null) {
				component.repaint();
			}
		}
	};

	public static class FFTAnalysis extends DefaultSignalOutput implements SignalInsert {

		private FFTModel model;

		private final FFT fft;
		private final int fftSize;

		private final double[] buffer, render;
		public final double[] result;
		public double max = 1;
		private int index;

		private final double[] winFunc;

		/**
		 * 
		 * @param context
		 *            The context
		 * @param size
		 *            Size of the result
		 */
		public FFTAnalysis(Context c, int size) {
			this.fftSize = size * 2; // we need twice the size as the FFT class generates the result in 2 values per
										// result index
			this.buffer = new double[fftSize * 2]; // the 2nd half produced by the FFT will be disregarded (negative
													// frequencies)
			this.render = new double[buffer.length];
			this.winFunc = new double[fftSize];

			for (int i = 0; i < fftSize; i++) {
				double r = Math.PI / size;
				for (int n = -size; n < size; n++) {
					winFunc[size + n] = 0.54f + 0.46f * Math.cos(n * r);
				}
			}

			this.result = new double[size];

			this.fft = new FFT(fftSize, -1);
		}

		@Override
		public void set(float signal) {
			if (index >= buffer.length) {
				index = 0;
				transform();
				SwingUtilities.invokeLater(model.updater);
			}

			buffer[index++] = signal;
			buffer[index++] = 0;

			connectedInput.set(signal);
		}

		/**
		 * Get the frequency intensities.
		 */
		private void transform() {
			System.arraycopy(buffer, 0, render, 0, render.length);

			// for (int i = 0; i < render.length; i++) {
			// render[i] *= winFunc[i % winFunc.length];
			// }

			fft.transform(render);

			max = 1;
			for (int i = 0; i < fftSize; i += 2) {
				double a = Math.sqrt(render[i] * render[i] + render[i + 1] * render[i + 1]);
				if (a > max) {
					max = a;
				}
				result[i / 2] = a;
			}
		}
	}

	public FFTModel(Context c) {
		super(new FFTAnalysis(c, 256));
		getDspObject().model = this;
		add(new InputModel(this, getDspObject(), "IN", 0, -1, 1));

		add(new OutputModel(this, getDspObject(), "OUT"));
	}

	@Override
	public String getName() {
		return "FFT";
	}
}
