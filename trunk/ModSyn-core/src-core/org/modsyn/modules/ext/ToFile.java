package org.modsyn.modules.ext;

import java.io.IOException;

import org.modsyn.SignalInput;
import org.modsyn.util.WavWriter;

public class ToFile {
    
    SignalInput connL, connR;
    WavWriter wav;

    public SignalInput inL = new SignalInput() {
        public void set(float sample) {
            try {
                wav.write(sample);
                if (connL != null) connL.set(sample);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public SignalInput inR = new SignalInput() {
        public void set(float sample) {
            try {
                wav.write(sample);
                if (connR != null) connR.set(sample);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
    };
    
    public ToFile(String path) throws IOException {
        wav = new WavWriter();
        wav.open(path);
    }
    
    public void connectTo(SignalInput inputL, SignalInput inputR) {
        connL = inputL;
        connR = inputR;
    }
    
    public void close() throws IOException {
        wav.close();
    }
    
    
}
