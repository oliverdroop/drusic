import { useState } from "react";
import { Dropdown, DropdownButton } from "react-bootstrap";

interface InstrumentSelectorProps {
  setInstrument(instrument: string): void
}

const Instruments = ["SQUARE_TO_SINE", "TRIANGLE_TO_SINE"];

function InstrumentSelector({setInstrument}: InstrumentSelectorProps) {
  const [showDropdownItems, setShowDropdownItems] = useState(false);

  const onSelect = (eventKey: string | null) => {
    if (eventKey) {
      setInstrument(eventKey);
    }
    setShowDropdownItems(false);
  };

  return (
    <DropdownButton
      className="Dropdown"
      title="Instrument"
      onSelect={(eventKey) => onSelect(eventKey)}
      onClick={() => setShowDropdownItems(!showDropdownItems)}
    >
      {showDropdownItems && Instruments.map(instrument => (
        <Dropdown.Item
          className="DropdownItem"
          key={`instrument_selector_${instrument}`}
          eventKey={instrument}>{instrument}
        </Dropdown.Item>
      ))}
    </DropdownButton>
  );
}

export default InstrumentSelector;
