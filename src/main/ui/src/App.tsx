import './App.css';
import NoteCollection from './components/NoteCollection';
import AudioPlayer from './components/AudioPlayer';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        Drusic
        <br/>
        <AudioPlayer />
        <NoteCollection />
      </header>
    </div>
  );
}

export default App;
