import './App.css';
import NotePanel, { Note } from './components/NotePanel';
import AudioPlayer from './components/AudioPlayer';
import { useState } from 'react';
import { Button } from 'react-bootstrap';
import { saveAs } from 'file-saver';
import { postNotes } from './services/noteService';

interface Track {
  notes: Note[]
}

function App() {
  const [keysPressed, setKeysPressed] = useState([] as string[]);
  const [notes, setNotes] = useState([] as Note[]);

  const onKeyDown = (event: any) => {
    const newKeysPressed = keysPressed.slice();
    let index = newKeysPressed.indexOf(event.key);
    if (index < 0) {
      newKeysPressed.push(event.key);
      setKeysPressed(newKeysPressed);
    }
  };

  const onKeyUp = (event: any) => {
    const newKeysPressed = keysPressed.slice();
    let index = newKeysPressed.indexOf(event.key);
    while (index >= 0) {
      newKeysPressed.splice(index, 1);
      index = newKeysPressed.indexOf(event.key);
    }
    setKeysPressed(newKeysPressed);
  }

  const save = () => {
    const track = {} as Track;
    track.notes = notes;

    const output = JSON.stringify(track, null, 2);
    const file = new Blob([output], { type: 'text/plain;charset=utf-8' });
    saveAs(file, "myTrack.json");
  };

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

  document.body.onkeydown = onKeyDown;
  document.body.onkeyup = onKeyUp;

  return (
    <div className="App">
      <header className="App-header">
        Drusic - {keysPressed}
        <br/>
        <div className="ButtonPanel">
          <AudioPlayer />
          <Button onClick={() => save()}>
            Save
          </Button>

          <input type="file" onChange={load}/>
        </div>
        <NotePanel
          keysPressed={keysPressed}
          notes={notes}
          setNotes={setNotes}
        />
      </header>
    </div>
  );
}

export default App;
