import RangeSlider from "react-bootstrap-range-slider";

interface ZoomXSliderProps {
  zoomX: number
  setZoomX(zoomX: number): void
}

function ZoomXSlider({zoomX, setZoomX}: ZoomXSliderProps) {

  const onChange = (changeEvent: any) => {
    const newValue = changeEvent.target.value;
    setZoomX(newValue);
  };

  return (
    <div className="ButtonPanel">
      <label className="SmallLabel">Zoom X: {zoomX}</label>
      <RangeSlider
        value={zoomX}
        min={10}
        max={160}
        onChange={changeEvent => onChange(changeEvent)}
        step={10}
        tooltip="off"
      />
    </div>
  );
}

export default ZoomXSlider;