# Evolution

A simple program that illustrates the Evolution Strategy (a subset of the Evolutionary Algorithm) by allowing "cells" to go through the evolutionary process working their way from black to white with a kalediscope of colors in between. Evolutions are done manually by clicking the Evolve button (to allow time to talk about what the program is doing), and takes approximately 60 iterations to reach it's final stages.

The code goes through all steps of the Evolution Strategy, starting with determining the fitness of the cells (described in more detail below), recombining to form new cells, mutating some of the cells, and then repeating the process with the next iteration.

Evolution3 is the most accurate version of the program.

## Determining Fitness

The goal of the program are white cells, the color of each determined via RGB code. Each color (Red, Green, and Blue) is determined by a numerical value, anywhere from 0 to 255. The program determines fitness by evaluating a 2 x 2 square of cells, combining the values of the three colors for each cell, and the cell with the lowest numerical score is "killed off." In the event two or more cells have the same score, the program randomly determines which one dies, always leaving three cells behind.

## Crossover

Once a cell is killed off, a new one is formed using the RGB values of the remaining three cells. In random order each cell is selected to contribute either its red, green or blue value to the new cell. The new cell then replaces the dead one.

## Mutation

The mutation phase is applied to the new cells created in each iteration. The cell has a 40% chance of mutating. Which color mutates is randomly selected (more on this in limitations), and has a chance of changing the color value by up to 40 either positive or negative.

## Limitations

The cells are processed in 2x2 square starting at the top left and working down to the bottom right. This means the corner squares are only processed once, the other edge cells twice, and the interior cells four times per iteration. This increses the potential mutation factor in the middle, rather than distributing it evenly.

Ideally Evolution Strategy begins with a varied population to more accurately reflect the wild, but in my case I started with a uniform population and (in version 3) implemented a slight level of randomness to accommodate that, even though Evolution Strategy is supposed to have deterministic selection. (And it does once the population develops some randomness.)

The reset button frequently has to be clicked twice to fully reset the cell matrix. But it's not consistent, and I don't know why.

## Running the program

The program is written in Java 8, each version contained in it's own folder, and none of them require any special addons to compile. There are three classes contained in each version of the program combined into one file. To compile from the command line and run the program, begin by being in the correct sub folder for the version you want to run (I recommend 3) and enter the following commands:

	javac Evolution3.java
	java Evolution3

