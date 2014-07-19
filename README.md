# Lazy TA - A Better Way To Mark #

I love helping students. I like giving them feedback and spending one on one time
with them. Do you know what I don't like? Finding complex mechanisms to simplify
marking 70 assignments where the comments get repetitious and bogged down. Toss 
in a little sleep loss and a terrible system to upload grades and you've got a recipe
for messing up. 

In comes Lazy TA! Currently, it only supports one simple format for rubrics, but
future plans are to include a configuration file to let you fully customize the
parsing to suit your needs. 

## What is it? ##

Lazy TA takes in a rubric as a text file and reads through it, pulling out all
relevant information. That relevant information comes in three forms:

* **Grade Items**: Individual things students get marked for.
* **Grade Sections or Categories**: Specific categorized sections which grade items fall under.
* **Problems**: Problems are the overarching collection of categories and items.

## How do I use it? ##

For basic assignments, all you need to do is drop it into your folder with the rubric,
compile if you haven't already (`javac Tester.java`) and run it (`java Tester`).

For a custom filename to output the report to, type it in as command line arguments.
For example, to output to `epicRawrSauce.out` I would run the command `java Tester epicRawrSauce.out`.

It's easy.

If you're feeling ambitious, there's also the groundwork for automated marking. 
It's currently a long ways off of being very viable, but you can extend the class
and overwrite the "automation" method. Check out the comments for more details on
that. It's my end goal to allow you to easy use custom Test objects which will run
automated marking and report it properly with ease. 

## How flexible is it? ##

At the moment, not super flexible. I tried to make the built in parsing as 
general as I could, but it was built hastily for one specific course. 

## Roadmap ##

* Make a *Test* object that makes automated testing easy
* Set up configuration files to make parsing the rubrics very flexible and simple
  to work with
* Set up a way to have Test objects read in from a text file, allowing for automated
  testing in any language that supports file i/o
  
## Who worked on this? ##

Me! My name is Connor Hillen and I wrote this because I am a teaching assistant 
at Carleton University helping to TA computer science. It was just a quick one day 
project and might develop off and on. I hope some people get use out of it and it 
gives you more time to help out and destress!