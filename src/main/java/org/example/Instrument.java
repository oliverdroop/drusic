package org.example;

import java.util.function.BiFunction;

public enum Instrument {

    SQUARE_TO_SINE((time, length) -> {
        double decayFactor = time / length;
        return (Waveform.SQUARE.calculate(time) * (1 - decayFactor)) + (Waveform.SINE.calculate(time) * decayFactor);
    }),
    TRIANGLE_TO_SINE((time, length) -> {
        double decayFactor = time / length;
        return (Waveform.TRIANGLE.calculate(time) * (1 - decayFactor)) + (Waveform.SINE.calculate(time) * decayFactor);
    });

    private final BiFunction<Double, Double, Double> sampleCalculator;

    Instrument(BiFunction<Double, Double, Double> sampleCalculator) {
        this.sampleCalculator = sampleCalculator;
    }

    public double calculate(double time, double length) {
        return sampleCalculator.apply(time, length);
    }
}