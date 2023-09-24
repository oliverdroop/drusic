package org.example;

public enum Pitch {
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
    AN_5(5, 10);
    private int octaveNumber;
    private int semitoneNumber;
    Pitch(int octaveNumber, int semitoneNumber) {
        this.octaveNumber = octaveNumber;
        this.semitoneNumber = semitoneNumber;
    }

    public double freq() {
        return Math.pow(Math.pow(2, (1 / (double)12)), octaveNumber * 12 + semitoneNumber - 58) * 440;
    }
}
