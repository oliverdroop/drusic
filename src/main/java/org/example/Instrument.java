package org.example;

import java.util.function.BiFunction;
import java.util.stream.IntStream;

public enum Instrument {

    SQUARE_TO_SINE((time, length) -> {
        double decayFactor = time / length;
        return (Waveform.SQUARE.calculate(time) * (1 - decayFactor)) + (Waveform.SINE.calculate(time) * decayFactor);
    }),
    TRIANGLE_TO_SINE((time, length) -> {
        double decayFactor = time / length;
        return (Waveform.TRIANGLE.calculate(time) * (1 - decayFactor)) + (Waveform.SINE.calculate(time) * decayFactor);
    }),
    RHODES((time, length) -> {
        double volumeFactor = 1 - Math.pow(time / length, 2);
        return ((Waveform.SINE.calculate(time) / 3)
                + (Waveform.SINE.calculate(time / 2) / 3)
                + (Waveform.SINE.calculate(time / 3) / 3))
                * volumeFactor;
    }),
    KOTO((time, length) -> {
        double volumeFactor = 1 - Math.pow(time / length, 2);
        int harmonics = 15;
        return IntStream.range(1, harmonics).mapToDouble(harmonicIndex -> Waveform.SINE.calculate(time * harmonicIndex)).sum() * volumeFactor;
    });

    private final BiFunction<Double, Double, Double> sampleCalculator;

    Instrument(BiFunction<Double, Double, Double> sampleCalculator) {
        this.sampleCalculator = sampleCalculator;
    }

    public double calculate(double time, double length) {
        return sampleCalculator.apply(time, length);
    }
}
