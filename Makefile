SRC = $(wildcard code/cookiq/**/*.java)
BIN = code

run:
	javac $(SRC)
	java -cp $(BIN) cookiq.CookIQ

clean:
	find $(BIN) -name "*.class" -delete
