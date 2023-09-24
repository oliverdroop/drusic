package org.example.controllers;

import com.sun.media.sound.WaveFileWriter;
import lombok.RequiredArgsConstructor;
import org.example.WaveGenerator;
import org.example.services.NoteService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class PlayController {

    private final NoteService noteService;

    private final double sampleRate = 44100.0;
    private final double bpm = 106;
    private final boolean bigEndian = false;
    private final boolean signed = true;
    private final int bits = 16;
    private final int channels = 1;
    private AudioFormat format = new AudioFormat((float)sampleRate, bits, channels, signed, bigEndian);

    @CrossOrigin(value = "http://localhost:3000")
    @GetMapping(value = "/playAll")
    public ResponseEntity<StreamingResponseBody> playAll() throws LineUnavailableException, IOException {
        byte[] byteBuffer = WaveGenerator.convertNotesToBytes(noteService.getNotes(), bpm, sampleRate, bits);

        int streamLength = byteBuffer.length / (bits / 8);
        ByteArrayInputStream bais = new ByteArrayInputStream(byteBuffer);
        AudioInputStream audioInputStream = new AudioInputStream(bais, format, streamLength);

        WaveFileWriter waveFileWriter = new WaveFileWriter();
        StreamingResponseBody responseBody = out -> waveFileWriter.write(audioInputStream, AudioFileFormat.Type.WAVE, out);
        return ResponseEntity.ok().contentType(MediaType.asMediaType(MimeType.valueOf("audio/wav"))).body(responseBody);
    }
}
