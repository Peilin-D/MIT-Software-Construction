package abc.player;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import abc.parser.MusicParser;
import abc.sound.Music;
import abc.sound.SequencePlayer;
import lib6005.parser.UnableToParseException;

/**
 * Main entry point of your application.
 */
public class Main {

    /**
     * Plays the input file using Java MIDI API and displays
     * header information to the standard output stream.
     * 
     * (Your code should not exit the application abnormally using
     * System.exit().)
     * 
     * @param file the name of input abc file
     * @throws IOException 
     * @throws UnableToParseException 
     * @throws InvalidMidiDataException 
     * @throws MidiUnavailableException 
     */
    public static void play(String file) throws UnableToParseException, IOException, MidiUnavailableException, InvalidMidiDataException {
    	MusicParser parser = new MusicParser();
		SequencePlayer player = new SequencePlayer();
		Music music = parser.parse(new File(file));
		System.out.println(music);
		music.play(player, 0.125);
		player.play();
    }

    public static void main(String[] args) throws UnableToParseException, IOException, MidiUnavailableException, InvalidMidiDataException {
    	String musicFile = "sample_abc/abc_song.abc";
    	if (args.length > 0) {
    		musicFile = args[0];
    	}
    	play(musicFile);
    }
}
