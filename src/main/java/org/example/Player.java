package org.example;

import javax.sound.sampled.*;
import java.io.IOException;

public class Player {

    public static void play(AudioFormat format, AudioInputStream audioInputStream)
            throws LineUnavailableException, IOException {
        SourceDataLine sourceDataLine = AudioSystem.getSourceDataLine(format);
        sourceDataLine.open();
        sourceDataLine.start();
        int bufferSize = 4096;
        byte[] bufferBytes = new byte[bufferSize];
        int readBytes = -1;
        while ((readBytes = audioInputStream.read(bufferBytes)) != -1) {
            sourceDataLine.write(bufferBytes, 0, readBytes);
        }
        sourceDataLine.drain();
        sourceDataLine.close();
        audioInputStream.close();
    }
}
