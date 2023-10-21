package org.example;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Arrays;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@NoArgsConstructor(force = true)
public enum Pitch {
    CN_1(1, 1),
    CS_1(1, 2),
    DF_1(1, 2),
    DN_1(1, 3),
    DS_1(1, 4),
    EF_1(1, 4),
    EN_1(1, 5),
    FN_1(1, 6),
    FS_1(1, 7),
    GF_1(1, 7),
    GN_1(1, 8),
    GS_1(1, 9),
    AF_1(1, 9),
    AN_1(1, 10),
    AS_1(1, 11),
    BF_1(1, 11),
    BN_1(1, 12),
    CN_2(2, 1),
    CS_2(2, 2),
    DF_2(2, 2),
    DN_2(2, 3),
    DS_2(2, 4),
    EF_2(2, 4),
    EN_2(2, 5),
    FN_2(2, 6),
    FS_2(2, 7),
    GF_2(2, 7),
    GN_2(2, 8),
    GS_2(2, 9),
    AF_2(2, 9),
    AN_2(2, 10),
    AS_2(2, 11),
    BF_2(2, 11),
    BN_2(2, 12),
    CN_3(3, 1),
    CS_3(3, 2),
    DF_3(3, 2),
    DN_3(3, 3),
    DS_3(3, 4),
    EF_3(3, 4),
    EN_3(3, 5),
    FN_3(3, 6),
    FS_3(3, 7),
    GF_3(3, 7),
    GN_3(3, 8),
    GS_3(3, 9),
    AF_3(3, 9),
    AN_3(3, 10),
    AS_3(3, 11),
    BF_3(3, 11),
    BN_3(3, 12),
    CN_4(4, 1),
    CS_4(4, 2),
    DF_4(4, 2),
    DN_4(4, 3),
    DS_4(4, 4),
    EF_4(4, 4),
    EN_4(4, 5),
    FN_4(4, 6),
    FS_4(4, 7),
    GF_4(4, 7),
    GN_4(4, 8),
    GS_4(4, 9),
    AF_4(4, 9),
    AN_4(4, 10),
    AS_4(4, 11),
    BF_4(4, 11),
    BN_4(4, 12),
    CN_5(5, 1),
    CS_5(5, 2),
    DF_5(5, 2),
    DN_5(5, 3),
    DS_5(5, 4),
    EF_5(5, 4),
    EN_5(5, 5),
    FN_5(5, 6),
    FS_5(5, 7),
    GF_5(5, 7),
    GN_5(5, 8),
    GS_5(5, 9),
    AF_5(5, 9),
    AN_5(5, 10),
    AS_5(5, 11),
    BF_5(5, 11),
    BN_5(5, 12),
    CN_6(6, 1),
    CS_6(6, 2),
    DF_6(6, 2),
    DN_6(6, 3),
    DS_6(6, 4),
    EF_6(6, 4),
    EN_6(6, 5),
    FN_6(6, 6),
    FS_6(6, 7),
    GF_6(6, 7),
    GN_6(6, 8),
    GS_6(6, 9),
    AF_6(6, 9),
    AN_6(6, 10),
    AS_6(6, 11),
    BF_6(6, 11),
    BN_6(6, 12),
    CN_7(7, 1),
    CS_7(7, 2),
    DF_7(7, 2),
    DN_7(7, 3),
    DS_7(7, 4),
    EF_7(7, 4),
    EN_7(7, 5),
    FN_7(7, 6),
    FS_7(7, 7),
    GF_7(7, 7),
    GN_7(7, 8),
    GS_7(7, 9),
    AF_7(7, 9),
    AN_7(7, 10),
    AS_7(7, 11),
    BF_7(7, 11),
    BN_7(7, 12),
    CN_8(8, 1),;

    @JsonIgnore private final int octaveNumber;
    @JsonIgnore private final int semitoneNumber;
    private final int number;

    Pitch(int octaveNumber, int semitoneNumber) {
        this.octaveNumber = octaveNumber;
        this.semitoneNumber = semitoneNumber;
        this.number = octaveNumber * 12 + semitoneNumber - 9;
    }

    public double freq() {
        return Math.pow(Math.pow(2, (1 / (double)12)), octaveNumber * 12 + semitoneNumber - 58) * 440;
    }

    public static class PitchDeserializer extends StdDeserializer<Pitch> {

        public PitchDeserializer() {
            this(null);
        }

        public PitchDeserializer(Class<?> clazz) {
            super(clazz);
        }

        @Override
        public Pitch deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            int number = node.get("number").asInt();
            return Arrays.stream(Pitch.values())
                    .filter(pitch -> pitch.getNumber() == number)
                    .findFirst()
                    .orElse(Pitch.AN_4);
        }
    }
}
