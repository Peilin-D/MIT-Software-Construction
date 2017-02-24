package abc.sound;

import java.util.List;

public class Chord implements NoteElement {
	private final List<Note> vecNotes;
	private double duration;
	
	public Chord(List<Note> notes) {
		vecNotes = notes;
		duration = notes.get(0).duration();
	} 
	
	@Override
	public double duration() {
		return duration;
	}
	
	@Override
	public void play(SequencePlayer player, double atBeat) {
		for (Note note : vecNotes) {
			player.addNote(note.instrument(), note.pitch(), atBeat, note.duration());
		}
	}
	
	@Override
	public void changeDuration(double d) {
		duration = d;
	}
	
	@Override
	public String toString() {
		String s = "[";
		for (Note n : vecNotes) {
			s += n.toString() + " ";
		}
		s += duration + "]";
		return s;
	}
}
