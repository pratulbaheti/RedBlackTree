JAVAC=javac
sources = $(wildcard *.java)
classes = $(sources:.java=.class)

all: $(classes)

rm :
	rm -f *.class

%.class : %.java
	$(JAVAC) $<
