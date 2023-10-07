package org.example;

import java.util.*;

public class WaveGenerator {

    public static float[] generateSamples(double beats, double bpm, double sampleRate, double frequency, double amplitude, Waveform waveform) {
        double seconds = (beats / bpm) * 60;
//        double waveCycles = frequency * seconds;
//        int roundedWaveCycles = (int)Math.ceil(waveCycles);
//        double adjustedSeconds = roundedWaveCycles / frequency;

        int sampleCount = (int)Math.floor(seconds * sampleRate);
//        int sampleCount = (int)Math.floor(adjustedSeconds * sampleRate);
        float[] buffer = new float[sampleCount];
        double freqFactor = frequency * Math.PI * 2;

        for (int sample = 0; sample < buffer.length; sample++) {
            double time = sample / sampleRate;
            buffer[sample] = (float)(amplitude * waveform.calculate(freqFactor * time));
        }
        return buffer;
    }

    public static float[] generateSamples(Note note, double bpm, double sampleRate) {
        return generateSamples(note.getEndBeat() - note.getStartBeat(), bpm, sampleRate, note.getPitch().freq(),
                note.getAmplitude(), note.getWaveform());
    }

    public static byte[] convertSamplesToBytes(float[] samples, int bits) {
        int bytesPerSample = bits / 8;
        final byte[] byteBuffer = new byte[samples.length * bytesPerSample];

        int bufferIndex = 0;
        for (int i = 0; i < byteBuffer.length; i++) {
            final int x = (int)(samples[bufferIndex++] * Short.MAX_VALUE);

            byteBuffer[i++] = (byte)x;
            byteBuffer[i] = (byte)(x >>> 8);
        }

        return byteBuffer;
    }

    public static byte[] convertNotesToBytes(Collection<Note> notes, double bpm, double sampleRate, int bits) {
        // Find end
        double endBeat = notes.stream().map(Note::getEndBeat).reduce(Math::max).orElse(0.0);
        Map<Integer, Collection<Float>> sampleMap = new HashMap<>();
        // Load notes into byte buffer
        for(Note note : notes) {
            float[] noteSamples = generateSamples(note, bpm, sampleRate);
            int startIndex = (int)Math.round((note.getStartBeat() / bpm) * 60 * sampleRate);
            for(int i = 0; i < noteSamples.length; i++) {
                int sampleMapIndex = i + startIndex;
                sampleMap.putIfAbsent(sampleMapIndex, new ArrayList<>());
                sampleMap.get(sampleMapIndex).add(noteSamples[i]);
            }
        }
        // Create sample buffer for the whole length of the track
        int sampleCount = (int)Math.round((endBeat / bpm) * 60 * sampleRate);
        float[] samples = new float[sampleCount];
        for(int i = 0; i < sampleCount; i++) {
            Collection<Float> sampleCollectionAtIndex = sampleMap.get(i);
            if (sampleCollectionAtIndex != null) {
                // Get the average sample value at this index
                samples[i] = sampleCollectionAtIndex.stream().reduce(Float::sum).get() / sampleCollectionAtIndex.size();
            }
        }
        // Create byte buffer for the whole length of the track
        return convertSamplesToBytes(samples, bits);
    }
}
