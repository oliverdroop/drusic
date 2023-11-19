import { Button } from "react-bootstrap";
import { Track } from "../App";
import saveAs from "file-saver";
import { Note } from "./NotePanel";

interface SaveButtonProps {
  notes: Note[]
}

function SaveButton({notes}: SaveButtonProps) {

  const save = () => {
    const track = {} as Track;
    track.notes = notes;

    const output = JSON.stringify(track, null, 2);
    const file = new Blob([output], { type: 'text/plain;charset=utf-8' });
    saveAs(file, "myTrack.json");
  };
  
  return (
    <Button onClick={() => save()}>
      Save
    </Button>
  );
}

export default SaveButton;
