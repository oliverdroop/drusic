package org.example;

import java.util.function.Function;

import static java.lang.Math.PI;

public enum Waveform {
    SINE(Math::sin),
    TRIANGLE(d -> 2 * Math.asin(Math.sin(d)) / PI),
    SQUARE(d -> Math.signum(Math.sin(d)));

    private final Function<Double, Double> sampleCalculator;

    Waveform(Function<Double, Double> sampleCalculator) {
        this.sampleCalculator = sampleCalculator;
    }

    public double calculate(double t) {
        return sampleCalculator.apply(t);
    }
}
