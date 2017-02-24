package abc.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import lib6005.parser.*;
import abc.sound.*;

public class MusicParser {
	private enum MusicGrammar {
		// music head
		abc_tune, abc_header, abc_music,
		field_number, comment, field_title, other_fields, field_key, meter,
		field_composer, field_default_length, field_meter, field_tempo, field_voice,
		key, keynote, mode_minor, key_accidental, tempo, meter_fraction, 
		// music piece
		abc_line, element, note_element, tuplet_element, barline, nth_repeat,
		note, multi_note, pitch, rest, basenote, octave, note_length, note_length_strict,
		tuplet_spec, mid_tune_field, accidental,
		WHITESPACE, NEWLINE, NUMBER, TEXT, END_OF_LINE, NUMERATOR, DENOMINATOR
	};
	private Parser<MusicGrammar> parser;
	private double defaultDuration = 0.125;
	private double meter = 0;
	private int tempo = 100;
	private String sharpNotes = "";
	private String flatNotes = "";
	private double beatLen;
	private Instrument DEFAULT_INSTRUMENT = Instrument.GUITAR_HARMONICS;
	// Repeat
	boolean firstRepeat = false, secondRepeat = false;
	private List<Music> notesToRepeat = new ArrayList<>();
	private List<Music> notesFirstRepeat = new ArrayList<>();
	private List<Music> notesSecondRepeat = new ArrayList<>();
	private Music music = new Rest(0);
	
	
	public MusicParser() throws UnableToParseException, IOException {
		parser = GrammarCompiler.compile(new File("src/abc/parser/Abc.g"), MusicGrammar.abc_tune);
	}
	
	public Music parse(File musicFile) throws IOException, UnableToParseException {
		ParseTree<MusicGrammar> tree = parser.parse(musicFile);
		parseHead(tree.childrenByName(MusicGrammar.abc_header).get(0));
		parseBody(tree.childrenByName(MusicGrammar.abc_music).get(0));
		for (Music elem : notesToRepeat) {
			music = new Concat(music, elem);
		}
		return music;
	}
	
	private void parseHead(ParseTree<MusicGrammar> node) {
		switch (node.getName()) {
		case abc_header:
			for (ParseTree<MusicGrammar> child : node.children()) {
				parseHead(child);
			}
			break;
		case other_fields:
			parseHead(node.children().get(0));
			break;
		case field_default_length:
			// Parse "L"
			defaultDuration = parseNoteLength(node.childrenByName(MusicGrammar.note_length_strict).get(0));
			// initialize beatLen
			beatLen = defaultDuration;
			break;
		case field_meter:
			// Parse "M"
			ParseTree<MusicGrammar> mt = node.childrenByName(MusicGrammar.meter).get(0);
			if (mt.isTerminal()) {
				meter = 1.0;
			} else {
				meter = parseNoteLength(mt.childrenByName(MusicGrammar.meter_fraction).get(0));
			}
			if (defaultDuration == 0) {
				if (meter < 0.75) {
					defaultDuration = 0.0625;
				} else {
					defaultDuration = 0.125;
				}
			}
			// initialize beatLen
			beatLen = defaultDuration;
			break;
		case field_tempo:
			// Parse tempo
			ParseTree<MusicGrammar> tp = node.childrenByName(MusicGrammar.tempo).get(0);
			tempo = Integer.parseInt(tp.childrenByName(MusicGrammar.NUMBER).get(0).getContents());
			beatLen = parseNoteLength(tp.childrenByName(MusicGrammar.meter_fraction).get(0));
			break;
		case field_key:
			// Parse key signature
			ParseTree<MusicGrammar> k = node.childrenByName(MusicGrammar.key).get(0);
			ParseTree<MusicGrammar> kn = k.childrenByName(MusicGrammar.keynote).get(0);
			String keynote = kn.childrenByName(MusicGrammar.basenote).get(0).getContents();
			if (!kn.childrenByName(MusicGrammar.key_accidental).isEmpty()) {
				String key_acc = kn.childrenByName(MusicGrammar.key_accidental).get(0).getContents();
				keynote += key_acc;
			}
			if (!k.childrenByName(MusicGrammar.mode_minor).isEmpty()) {
				keynote += "m";
			}
			switch(keynote) {
				case "G": 
				case "Em":
					sharpNotes = "F";
					break;
				case "D":
				case "Bm":
					sharpNotes = "FC";
					break;
				case "A":
				case "F#m":
					sharpNotes = "FCG";
					break;
				case "E":
				case "C#m":
					sharpNotes = "FCGD";
					break;
				case "B":
				case "G#m":
					sharpNotes = "FCGDA";
					break;
				case "F#":
				case "D#m":
					sharpNotes = "FCGDAE";
					break;
				case "C#":
				case "A#m":
					sharpNotes = "FCGDAEB";
					break;
				case "F":
				case "Dm":
					flatNotes = "B";
					break;
				case "Bb":
				case "Gm":
					flatNotes = "BE";
					break;
				case "Eb":
				case "Cm":
					flatNotes = "BEA";
					break;
				case "Ab":
				case "Fm":
					flatNotes = "BEAD";
					break;
				case "Db":
				case "Bbm":
					flatNotes = "BEADG";
					break;
				case "Gb":
				case "Ebm":
					flatNotes = "BEADGC";
					break;
				case "Cb":
				case "Abm":
					flatNotes = "BEADGCF";
					break;
				default:
					break;
			}
		default:
			break;
		}
	}
	
	// Parse music body
	private void parseBody(ParseTree<MusicGrammar> node) {
		switch(node.getName()) {
		case abc_music:
			for (ParseTree<MusicGrammar> child : node.children()) {
				parseBody(child);
			}
			break;
		case abc_line:
			for (ParseTree<MusicGrammar> child : node.childrenByName(MusicGrammar.element)) {
				parseBody(child);
			}
			break;
		case element:
			if (!node.childrenByName(MusicGrammar.note_element).isEmpty()) {
				parseBody(node.childrenByName(MusicGrammar.note_element).get(0));
			} else if (!node.childrenByName(MusicGrammar.tuplet_element).isEmpty()) {
				parseBody(node.childrenByName(MusicGrammar.tuplet_element).get(0));
			} else if (!node.childrenByName(MusicGrammar.nth_repeat).isEmpty()) {
				parseBody(node.childrenByName(MusicGrammar.nth_repeat).get(0));
			} else if (!node.childrenByName(MusicGrammar.barline).isEmpty()) {
				parseBody(node.childrenByName(MusicGrammar.barline).get(0));
			}
			break;
		case note_element:
			NoteElement elem = parseNoteElement(node);
			if (firstRepeat) {
				notesFirstRepeat.add(elem);
			} else if (secondRepeat) {
				notesSecondRepeat.add(elem);
			} else {
				notesToRepeat.add(elem);
			}
			break;
		case tuplet_element:
			int numNoteElems = Integer.parseInt(node.childrenByName(MusicGrammar.tuplet_spec).get(0).childrenByName(MusicGrammar.NUMBER).get(0).getContents());
			List<NoteElement> vecNoteElems = new ArrayList<>();
			for (int i = 0; i < numNoteElems; i++) {
				ParseTree<MusicGrammar> noteElem = node.childrenByName(MusicGrammar.note_element).get(i);
				vecNoteElems.add(parseNoteElement(noteElem));
			}
			Tuplet tuplet = new Tuplet(numNoteElems, vecNoteElems);
			if (firstRepeat) {
				notesFirstRepeat.add(tuplet);
			} else if (secondRepeat) {
				notesSecondRepeat.add(tuplet);
			} else {
				notesToRepeat.add(tuplet);
			}
			break;
		case nth_repeat:
			switch(node.getContents()) {
			case "[1":
				firstRepeat = true;
				break;
			case "[2":
				secondRepeat = true;
				break;
			}
			break;
		case barline:
			switch(node.getContents()) {
			case "[|":
			case "|]":
			case "||":
			case "|:":
				for (Music ne : notesToRepeat) {
					music = new Concat(music, ne);
				}
				if (secondRepeat) {
					for (Music ne : notesSecondRepeat) {
						music = new Concat(music, ne);
					}
					secondRepeat = false;
					notesSecondRepeat.clear();
				}
				notesToRepeat.clear();
				break;
			case "|":
				if (secondRepeat) {
					for (Music ne : notesToRepeat) {
						music = new Concat(music, ne);
					}
					for (Music ne : notesSecondRepeat) {
						music = new Concat(music, ne);
					}
					secondRepeat = false;
					notesToRepeat.clear();
					notesSecondRepeat.clear();
				}
				break;
			case ":|":
				for (Music ne : notesToRepeat) {
					music = new Concat(music,ne);
				}
				if (firstRepeat) {
					for (Music ne : notesFirstRepeat) {
						music = new Concat(music, ne);
					}
					firstRepeat = false;
					notesFirstRepeat.clear();
				}
				break;
			}
			break;
		default:
			break;
		}
	}
	
	private NoteElement parseNoteElement(ParseTree<MusicGrammar> node) {
		if (!node.childrenByName(MusicGrammar.note).isEmpty()) {
			return parseNote(node.childrenByName(MusicGrammar.note).get(0));
		} else if (!node.childrenByName(MusicGrammar.multi_note).isEmpty()) {
			return parseChord(node.childrenByName(MusicGrammar.multi_note).get(0));
		} else {
			return parseRest(node.childrenByName(MusicGrammar.rest).get(0));
		}
	}
	
	private Note parseNote(ParseTree<MusicGrammar> node) {
		double scale = 1.0;
		if (!node.childrenByName(MusicGrammar.note_length).isEmpty()) {
			scale = parseNoteLength(node.childrenByName(MusicGrammar.note_length).get(0));
		}
		Pitch pitch = parsePitch(node.childrenByName(MusicGrammar.pitch).get(0));
		Note note = new Note(scale * defaultDuration, pitch, DEFAULT_INSTRUMENT);
		return note;
	}
	
	private Rest parseRest(ParseTree<MusicGrammar> node) {
		double scale = 1.0;
		if (!node.childrenByName(MusicGrammar.note_length).isEmpty()) {
			scale = parseNoteLength(node.childrenByName(MusicGrammar.note_length).get(0));
		}
		return new Rest(defaultDuration * scale);
	}
	
	private Chord parseChord(ParseTree<MusicGrammar> node) {
		List<Note> notes = new ArrayList<>();
		for (ParseTree<MusicGrammar> note : node.childrenByName(MusicGrammar.note)) {
			notes.add(parseNote(note));
		}
		return new Chord(notes);
	}
	
	private double parseNoteLength(ParseTree<MusicGrammar> node) {
		double numerator = 1.0, denominator = 1.0;
		if (node.childrenByName(MusicGrammar.NUMERATOR).isEmpty() && node.childrenByName(MusicGrammar.DENOMINATOR).isEmpty()) {
			return 1.0;
		} 
		if (!node.childrenByName(MusicGrammar.NUMERATOR).isEmpty()) {
			numerator = Double.parseDouble(node.childrenByName(MusicGrammar.NUMERATOR).get(0).getContents());
		} 
		if (node.getContents().contains("/")) {
			if (!node.childrenByName(MusicGrammar.DENOMINATOR).isEmpty()) {
				denominator = Double.parseDouble(node.childrenByName(MusicGrammar.DENOMINATOR).get(0).getContents());
			} else {
				denominator = 2.0;
			}
		}
		return numerator / denominator;
	}
	
	private Pitch parsePitch(ParseTree<MusicGrammar> node) {
		String basenote = node.childrenByName(MusicGrammar.basenote).get(0).getContents();
		Pitch pitch = new Pitch(basenote.toUpperCase().charAt(0));
		if (Character.isLowerCase(basenote.charAt(0))) { 
			pitch = pitch.transpose(12);
		}
		Pitch originalPitch = pitch;
		if (sharpNotes.contains(basenote)) {
			pitch = pitch.transpose(1);
		}
		if (flatNotes.contains(basenote)) {
			pitch = pitch.transpose(-1);
		}
		if (!node.childrenByName(MusicGrammar.accidental).isEmpty()) {
			switch (node.childrenByName(MusicGrammar.accidental).get(0).getContents()) {
			case "^":
				pitch = pitch.transpose(1);
				break;
			case "^^":
				pitch = pitch.transpose(2);
				break;
			case "_":
				pitch = pitch.transpose(-1);
				break;
			case "__":
				pitch = pitch.transpose(-2);
				break;
			case "=":
				pitch = originalPitch;
				break;
			default:
				break;
			}
		} 
		// octave
		String octave = "";
		for (ParseTree<MusicGrammar> child : node.childrenByName(MusicGrammar.octave)) {
			octave += child.getContents();
		}
		if (octave.startsWith("'")) {
			pitch = pitch.transpose(12 * octave.length());
		} else {
			pitch = pitch.transpose(-12 * octave.length());
		}
		return pitch;
	}
	
	
	public static void main(String[] args) throws UnableToParseException, IOException, MidiUnavailableException, InvalidMidiDataException {
		 MusicParser parser = new MusicParser();
		 SequencePlayer player = new SequencePlayer();
		 Music music = parser.parse(new File("sample_abc/waxies_dargle.abc"));
		 System.out.println(music);
		 music.play(player, 0.125);
		 player.play();
	}
	
}
