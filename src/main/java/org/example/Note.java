package org.example;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
@EqualsAndHashCode
public class Note {
    private final double startBeat;
    private final double endBeat;
    @JsonDeserialize(using = Pitch.PitchDeserializer.class) private final Pitch pitch;
    private final double amplitude;
    private final Instrument instrument;
}
