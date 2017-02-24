package abc.sound;

public interface Music {
	/**
     * @return total duration of this piece in beats
     */
    double duration();
    
    /**
     * Play this piece.
     * @param player player to play on
     * @param atBeat when to play
     */
    void play(SequencePlayer player, double atBeat);
    
}