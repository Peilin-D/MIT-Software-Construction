package abc.sound;

import java.util.List;

public class Tuplet implements Music {

	private final List<NoteElement> vecNoteElems;
	private final double duration;
	private final double ratio;
	
	public Tuplet(int spec, List<NoteElement> vecNoteElems) {
		this.vecNoteElems = vecNoteElems;
		if (spec == 3) { // Triplet
			ratio = 2.0 / 3.0;
		} else if (spec == 4) { // Quadruplet
			ratio = 3.0 / 4.0;
		} else if (spec == 2) { // Duplet
			ratio = 3.0 / 2.0;
		} else {
			ratio = 1.0;
		}
		double lastDuration = 0;
		for (NoteElement ne : vecNoteElems) {
			ne.changeDuration(ne.duration() * ratio);
			lastDuration += ne.duration();
		}
		duration = lastDuration;
	}
	
	@Override
	public double duration() {
		return duration;
	}

	@Override
	public void play(SequencePlayer player, double atBeat) {
		double lastDuration = 0;
		for (NoteElement ne : vecNoteElems) {
			ne.play(player, atBeat + lastDuration);
			lastDuration += ne.duration();
		}
	}
	
	@Override
	public String toString() {
		String s = "(";
		for (NoteElement elem : vecNoteElems) {
			s += elem.toString() + " ";
		}
		s += duration + ")";
		return s;
	}
	
}
