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

		private final double[] buffer;
		public final double[] result;
		public double max = 1;
		private int index;

		/**
		 * 
		 * @param context
		 *            The context
		 * @param size
		 *            Size of the result
		 */
		public FFTAnalysis(Context c, int size) {
			this.fftSize = size * 2;
			this.buffer = new double[fftSize * 2];
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

			connectedInput.set(signal);
		}

		/**
		 * Get the frequency intensities.
		 * 
		 * @param wave
		 *            amplitudes of the signal
		 * @return intensities of each frequency unit:
		 *         mag[frequency_unit]=intensity
		 */
		private void transform() {
			fft.transform(buffer);

			max = 1;
			for (int i = 0; i < fftSize; i += 2) {
				double a = Math.sqrt(buffer[i] * buffer[i] + buffer[i + 1] * buffer[i + 1]);
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
