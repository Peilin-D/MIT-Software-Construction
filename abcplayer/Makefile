JFLAGS = -g -d bin
JC = javac

all:	
	 mkdir -p bin	
	$(JC) $(JFLAGS) -cp lib/parserlib.jar src/abc/parser/*.java src/abc/player/*.java src/abc/sound/*.java

clean:
	rm -rf bin/
