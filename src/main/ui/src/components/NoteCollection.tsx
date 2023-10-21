import { useEffect, useState } from 'react';
import NoteDiv from './NoteDiv';
import { getNotes, postNote, postNotes, deleteNotes } from '../services/noteService.ts';
import NoteSnapGrid from './NoteSnapGrid.tsx';

export interface Pitch {
  frequency: number
  number: number
}

export interface Note {
  startBeat: number
  endBeat: number
  pitch: Pitch
  amplitude: number
  waveform: string
}

const NoteCollection = () => {
  const id = "notecollection";
  const xFactor = 40;
  const yFactor = 10;
  const yOffset = 880;
  const timeSnap = 0.5;
  const noteCollection = document.getElementById(id);
  const offsetLeft = noteCollection?.offsetLeft ?? 0;
  const offsetTop = noteCollection?.offsetTop ?? 0;

  const [mouseX, setMouseX] = useState(0);
  const [mouseY, setMouseY] = useState(0);
  const [mouseDownButton, setMouseDownButton] = useState(-1);
  const [mouseDownX, setMouseDownX] = useState(0);
  const [mouseDownY, setMouseDownY] = useState(0);
  const [notes, setNotes] = useState([] as Note[]);
  const [grabbedNotes, setGrabbedNotes] = useState([] as Note[]);
  const [selectedNotes, setSelectedNotes] = useState([] as Note[]);
  const [isResizeNote, setIsResizeNote] = useState(false);

  useEffect(() => {
    const handleContextMenu = (event: any) => {
      event.preventDefault();
    };
    const rootElement = document.getElementById(id);
    rootElement?.addEventListener("contextmenu", handleContextMenu);

    getNotes().then(response => setNotes(response));
    }, []);

  const onMouseMove = (event: any) => {
    setMouseX(event.clientX);
    setMouseY(event.clientY);
  };

  const onMouseDown = (event: any) => {
    setMouseDownX(event.clientX);
    setMouseDownY(event.clientY);
    setMouseDownButton(event.button);
    event.preventDefault();
  };

  const onMouseUp = (event: any) => {
    if (event.button !== mouseDownButton) {
      return;
    }
    const pitchChange = calculatePitchChange();
    const timeChange = calculateTimeChange();
    if (grabbedNotes.length > 0) {
      if (mouseDownButton === 0) {
        if (pitchChange !== 0 || timeChange !== 0) {
          grabbedNotes.forEach(grabbedNote => {
            if (isResizeNote) {
              // Resize a note
              grabbedNote.endBeat += Math.max(timeChange, grabbedNote.startBeat - grabbedNote.endBeat + timeSnap);
            } else {
              // Move a note
              grabbedNote.pitch.number += pitchChange;
              grabbedNote.startBeat += timeChange;
              grabbedNote.endBeat += timeChange;
            }
          });
          postNotes(notes).then(response => {
            setNotes(response);
            // Update selected notes
            setSelectedNotes(response
              .filter(newNote => selectedNotes
                .find(oldNote => 
                  oldNote.amplitude === newNote.amplitude
                  && oldNote.pitch.number === newNote.pitch.number
                  && oldNote.startBeat === newNote.startBeat
                  && oldNote.endBeat === newNote.endBeat
                  && oldNote.waveform === newNote.waveform
                )
              )
            );
          });
        }
      } else if (mouseDownButton === 2) {
        // Delete a note
        deleteNotes(grabbedNotes).then(response => setNotes(response));
        setSelectedNotes([]);
      }
    } else if (isCreatingNewNote()) {
      // Create a note
      const startBeat = getBeatNumber(mouseDownX - offsetLeft);
      const pitchNumber = getPitchNumber(mouseDownY - offsetTop);
      const endBeat = Math.max(timeSnap, timeChange) + startBeat;
      const newNote: Note = {
        startBeat: startBeat,
        endBeat: endBeat,
        amplitude: 0.5,
        pitch: { number: pitchNumber } as Pitch,
        waveform: "SINE"}
      postNote(newNote).then(response => setNotes(response));
      setSelectedNotes([]);
    } else {
      // Select notes
      const startBeat = getBeatNumber(Math.min(mouseDownX, mouseX) - offsetLeft);
      const endBeat = getBeatNumber(Math.max(mouseDownX, mouseX) - offsetLeft);
      const pitchHigh = getPitchNumber(Math.min(mouseDownY, mouseY) - offsetTop);
      const pitchLow = getPitchNumber(Math.max(mouseDownY, mouseY) - offsetTop);
      console.log("startBeat: %s endBeat: %s pitchHigh: %s pitchLow: %s", startBeat, endBeat, pitchHigh, pitchLow);
      const newNoteSelection = notes.filter(note => 
        note.endBeat > startBeat 
        && note.startBeat <= endBeat 
        && note.pitch.number <= pitchHigh 
        && note.pitch.number >= pitchLow);
      setSelectedNotes(newNoteSelection);
    }
    setGrabbedNotes([]);
    setIsResizeNote(false);
    setMouseDownX(0);
    setMouseDownY(0);
    setMouseDownButton(-1);
  };

  const getBeatNumber = (posX: number) => {
    return timeSnap * Math.floor(posX / (xFactor * timeSnap));
  };

  const getXForBeat = (beat: number) => {
    return beat * xFactor;
  };

  const calculateTimeChange = () => {
    const diffX = mouseX - mouseDownX;
    return timeSnap * Math.round(diffX / (xFactor * timeSnap));
  };

  const getPitchNumber = (posY: number) => {
    return -Math.floor((posY - yOffset) / yFactor);
  };

  const getYForPitch = (pitch: number) => {
    return pitch * yFactor;
  };

  const calculatePitchChange = () => {
    const diffY = mouseY - mouseDownY;
    return -Math.round(diffY / yFactor);
  };

  const isCreatingNewNote = () => {
    return mouseDownButton === 0 && selectedNotes.length === 0 && calculatePitchChange() === 0 && calculateTimeChange() >= 0;
  };

  const grabNote = (note: Note) => {
    if (!selectedNotes.includes(note)) {
      setSelectedNotes([note]);
      setGrabbedNotes([note]);
    } else {
      setGrabbedNotes(selectedNotes);
    }
  };

  return (
    <div id={id} className="NoteCollection" onMouseDown={onMouseDown} onMouseUp={onMouseUp} onMouseMove={onMouseMove}>
      {notes !== undefined && notes.map((note, i) => 
        <NoteDiv 
          key={`note_${i}`}
          note={note}
          xFactor={xFactor}
          yFactor={yFactor}
          yOffset={yOffset}
          index={i}
          selected={selectedNotes.includes(note)}
          grabNote={grabNote}
          setIsResizeNote={setIsResizeNote}
        />
      )}
      
      <NoteSnapGrid getPitchNumber={getPitchNumber}/>

      {/* Show the change for grabbed note(s) while the mouse is clicked */}
      {grabbedNotes.length > 0 && mouseDownButton === 0 && (
        grabbedNotes.map(grabbedNote =>
          <div
            className='DragBox'
            style={{
              top: `${yOffset - ((grabbedNote.pitch.number + calculatePitchChange()) * yFactor)}px`,
              left: `${(grabbedNote.startBeat + (isResizeNote ? 0 : calculateTimeChange())) * xFactor}px`,
              height: `${yFactor}px`,
              width: `${(grabbedNote.endBeat - grabbedNote.startBeat - (isResizeNote ? -calculateTimeChange() : 0)) * xFactor}px`
            }}
          />
        )
      )}
      {mouseDownX !== 0 && mouseDownY !== 0 && mouseDownButton === 0 && grabbedNotes.length === 0 && (
        <div>
          {/* Show simple drag box(es) when the mouse is clicked with a pitch change */}
          {!isCreatingNewNote() && (
            <div 
              className='DragBox'
              style={{
                top: `${Math.min(mouseDownY, mouseY) - offsetTop}px`,
                left: `${Math.min(mouseDownX, mouseX) - offsetLeft}px`,
                height: `${Math.abs(mouseY - mouseDownY)}px`,
                width: `${Math.abs(mouseX - mouseDownX)}px`
              }}
            />
          )}
          {/* Show the new note while the mouse is clicked with no pitch change */}
          {isCreatingNewNote() && (
            <div 
              className='DragBox'
              style={{
                top: `${yOffset - getYForPitch(getPitchNumber(mouseDownY - offsetTop))}px`,
                left: `${getXForBeat(getBeatNumber(mouseDownX - offsetLeft))}px`,
                height: `${yFactor}px`,
                width: `${calculateTimeChange() * xFactor}px`
              }}
            />
          )}
        </div>
      )}
    </div>
  )};

export default NoteCollection;
