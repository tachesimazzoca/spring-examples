all: dist 

.PHONY: dist 
dist:
	@mvn clean package assembly:assembly 

.PHONY: run 
run:
	@mvn compile exec:java

.PHONY: compile
compile:
	@mvn compile

.PHONY: javadoc
javadoc:
	@mvn javadoc:javadoc

.PHONY: checkstyle
checkstyle:
	@mvn checkstyle:checkstyle 

.PHONY: clean
clean:
	@mvn clean
