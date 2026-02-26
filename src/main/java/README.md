Project Implementation Overview
General Architecture

First of all, I created an Entity base class that serves as the parent class for all entities. This class contains the common fields: mass, type, and name.

From this base class, I derived the following subclasses:

Soil

Air

Animal

The Animal class has two subclasses:

MeatEater

Vegetarian

From MeatEater, I further extended:

Parasite

Carnivore

Each subclass implements its own feeding behavior. For example:

MeatEater implements the algorithm for feeding on animals.

Vegetarian implements a different feeding algorithm, since it does not consume animals.

I did not create a class hierarchy for Plant and Water because it was not necessary. Although plants may differ in type, they do not have different behaviors or special methods — only different field values. The abstract classes ensure that subclasses are required to implement their specific behavior where needed.

Map Structure

The Map class represents the environment as a matrix.

Each position in the matrix contains a MapCell, which is implemented as an inner class of Map, since a cell cannot exist without the map.

Each MapCell:

Has reserved slots for each type of entity

Contains an entitiesCount field to track how many entities are present in that cell

Interaction Between Entities

For each type of entity that can be scanned (Animal, Plant, Water), I implemented an Updatable interface that defines a method responsible for updating map interactions.

At every timestamp (handled through a switch structure inside the Robot class), entity interactions are executed.

In the chargeRobot operation, a loop is started that repeatedly calls the update method to simulate interactions during charging.

The Map class is responsible for:

Searching for scanned objects

Calling their specific interaction method

All interactions with the map occur during each iteration of the main switch structure inside the Robot class.

Logic Hierarchy

The hierarchy of logic is structured as follows:

Robot → Map → MapCell → Entities

When starting a new simulation:

The map is replaced

The robot's fields are reset

The Robot class also includes a method that returns a String describing the most frequent events, such as isCharging and StartedSimulation.

Explanation of Each Requirement
1. Start Simulation

Inside the Robot class, I implemented a boolean flag that verifies whether the simulation has started.

In main, I also use:

A counter to determine whether multiple simulations are required

A secondary map reference

An energy level tracker

Errors are handled based on whether the simulation has started or not.

2. End Simulation

This behaves similarly to start simulation but sets the simulation flag to false.

3. PrintMap

This method:

Prints the number of entities using the entity counter

Displays the quality of each cell

4. PrintEnvConditions

This method:

Retrieves the cell in which the robot is currently located

Prints all the entities and conditions inside that cell

5. Move Robot

The robot moves based on probability.

This logic is implemented inside the Map class, which:

Checks neighboring cells

Selects the one with the best probability

If the robot has sufficient energy, it moves.

One major difficulty was related to the ambiguous requirement phrased as “if it exists.” This term was used in multiple places and did not clearly specify whether it referred to scanned entities or all entities in general. This caused confusion, especially during the implementation of moveAnimal.

6. Scan Object

When objects are scanned:

An isScanned flag is set

The object is added to:

The robot’s inventory (ArrayList)

A database inventory (also implemented as an ArrayList)

After scanning, the interaction update method is called to maintain consistency.

7. Scan Robot

The robot determines what type of object it detects based on its fields (e.g., whether attributes such as smell, color, etc., are None or not).

8. Charge Robot

This:

Increases battery level

Executes entity interactions during the charging period

9. LearnFact

This simply adds new information to storage. The functionality is straightforward and directly reflects the requirement.

10. Improve Environment

This method:

Checks the robot’s inventory

Increases specific environmental fields depending on the type of improvement

Removes the used item from the inventory afterward

Feeding Behavior Refactoring

One major design improvement involved the animal feeding logic.

Initially, I checked the animal type manually and implemented conditional logic depending on whether the animal was herbivorous or carnivorous.

I refactored this using proper OOP principles:

Created Herbivore and MeatEater classes

Each implements its own feeding algorithm

Now, when animal.feed() is called, the correct method is selected at runtime through polymorphism, without requiring additional type checks.

Entity Parsing

To avoid overloading the main method with input handling and parsing logic, I created a dedicated EntityParser class responsible for reading and parsing input data.
