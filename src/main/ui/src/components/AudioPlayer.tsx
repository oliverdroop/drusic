const AudioPlayer = () => {

  const play = () => {
    const audio = new Audio("http://localhost:8080/playAll/" + Date.now());
    audio.volume = 0.1;
    audio.play();
  };

  return (
    <div className="AudioPlayer" onClick={() => play()} >
      {'\u23F5'}
    </div>
  );
}

export default AudioPlayer;
