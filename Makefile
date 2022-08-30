
rootdir = .

CLASSPATH = $(rootdir)/src
BINDIR = $(rootdir)/bin



sourcelist = $(shell find $(rootdir) -name '*.java' | sed "s,[.]/,,g")


docdir = ./docs 

default: all 

all:

	@javac -d bin $(sourcelist)


	@javadoc -d $(docdir) -linksource $(sourcelist)


clean:
	@if [ -d $(docdir) ]; then rm -r $(docdir); fi;

        # Remove all the class files in the classpath
	@-find $(rootdir) \( -name "*~" -o -name "*.class" -o -name "runTests" \) -exec rm '{}' \; 

