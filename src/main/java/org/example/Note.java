package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Note {
    private final double startBeat;
    private final double endBeat;
    private final double frequency;
    private final double amplitude;
    private final Waveform waveform;
}
