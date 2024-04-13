import { Form } from "react-bootstrap";

interface BpmControllerProps{
  bpm: number;
  setBpm(bpm: number): void;
}

const BpmController = ({bpm, setBpm}: BpmControllerProps) => {
  const regex = new RegExp('^[0-9]{1,3}$');

  const onChangeBpm = (changeEvent: any) => {
    const newBpmString = changeEvent.target.value;
    if (regex.test(newBpmString)) {
      const newBpmNumber: number = Number.parseFloat(newBpmString);
      console.log(newBpmNumber);
      setBpm(newBpmNumber);
    }
  };

  return (
    <Form className="BpmController">
      <Form.Control className="BpmController" type="text" value={bpm.toString()} onChange={changeEvent => onChangeBpm(changeEvent)} />
      <Form.Label>bpm</Form.Label>
    </Form>
  );
}

export default BpmController;
