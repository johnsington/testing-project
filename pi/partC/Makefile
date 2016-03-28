all: clean
	@echo "Compiling..."

	mkdir classes

	javac -d classes/ @sources

clean:
	@echo "Cleaning..."
	if [ -a classes ] ; \
	then \
	     rm -rf classes ; \
	fi;
