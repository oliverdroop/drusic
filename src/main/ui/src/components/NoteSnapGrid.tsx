
export interface NoteSnapGridProps {
  getPitchNumber(yPos: number): number
}

const NoteSnapGrid = ({getPitchNumber}: NoteSnapGridProps) => {
  const id = "notePanel";
  const xFactor = 40;
  const yFactor = 10;

  const getIntegers = (max: number) => {
    const numbers = [];
    for(let i = 0; i < max; i++) {
      numbers.push(i);
    }
    return numbers;
  };

  const isWhiteNote = (num: number) => {
    return [1, 3, 4, 6, 8, 9, 11].includes(num % 12);
  };

  const isC = (num: number) => {
    return num % 12 === 4;
  };

  const getHorizontalLinePositions = () => {
    const count = (document.getElementById(id)?.clientHeight ?? 0) / yFactor;
    return getIntegers(count)
      .map(num => num * yFactor);
  };

  const getVerticalLinePositions = () => {
    const count = (document.getElementById(id)?.clientWidth ?? 0) / xFactor;
    return getIntegers(count).map(num => num * xFactor);
  };

  return (
    <div>
      {getHorizontalLinePositions().map((yPos) => 
        <div
          className="hl"
          key={`horizontal_rule_${yPos}`}
          style={{
            top: `${yPos + 1}px`,
            borderTop: `7px solid ${
              isC(getPitchNumber(yPos)) 
                ? "#444444"
                : isWhiteNote(getPitchNumber(yPos)) 
                  ? "#3F3F3F" 
                  : "#3A3A3A"
            }`
          }}/>
      )}
      {getVerticalLinePositions().map((xPos) => 
        <div
          className="vl"
          key={`vertical_rule_${xPos}`}
          style={{
            left: `${xPos - 1}px`,
            borderLeft: `1px solid #444444`
          }}/>
      )}
    </div>
  );
}

export default NoteSnapGrid;
