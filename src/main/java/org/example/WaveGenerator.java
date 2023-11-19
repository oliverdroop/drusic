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
        Map<Integer, Collection<Float>> sampleMap = new HashMap<>();
        // Load notes into byte buffer
        for(Note note : notes) {
            float[] noteSamples = generateSamples(note, bpm, sampleRate);
            int startIndex = (int)Math.round((note.getStartBeat() / bpm) * 60 * sampleRate);
            for(int i = 0; i < noteSamples.length; i++) {
                int sampleMapIndex = i + startIndex;
                sampleMap.putIfAbsent(sampleMapIndex, new LinkedList<>());
                sampleMap.get(sampleMapIndex).add(noteSamples[i]);
            }
        }
        // Create sample buffer for the whole length of the track
        int sampleCount = (int) Math.round((endBeat / bpm) * 60 * sampleRate);
        float[] samples = new float[sampleCount];
        float maxWaveformValue = combineNoteSamplesAndReturnMaximum(sampleMap, samples);

        // Reduce each sample so that clipping can be avoided
        reduceSamples(samples, maxWaveformValue);

        // Create byte buffer for the whole length of the track
        return convertSamplesToBytes(samples, bits);
    }

    private static float combineNoteSamplesAndReturnMaximum(Map<Integer, Collection<Float>> sampleMap, float[] unclippedSamples) {
        float maxWaveformValue = 0;
        for(int i = 0; i < unclippedSamples.length; i++) {
            Collection<Float> sampleCollectionAtIndex = sampleMap.get(i);
            if (sampleCollectionAtIndex != null) {
                // Get the total sample value at this index
                float waveformValue = sampleCollectionAtIndex.stream().reduce(simpleFloatCombinator()).orElse(0f);
                unclippedSamples[i] = waveformValue;
                // Find the max waveform value for later reduction
                maxWaveformValue = Math.max(Math.abs(waveformValue), maxWaveformValue);
            }
        }
        return maxWaveformValue;
    }

    private static void reduceSamples(float[] samples, float maxWaveformValue) {
        float reductionFactor = 1 / maxWaveformValue;
        for(int i = 0; i < samples.length; i++) {
            samples[i] = samples[i] * reductionFactor;
        }
    }

    private static BinaryOperator<Float> simpleFloatCombinator() {
        return Float::sum;
    }

    private static BinaryOperator<Float> clippingFloatCombinator() {
        return (float1, float2) -> {
            float combined = float1 + float2;
            if (combined > 1) {
                return 1f;
            } else if (combined < -1) {
                return -1f;
            }
            return combined;
        };
    }
}
