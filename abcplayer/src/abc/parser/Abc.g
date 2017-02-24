abc_tune ::= abc_header abc_music;
@skip WHITESPACE {
	abc_header ::= field_number comment* field_title other_fields* field_key;
	
	field_number ::= "X:" NUMBER end_of_line;
	field_title ::= "T:" text end_of_line;
	other_fields ::= field_composer | field_default_length | field_meter | field_tempo | field_voice | comment;
	field_composer ::= "C:" text end_of_line;
	field_default_length ::= "L:" note_length_strict end_of_line;
	field_meter ::= "M:" meter end_of_line;
	field_tempo ::= "Q:" tempo end_of_line;
	field_voice ::= "V:" text end_of_line;
	field_key ::= "K:" key end_of_line;
	
	key ::= keynote mode_minor?;
	keynote ::= basenote key_accidental?;
	key_accidental ::= "#" | "b";
	mode_minor ::= "m";
	
	meter ::= "C" | "C|" | meter_fraction;
	meter_fraction ::= NUMERATOR "/" DENOMINATOR;
	
	tempo ::= meter_fraction "=" NUMBER;
}

abc_music ::= abc_line+;
abc_line ::= element* NEWLINE | mid_tune_field | comment;
element ::= note_element | tuplet_element | barline | nth_repeat | WHITESPACE;

note_element ::= note | multi_note | rest;

note ::= pitch note_length?;
pitch ::= accidental? basenote octave?;
octave ::= "'"+ | ","+;
note_length ::= NUMERATOR? ("/" DENOMINATOR?)?;
note_length_strict ::= NUMERATOR "/" DENOMINATOR;

accidental ::= "^" | "^^" | "_" | "__" | "=";

basenote ::= "C" | "D" | "E" | "F" | "G" | "A" | "B"
        | "c" | "d" | "e" | "f" | "g" | "a" | "b";

rest ::= "z" note_length?;

tuplet_element ::= tuplet_spec note_element+;
tuplet_spec ::= "(" NUMBER;

multi_note ::= "[" note+ "]";

barline ::= "|" | "||" | "[|" | "|]" | ":|" | "|:";
nth_repeat ::= "[1" | "[2";

mid_tune_field ::= field_voice;

comment ::= "%" text NEWLINE;
end_of_line ::= comment | NEWLINE;

NUMERATOR ::= [0-9]+;
DENOMINATOR ::= [0-9]+;
NUMBER ::= [0-9]+;
NEWLINE ::= "\n" | "\r" "\n"?;
WHITESPACE ::= " " | "\t";
text ::= [a-zA-Z0-9',. ]*;
