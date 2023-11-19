import { Button } from "react-bootstrap";
import { Track } from "../App";
import { postNotes } from "../services/noteService";
import { Note } from "./NotePanel";

interface LoadButtonProps {
  setNotes(notes: Note[]): void
}

function LoadButton({setNotes}: LoadButtonProps) {
  const load = (e: any) => {
    const fileReader = new FileReader();
    fileReader.readAsText(e.target.files[0], "UTF-8");
    fileReader.onload = e => {
      if (e.target && e.target.result) {
        const track = JSON.parse(e.target.result?.toString()) as Track;
        postNotes(track.notes).then(response => setNotes(response));
      }
    };
  };

  return (
    <Button onClick={() => document.getElementById("upload_input")?.click()}>
      <input id="upload_input" type="file" onChange={load} accept=".json"/>
      Load
    </Button>
  );
}

export default LoadButton;
