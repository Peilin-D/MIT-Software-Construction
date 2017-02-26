### MIDI Music Player
Here's my implementation of a music player, built on the skeleton code provided by the staff of MIT 6.005.

This player implements a subset of the standard [ABC](https://en.wikipedia.org/wiki/ABC_notation) grammar. 
The sub-grammar is in [Abc.g](https://github.com/Peilin-D/MIT-Software-Construction/blob/master/abcplayer/src/abc/parser/Abc.g).
The full standard is [here](http://abcnotation.com/wiki/abc:standard:v2.1).

The player implements Chord, Tuplet, and Repeat, but it doesn't support multiple voices for now. 
There're some sample songs in the sample_abc folder. To use the player, download the file and run `make`, and 
`java -cp bin/:lib/parserlib.jar abc.player.Main [music file]`.

