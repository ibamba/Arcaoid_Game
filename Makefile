JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Ball.java \
	Brique.java \
	Raquette.java \
	Terrain.java \
	TerrainValues.java \
	LectureNiveau.java \
	Controler.java \
	Vue.java \
	Modele.java 


default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class *~
