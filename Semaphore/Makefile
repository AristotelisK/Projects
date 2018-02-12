

myprogram: main.o 
	g++ -std=c++11 main.o -o myprogram

main.o: main.cpp
	g++ -std=c++11 -c main.cpp

clean: 
	rm *.o *.log myprogram

