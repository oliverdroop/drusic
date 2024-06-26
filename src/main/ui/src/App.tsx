import './App.css';
import NotePanel, { Note } from './components/NotePanel';
import AudioPlayer from './components/AudioPlayer';
import { useState } from 'react';
import LoadButton from './components/LoadButton';
import SaveButton from './components/SaveButton';
import TimeSnapSlider from './components/TimeSnapSlider';
import InstrumentSelector from './components/InstrumentSelector';
import ZoomXSlider from './components/ZoomXSlider';
import BpmController from './components/BpmController';

export interface Track {
  notes: Note[]
}

function App() {
  const [keysPressed, setKeysPressed] = useState([] as string[]);
  const [notes, setNotes] = useState([] as Note[]);
  const [timeSnap, setTimeSnap] = useState(0.5);
  const [instrument, setInstrument] = useState("SQUARE_TO_SINE");
  const [zoomX, setZoomX] = useState(40);
  const [bpm, setBpm] = useState(106);

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

  document.body.onkeydown = onKeyDown;
  document.body.onkeyup = onKeyUp;

  return (
    <div className="App">
      <header className="App-header">
        Drusic - {keysPressed}
        <br/>
        <div className="ButtonPanel">
          <AudioPlayer bpm={bpm}/>
          <SaveButton notes={notes} />
          <LoadButton setNotes={setNotes} />
          <TimeSnapSlider timeSnap={timeSnap} setTimeSnap={setTimeSnap} />
          <ZoomXSlider zoomX={zoomX} setZoomX={setZoomX} />
          <InstrumentSelector setInstrument={setInstrument} />
          <BpmController bpm={bpm} setBpm={setBpm} />
        </div>
        <NotePanel
          keysPressed={keysPressed}
          notes={notes}
          timeSnap={timeSnap}
          instrument={instrument}
          zoomX={zoomX}
          setNotes={setNotes}
        />
      </header>
    </div>
  );
}

export default App;
