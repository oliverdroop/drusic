import { Note } from "../components/NotePanel"

export async function getNotes(): Promise<Note[]> {
  const response = await fetch("http://localhost:8080/notes")
  const response_1 = await response.json();
  return response_1 as Note[];
}

export async function postNotes(notes: Note[]): Promise<Note[]> {
  const response = await fetch("http://localhost:8080/notes", {
    method: "POST",
    body: JSON.stringify(notes),
    // headers: undefined,
  });
  const response_1 = await response.json()
  return response_1 as Note[];
};

export async function postNote(note: Note): Promise<Note[]> {
  const response = await fetch("http://localhost:8080/note", {
    method: "POST",
    body: JSON.stringify(note),
    // headers: undefined,
  });
  const response_1 = await response.json()
  return response_1 as Note[];
};

export async function deleteNotes(notes: Note[]): Promise<Note[]> {
  const response = await fetch("http://localhost:8080/notes", {
    method: "DELETE",
    body: JSON.stringify(notes),
    // headers: undefined,
  });
  const response_1 = await response.json()
  return response_1 as Note[];
};
