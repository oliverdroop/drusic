import { Note } from "./NoteCollection";

export interface NoteDivProps {
  note: Note
  xFactor: number
  yFactor: number
  yOffset: number
  index: number
  selected: boolean
  grabNote(note: Note): void
  setIsResizeNote(isResizeNote: boolean): void
}

const NoteDiv = ({note, xFactor, yFactor, yOffset, index, selected, grabNote, setIsResizeNote}: NoteDivProps) => {

  return (
    <div>
      {/* The main body of the note */}
      <div className={`NoteDiv${selected ? " selected" : ""}`}
        style={{
          top: `${yOffset - (note.pitch.number * yFactor)}px`,
          left: `${note.startBeat * xFactor}px`,
          height: `${yFactor}px`,
          width: `${(note.endBeat - note.startBeat) * xFactor - 2}px`,
        }}
        onMouseDown={() => {
          grabNote(note);
        }}
      >
        {note.pitch.number}
      </div>
      {/* The right-hand border for resizing the note */}
      <div className="NoteDiv eResize"
        style={{
          top: `${yOffset - (note.pitch.number * yFactor)}px`,
          left: `${note.endBeat * xFactor - 2}px`,
          height: `${yFactor}px`,
          width: `${2}px`,
        }}
        onMouseDown={() => {
          grabNote(note);
          setIsResizeNote(true);
        }}
      >
    </div>
  </div>
  );
}

export default NoteDiv;
