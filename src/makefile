all: clean
	@echo "Compiling..."

	mkdir out

	javac -d out @sources

clean:
	@echo "Cleaning..."
	if [ -a out ] ; \
	then \
	     rm -rf out ; \
	fi;