package entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import robot.Robot;
import map.Map;
import map.MapCardinalPoints;

@Getter
@Setter
public abstract class Animal extends Entity implements UpdatableInterface {
    @JsonIgnore
    protected double possibilityToAttackRobot;
    @JsonIgnore
    protected String stateOfHunger;
    @JsonIgnore
    protected int timestampAtWhichItWasScanned;
    @JsonIgnore
    protected boolean scannedByRobot;
    @JsonIgnore
    protected boolean feedWithPrey;
    @JsonIgnore
    protected boolean hasDrankWater;
    @JsonIgnore
    protected boolean hasEatenPlant;
    @JsonIgnore
    protected boolean hasEatenPrey;
    @JsonIgnore
    protected boolean processedForThisTimestamp;
    @JsonIgnore
    private final int totalNumberOfPossibleMoves = 4;
    @JsonIgnore
    private final int bestCandidateIndex = 3;
    @JsonIgnore
    private final int secondBestCandidateIndex = 2;
    @JsonIgnore
    private final int thirdBestCandidateIndex = 1;
    @JsonIgnore
    private final int worstCandidateIndex = 0;
    @JsonIgnore
    private final double waterIntakeMultiplier = 0.08;
    @JsonIgnore
    private final double soilImprovementAfterEatingBothWaterAndPlant = 0.8;
    @JsonIgnore
    private final double soilImprovementAfterEatingSomethingElse = 0.5;
    @JsonIgnore
    protected final int detrivoresAttackPossibility = 90;
    @JsonIgnore
    protected final int herbivoresAttackPossibility = 85;
    @JsonIgnore
    protected final int omnivoresAttackPossibility = 60;
    @JsonIgnore
    protected final int carnivoresAttackPossibility = 30;
    @JsonIgnore
    protected final int parasitesAttackPossibility = 10;
    @JsonIgnore
    protected final double animalAttackDivisor = 10.0;
    @JsonIgnore
    protected final int oneHundred = 100;
    @JsonIgnore
    protected final double zeroBoundsCheck = 0;
    @JsonIgnore
    private final double maxScoreMultiplierToxicity = 0.8;
    @JsonIgnore
    protected final int northIndex = 0;
    @JsonIgnore
    protected final int eastIndex = 1;
    @JsonIgnore
    protected final int southIndex = 2;
    @JsonIgnore
    protected final int westIndex = 3;

   /**
    * Method to calculate the possibility to attack the robot
    * */
    public double calculatePossibilityToAttackRobot(final double attackPossibility) {
        return (oneHundred - attackPossibility) / animalAttackDivisor;
    }

    /**
     * This method will be overridden in each subclass of Animal
     * Vegetarian animals will use feedAnimalWithoutPrey method
     * MeatEater animals will use feedAnimalWithPrey method first, then feedAnimalWithoutPrey method
     * If they don't find any animals to eat
     * */
    public abstract void feedAnimal(Map map, int timestamp);

    public Animal() {

    }

    /**
     * Resets animal state after fertilizing soil
     */
    public void resetAnimal() {
        this.hasDrankWater = false;
        this.hasEatenPlant = false;
        this.hasEatenPrey = false;
        this.stateOfHunger = "hungry";
    }

    /**
     * Feeds animal with both water and plant
     * */
    public void feedAnimalWithPlantAndWater(final Map map) {
        this.feedAnimalWithWater(map);
        this.feedAnimalWithPlant(map);
    }

    /**
     *Checks if the animal has become intoxicated, it becomes if the toxicityAQ
     * is greater than a set number
     * */
    public boolean checkIfAnimalHasBecomeIntoxicated(final Map map) {
        Map.MapCell cell = map.getMapCell(this.x, this.y);
        Air air = cell.getAir();
        if (air == null) {
            return false;
        } else {
            return air.getToxicityAQ() >= (maxScoreMultiplierToxicity * air.getMaxScore());
        }
    }

    /**
     * Feeds animal with water, if he drinks all the water,
     * the water is eliminated from the map cell
     * */
    public void feedAnimalWithWater(final Map map) {
        Map.MapCell currentCell = map.getMapCell(this.x, this.y);
        Water water = currentCell.getWater();
        Animal animal = currentCell.getAnimal();
        double waterMass = water.getMass();
        double animalMass = animal.getMass();
        double waterToDrink = Math.min(animalMass * this.waterIntakeMultiplier, waterMass);
        currentCell.getWater().setMass(currentCell.getWater().getMass() - waterToDrink);
        currentCell.getAnimal().setMass(waterToDrink + currentCell.getAnimal().getMass());
        if (currentCell.getWater().getMass() <= 0.0) {
            currentCell.setWater(null);
        }
        currentCell.getAnimal().setStateOfHunger("well-fed");
        currentCell.getAnimal().setHasDrankWater(true);
    }

    /**
     * Feeds animal with plant, after eating the plant, it is eliminated from the map cell
     * */
    public void feedAnimalWithPlant(final Map map) {
        Map.MapCell currentCell = map.getMapCell(this.x, this.y);
        double plantMass = currentCell.getPlant().getMass();
        currentCell.getAnimal().setMass(plantMass + currentCell.getAnimal().getMass());
        currentCell.setPlant(null);
        currentCell.getAnimal().setStateOfHunger("well-fed");
        currentCell.getAnimal().setHasEatenPlant(true);
    }

    public Animal(final String name, final double mass, final String type) {
        super(name, mass, type);
        this.stateOfHunger = "hungry";
    }

    private int findBestWaterQualityIndex(final Map.MapCell[] adjacentCells,
                                          final int[] bestCandidate,
                                          final int candidateIndex) {
        double bestWaterQuality = -1.0;
        int bestIndex = -1;

        for (int i = 0; i < bestCandidate.length; i++) {
            if (bestCandidate[i] == candidateIndex) {
                double currentWaterQuality = adjacentCells[i].getWater().getWaterQuality();
                if (currentWaterQuality > bestWaterQuality) {
                    bestWaterQuality = currentWaterQuality;
                    bestIndex = i;
                }
            }
        }

        return bestIndex;
    }


    /**
     * Method to check if the animal is in bounds
     * Method that gets used when the animal needs to move to eat
     * The order is up right down left
     * */
    public boolean checkIfInBounds(final Map map,
                                   final int xToMoveAnimal,
                                   final int yToMoveAnimal) {
        return xToMoveAnimal >= zeroBoundsCheck && xToMoveAnimal < map.getRowLength()
                &&
                yToMoveAnimal >= zeroBoundsCheck && yToMoveAnimal < map.getColumnLength();
    }


    /**
     * Method that moves the animal to the best position
     * it gets the oldCell and then sets the animal to null in the old Cell
     * then it gets the newCell and sets the animal to the new Cell
     * The @param bestMove is the direction in which the animal will move
     * The bestMove param is determined by the bestMoveForAnimal method
     * Will be explained in bestMoveForAnimal method
     * */
    public void moveAnimal(final Map map, final String bestMove) {
        Map.MapCell oldCell = map.getMapCell(this.x, this.y);
        switch (bestMove) {
            case "NORTH" -> {
                this.y += 1;
            }
            case "EAST" -> {
                this.x += 1;
            }
            case "SOUTH" -> {
                this.y -= 1;
            }
            case "WEST" -> {
                this.x -= 1;
            }
            default -> {
                //does nothing, the animal shouldn't get stuck !!
            }
        }
        Map.MapCell newCell = map.getMapCell(this.x, this.y);
        newCell.setAnimal(this);
        oldCell.setAnimal(null);
    }

    /**
     * feedAnimalWithoutPrey method will feed the animal with plants and water
     * if both are available, it will eat both
     * if not, the animal will eat the plant if it is scanned and if not it will drink water
     * After the plant is eaten, it is set to null and if all the water is drunk,
     * it is set to null
     * */
    public void feedAnimalWithoutPrey(final Map map) {
        Map.MapCell currentCell = map.getMapCell(this.x, this.y);
        Plant plant = currentCell.getPlant();
        Water water = currentCell.getWater();
        this.feedWithPrey = false;
        if (plant != null && plant.isScannedByRobot()
                &&
                water != null && water.isScannedByRobot()) {
            this.feedAnimalWithPlantAndWater(map);
        } else if (plant != null && plant.isScannedByRobot()) {
            this.feedAnimalWithPlant(map);
        } else if (water != null && water.isScannedByRobot()) {
            this.feedAnimalWithWater(map);
        } else {
            this.setStateOfHunger("hungry");
        }
    }

    /**
     * will use codifications to store the best candidates for the animal to move,
     * 3 - cell has both water and plant, will need comparison to get the best quality for water
     * 2 - cell has only plant, the animal moves either way, doesn't care about the water
     * 1 - cell has only water, the animal moves to the best water quality cell
     * 0 - cell has neither, the animal moves to the ordinary cell, up right down left
     * */
    public String bestMoveForAnimal(final Map map) {
        Map.MapCell[] adjacentCells = new Map.MapCell[totalNumberOfPossibleMoves];

        for (MapCardinalPoints mapCoordinates : MapCardinalPoints.values()) {
            if (mapCoordinates == MapCardinalPoints.NORTH) {
                if (checkIfInBounds(map, this.x, this.y + 1)) {
                    adjacentCells[northIndex] = map.getMapCell(this.x, this.y + 1);
                } else {
                    adjacentCells[northIndex] = null;
                }
            } else if (mapCoordinates == MapCardinalPoints.EAST) {
                if (checkIfInBounds(map, this.x + 1, this.y)) {
                    adjacentCells[eastIndex] = map.getMapCell(this.x + 1, this.y);
                } else {
                    adjacentCells[eastIndex] = null;
                }
            } else if (mapCoordinates == MapCardinalPoints.SOUTH) {
                if (checkIfInBounds(map, this.x, this.y - 1)) {
                    adjacentCells[southIndex] = map.getMapCell(this.x, this.y - 1);
                } else {
                    adjacentCells[southIndex] = null;
                }
            } else if (mapCoordinates == MapCardinalPoints.WEST) {
                if (checkIfInBounds(map, this.x - 1, this.y)) {
                    adjacentCells[westIndex] = map.getMapCell(this.x - 1, this.y);
                } else {
                    adjacentCells[westIndex] = null;
                }
            }
        }

        int[] bestCandidate = new int[totalNumberOfPossibleMoves];

        for (int i = 0; i < adjacentCells.length; i++) {
            Map.MapCell adjacentCell = adjacentCells[i];
            Water water = null;
            Plant plant = null;
            if (adjacentCell != null) {
                water = adjacentCell.getWater();
            }
            if (adjacentCell != null) {
                plant = adjacentCell.getPlant();
            }

             // Sets the best index based on the presence of water and plant
            if (adjacentCell != null && water != null && water.isScannedByRobot()
                    &&
                    plant != null && plant.isScannedByRobot()) {
                bestCandidate[i] = bestCandidateIndex;
            } else if (adjacentCell != null && plant != null && plant.isScannedByRobot()) {
                bestCandidate[i] = secondBestCandidateIndex;
            } else if (adjacentCell != null && water != null && water.isScannedByRobot()) {
                bestCandidate[i] = thirdBestCandidateIndex;
            } else if (adjacentCell != null) {
                bestCandidate[i] = worstCandidateIndex;
            } else {
                bestCandidate[i] = -1;
            }
        }

        int bestPriority = -1;
        int bestIndex = -1;

        for (int priority : bestCandidate) {
            if (priority > bestPriority) {
                bestPriority = priority;
            }
        }

        // finds the best cell for the animal to move to, based on the bestIndex
        //if there are more than one candidate with water, the method
        //findBestWaterQualityIndex solves the issue
        if (bestPriority == bestCandidateIndex) {
            bestIndex = findBestWaterQualityIndex(adjacentCells,
                    bestCandidate,
                    bestCandidateIndex);
        } else if (bestPriority == secondBestCandidateIndex) {
            for (int i = 0; i < bestCandidate.length; i++) {
                if (bestCandidate[i] == secondBestCandidateIndex) {
                    bestIndex = i;
                    break;
                }
            }
        } else if (bestPriority == thirdBestCandidateIndex) {
            bestIndex = findBestWaterQualityIndex(adjacentCells,
                    bestCandidate,
                    thirdBestCandidateIndex);
        } else {
            for (int i = 0; i < bestCandidate.length; i++) {
                if (bestCandidate[i] == 0) {
                    bestIndex = i;
                    break;
                }
            }
        }

        if (bestIndex != -1) {
            if (bestIndex == 0) {
                return "NORTH";
            } else if (bestIndex == 1) {
                return "EAST";
            } else if (bestIndex == 2) {
                return "SOUTH";
            } else {
                return "WEST";
            }
        }

        return "WILL REMAIN HUNGRY";
    }

    /**
     * Only for carnivores and parasites, the animal will eat the animal before moving
     * After moving, the prey is set to null in the cell that the animal will move
     * */
    public void feedAnimalWithPrey(final Map map) {
        String bestPositionForCell = bestMoveForAnimal(map);
        Animal animalPrey = null;
        Map.MapCell animalPreyCell;

        switch (bestPositionForCell) {
            case "NORTH" -> animalPrey = map.getMapCell(this.x, this.y + 1).getAnimal();
            case "EAST" -> animalPrey = map.getMapCell(this.x + 1, this.y).getAnimal();
            case "SOUTH" -> animalPrey = map.getMapCell(this.x, this.y - 1).getAnimal();
            case "WEST" -> animalPrey = map.getMapCell(this.x - 1, this.y).getAnimal();
            default -> {
                //does nothing, returns null if there is no prey available
            }
        }

        if (animalPrey == null) {
            return;
        }

        animalPreyCell = map.getMapCell(animalPrey.getX(), animalPrey.getY());
        this.setMass(this.getMass() + animalPrey.getMass());
        this.feedWithPrey = true;
        animalPreyCell.setAnimal(null);
    }


    /**
     * if 2 iterations have passed, the animal will move to eat
     * if the animal is either carnivore or parasite, it will first eat the prey and then move
     * The movement is determined by the bestMoveForAnimal method, it does not get influenced by
     * the presence of prey, only by water and plant
     * */
    @Override
    public void updateMapWithScannedObject(final Robot robot,
                                           final Map map,
                                           final Map.MapCell cell,
                                           final int timestamp) {

        if (this.processedForThisTimestamp) {
            return;
        }

        int differentiationBetweenTimestamps = timestamp - this.timestampAtWhichItWasScanned;

        if (this.stateOfHunger.equals("well-fed")) {
            if (this.hasEatenPlant && this.hasDrankWater) {
                if (!this.checkIfAnimalHasBecomeIntoxicated(map)) {
                    Soil soil = cell.getSoil();
                    soil.setOrganicMatter(soil.getOrganicMatter()
                            +
                            soilImprovementAfterEatingBothWaterAndPlant);
                }


                this.resetAnimal();
            }

            if (this.hasEatenPlant || this.hasEatenPrey || this.hasDrankWater) {
                if (this.checkIfAnimalHasBecomeIntoxicated(map)) {
                    Soil soil = cell.getSoil();
                    soil.setOrganicMatter(soil.getOrganicMatter()
                            +
                            soilImprovementAfterEatingSomethingElse);
                }



                this.resetAnimal();
            }

        }

        this.feedAnimal(map, differentiationBetweenTimestamps);
        this.setProcessedForThisTimestamp(true);
    }

}

class Vegetarians extends Animal {
    Vegetarians(final String name, final double mass, final String type) {
        super(name, mass, type);
    }

    @Override
    public void feedAnimal(final Map map, final int timestamp) {
        this.feedAnimalWithoutPrey(map);
        if (timestamp % 2 == 0) {
            String bestMove = bestMoveForAnimal(map);
            moveAnimal(map, bestMove);
        }
    }
}

class MeatEaters extends Animal {
    MeatEaters(final String name,
               final double mass,
               final String type) {
        super(name, mass, type);
    }

    @Override
    public void feedAnimal(final Map map, final int timestamp) {
        if (timestamp % 2 == 0) {
            this.feedAnimalWithPrey(map);
            if (this.feedWithPrey) {
                String bestMove = bestMoveForAnimal(map);
                moveAnimal(map, bestMove);
            } else {
                this.feedAnimalWithoutPrey(map);
                String bestMove = bestMoveForAnimal(map);
                moveAnimal(map, bestMove);
            }
        } else {
            this.feedAnimalWithoutPrey(map);
        }
    }
}

class Herbivores extends Vegetarians {
    Herbivores(final String name, final double mass) {
        super(name, mass, "Herbivores");
        this.possibilityToAttackRobot
                =
                calculatePossibilityToAttackRobot(herbivoresAttackPossibility);
    }
}

class Carnivores extends MeatEaters {
    Carnivores(final String name, final double mass) {
        super(name, mass, "Carnivores");
        this.possibilityToAttackRobot
                =
                calculatePossibilityToAttackRobot(carnivoresAttackPossibility);
    }
}

class Omnivores extends Vegetarians {
    Omnivores(final String name, final double mass) {
        super(name, mass, "Omnivores");
        this.possibilityToAttackRobot
                =
                calculatePossibilityToAttackRobot(omnivoresAttackPossibility);
    }
}

class Detritivores extends Vegetarians {
    Detritivores(final String name, final double mass) {
        super(name, mass, "Detritivores");
        this.possibilityToAttackRobot
                =
                calculatePossibilityToAttackRobot(detrivoresAttackPossibility);
    }
}

class Parasites extends MeatEaters {
    Parasites(final String name, final double mass) {
        super(name, mass, "Parasites");
        this.possibilityToAttackRobot
                =
                calculatePossibilityToAttackRobot(parasitesAttackPossibility);
    }
}
