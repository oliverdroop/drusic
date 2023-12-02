import { useState } from "react";
import RangeSlider from "react-bootstrap-range-slider";

interface TimeSnapSliderProps {
  timeSnap:number
  setTimeSnap(timeSnap: number): void
}

function TimeSnapSlider({timeSnap, setTimeSnap}: TimeSnapSliderProps) {
  const [value, setValue] = useState(2);

  const onChange = (changeEvent: any) => {
    const newValue = changeEvent.target.value;
    const timeSnap = 0.125 * Math.pow(2, newValue);
    setValue(newValue);
    setTimeSnap(timeSnap);
  };

  return (
    <div className="ButtonPanel">
      <label className="SmallLabel">TimeSnap: {timeSnap}</label>
      <RangeSlider
        value={value}
        min={0}
        max={5}
        onChange={changeEvent => onChange(changeEvent)}
        step={1}
        tooltip="off"
      />
    </div>
  );
}

export default TimeSnapSlider;
