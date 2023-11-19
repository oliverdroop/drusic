package org.example;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.IntStream;

public class WaveGenerator {

    private static float[] generateSamples(Note note, double bpm, double sampleRate) {
        return generateSamples(note.getEndBeat() - note.getStartBeat(), bpm, sampleRate, note.getPitch().freq(),
                note.getAmplitude(), note.getWaveform());
    }

    private static float[] generateSamples(double beats, double bpm, double sampleRate, double frequency, double amplitude, Waveform waveform) {
        return generateSamplesModulatingWaveform(beats, bpm, sampleRate, frequency, amplitude, waveform, Waveform.SINE);
    }


    private static float[] generateSamplesModulatingWaveform(double beats, double bpm, double sampleRate, double frequency, double amplitude, Waveform waveform1, Waveform waveform2) {
        double seconds = (beats / bpm) * 60;

        int sampleCount = (int)Math.floor(seconds * sampleRate);
        float[] buffer = new float[sampleCount];
        double freqFactor = frequency * Math.PI * 2;

        for (int sample = 0; sample < buffer.length; sample++) {
            double time = sample / sampleRate;
            float wave1Value = (float)(((1 - (sample / (double) buffer.length)) * amplitude) * waveform1.calculate(freqFactor * time));
            float wave2Value = (float)(((sample / (double) buffer.length) * amplitude) * waveform2.calculate(freqFactor * time));
            buffer[sample] = wave1Value + wave2Value;
        }
        return buffer;
    }

    private static byte[] convertSamplesToBytes(float[] samples, int bits) {
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

        // Find the number of samples over the whole length of the track
        int sampleCount = getSampleIndexFromBeat(endBeat, bpm, sampleRate);
        float[] samples = new float[sampleCount];

        // Load notes into byte buffer
        for(Note note : notes) {
            float[] noteSamples = generateSamples(note, bpm, sampleRate);
            int startIndex = getSampleIndexFromBeat(note.getStartBeat(), bpm, sampleRate);
            for(int i = 0; i < noteSamples.length; i++) {
                samples[i + startIndex] += noteSamples[i];
            }
        }
        // Reduce each sample so that clipping can be avoided
        reduceSamples(samples);

        // Create byte buffer for the whole length of the track
        return convertSamplesToBytes(samples, bits);
    }

    private static int getSampleIndexFromBeat(double beat, double bpm, double sampleRate) {
        return (int) Math.round((beat / bpm) * 60 * sampleRate);
    }

    private static float getMaximumSampleValue(float[] unclippedSamples) {
        double maximumSampleValue = IntStream
                .range(0, unclippedSamples.length)
                .mapToDouble(i -> unclippedSamples[i])
                .map(Math::abs)
                .max()
                .orElse(0);

        if (maximumSampleValue == 0) {
            throw new RuntimeException("Maximum sample value is 0");
        }
        return (float) maximumSampleValue;
    }

    private static void reduceSamples(float[] samples) {
        float reductionFactor = 1 / getMaximumSampleValue(samples);
        for(int i = 0; i < samples.length; i++) {
            samples[i] = samples[i] * reductionFactor;
        }
    }
}
