/*
 * Created on 19-jun-07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.modsyn.midi;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author DU1381
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MIDIFile {

    ArrayList<MTrk> tracks;

    BufferedInputStream bis;

    byte[] b4 = new byte[4];
    byte[] buf = new byte[0x10000];

    // MThd chunk
    int midiFormat;
    int nrOfTracks;
    int division;

    public MIDIFile(String path) throws IOException {
        URL url = Thread.currentThread().getContextClassLoader().getResource(path);

        tracks = new ArrayList<MTrk>();
        try {
            bis = new BufferedInputStream(url.openStream());
            read();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            bis.close();
        }
    }

    public static void main(String[] args) {
        try {
            new MIDIFile(args[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     */
    private void read() throws IOException {

        System.out.println("Loading MIDI file ");
        String id;

        while ((id = readID()) != null) {
            int len = readLen();

            System.out.println(id + ", len=" + len);

            if (id.equals("MThd") && len == 6) {
                readMThd(len);
            } else if (id.equals("MTrk")) {
                readMTrk(len);
            } else {
                System.out.println("????");
            }
        }

        System.out.println("MIDI file loaded successfully!");
    }

    /**
     * @param len
     */
    private void readMThd(int len) throws IOException {
        bis.read(buf, 0, len);

        midiFormat = ((buf[0] & 0xff) << 8) | (buf[1] & 0xff);
        nrOfTracks = ((buf[2] & 0xff) << 8) | (buf[3] & 0xff);
        division = ((buf[4]) << 8) | (buf[5] & 0xff);

        System.out.println(" format:" + midiFormat + " tracks:" + nrOfTracks + " division:" + division);
    }

    /**
     * @param len
     */
    private void readMTrk(int len) throws IOException {
        tracks.add(new MTrk(bis, len));
    }

    private String readID() throws IOException {
        int read = bis.read(b4);

        if (read == 4) {
            return new String(b4);
        } else {
            return null;
        }
    }

    private int readLen() throws IOException {
        int read = bis.read(b4);

        if (read == 4) {
            return ((b4[0] & 0xff) << 24) | ((b4[1] & 0xff) << 16) | ((b4[2] & 0xff) << 8) | (b4[3] & 0xff);
        } else {
            return -1;
        }
    }

    public ArrayList<Event> getAllOrderedEvents() {
        ArrayList<Event> list = new ArrayList<Event>();

        for (int i = 0; i < tracks.size(); i++) {
            MTrk mtrk = (MTrk) tracks.get(i);
            list.addAll(mtrk.events);
        }

        Collections.sort(list, new Comparator<Event>() {
            public int compare(Event e1, Event e2) {
                return (int) (e1.getOffset() - e2.getOffset());
            }
        });

        return list;
    }
}
