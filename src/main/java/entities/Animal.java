package entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import robot.Robot;

import map.Map;
import map.MapCardinalPoints;

@Getter
@Setter
public abstract class Animal extends Entity {
    @JsonIgnore
    protected double possibilityToAttackRobot;
    @JsonIgnore
    protected String stateOfHunger;
    @JsonIgnore
    protected int timestampAtWhichItWasScanned;
    @JsonIgnore
    protected double preyMass;
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

    public double calculatePossibilityToAttackRobot(final double attackPossibility) {
        return (oneHundred - attackPossibility) / 10.0;
    }

    public Animal() {
    }

    public Animal(final String name, final double mass, final String type) {
        super(name, mass, type);
        this.stateOfHunger = "hungry";
    }

    public boolean checkIfInBounds(Map map, int xToMoveAnimal, int yToMoveAnimal) {
        return xToMoveAnimal >= zero && xToMoveAnimal < map.getRowLength()
                &&
                yToMoveAnimal >= zero && yToMoveAnimal < map.getColumnLength();
    }

    public void moveAnimal(Map map, String bestMove) {
        Map.MapCell oldCell = map.getMapCell(this.x, this.y);
        switch(bestMove) {
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
        }
        Map.MapCell newCell = map.getMapCell(this.x, this.y);
        newCell.setAnimal(this);
        oldCell.setAnimal(null);
    }

    public void feedAnimalWithoutPrey(Map map) {
        Map.MapCell currentCell = map.getMapCell(this.x, this.y);
        this.feedWithPrey = false;
        double intakeRate = 0.08;
        if (currentCell.getPlant() != null && currentCell.getPlant().isScannedByRobot()
                &&
                currentCell.getWater() != null && currentCell.getWater().isScannedByRobot()) {
            double plantMass = currentCell.getPlant().getMass();
            double waterToDrink = Math.min(currentCell.getAnimal().getMass() * intakeRate, currentCell.getWater().getMass());
            currentCell.getWater().setMass(currentCell.getWater().getMass() - waterToDrink);
            if (currentCell.getWater().getMass() <= 0.0) {
                currentCell.setWater(null);
            }
            currentCell.getAnimal().setMass(waterToDrink + plantMass);
            currentCell.setPlant(null);
            currentCell.getAnimal().setStateOfHunger("well-fed");
            currentCell.getAnimal().setHasDrankWater(true);
            currentCell.getAnimal().setHasEatenPlant(true);
        } else if (currentCell.getPlant() != null
                &&
                currentCell.getPlant().isScannedByRobot()) {
            double plantMass = currentCell.getPlant().getMass();
            currentCell.getAnimal().setMass(plantMass + currentCell.getAnimal().getMass());
            currentCell.setPlant(null);
            currentCell.getAnimal().setStateOfHunger("well-fed");
            currentCell.getAnimal().setHasEatenPlant(true);
        } else if (currentCell.getWater() != null
                &&
                currentCell.getWater().isScannedByRobot()) {
            double waterToDrink = Math.min(currentCell.getAnimal().getMass() * intakeRate, currentCell.getWater().getMass());
            currentCell.getWater().setMass(currentCell.getWater().getMass() - waterToDrink);
            currentCell.getAnimal().setMass(waterToDrink + currentCell.getAnimal().getMass());
            if (currentCell.getWater().getMass() <= 0.0) {
                currentCell.setWater(null);
            }
            currentCell.getAnimal().setStateOfHunger("well-fed");
            currentCell.getAnimal().setHasDrankWater(true);
        } else {
            currentCell.getAnimal().setStateOfHunger("hungry");
        }
    }

    /**
     * will use codifications to store the best candidates for the animal to move,
     * 3 - cell has both water and plant, will need comparison to get the best quality for water
     * 2 - cell has only plant, the animal moves either way, doesn't care about the water
     * 1 - cell has only water, the animal moves to the best water quality cell
     * 0 - cell has neither, the animal moves to the ordinary cell, up right down left
     * */
    public String bestMoveForAnimal(Map map) {
        Map.MapCell[] adjacentCells = new Map.MapCell[4];

        for (MapCardinalPoints mapCoordinates : MapCardinalPoints.values()) {
            if (mapCoordinates == MapCardinalPoints.NORTH) {
                if (checkIfInBounds(map, this.x, this.y + 1)) {
                    adjacentCells[0] = map.getMapCell(this.x, this.y + 1);
                } else {
                    adjacentCells[0] = null;
                }
            } else if (mapCoordinates == MapCardinalPoints.EAST) {
                if (checkIfInBounds(map, this.x + 1, this.y)) {
                    adjacentCells[1] = map.getMapCell(this.x + 1, this.y);
                } else {
                    adjacentCells[1] = null;
                }
            } else if (mapCoordinates == MapCardinalPoints.SOUTH) {
                if (checkIfInBounds(map, this.x, this.y - 1)) {
                    adjacentCells[2] = map.getMapCell(this.x, this.y - 1);
                } else {
                    adjacentCells[2] = null;
                }
            } else if (mapCoordinates == MapCardinalPoints.WEST) {
                if (checkIfInBounds(map, this.x - 1, this.y)) {
                    adjacentCells[3] = map.getMapCell(this.x - 1, this.y);
                } else {
                    adjacentCells[3] = null;
                }
            }
        }

        int[] bestCandidate = new int[4];

        //cred ca trebuie sa iau in calcul si daca este scanata entitatea respectiva, adica planta sau apa !!!

        for (int i = 0; i < adjacentCells.length; i++) {
            Map.MapCell adjacentCell = adjacentCells[i];
            if (adjacentCell != null && adjacentCell.getWater() != null && adjacentCell.getPlant() != null) {
                bestCandidate[i] = 3;
            } else if (adjacentCell != null && adjacentCell.getPlant() != null) {
                bestCandidate[i] = 2;
            } else if (adjacentCell != null && adjacentCell.getWater() != null) {
                bestCandidate[i] = 1;
            } else if (adjacentCell != null) {
                bestCandidate[i] = 0;
            } else {
                bestCandidate[i] = -1;
            }
        }

        int bestPriority = -1;
        int bestIndex = -1;
        double bestWaterQuality = -1.0;

        for (int priority : bestCandidate) {
            if (priority > bestPriority) {
                bestPriority = priority;
            }
        }

        if (bestPriority == 3) {
            for (int i = 0; i < bestCandidate.length; i++) {
                if (bestCandidate[i] == 3) {
                    double currentWaterQuality = adjacentCells[i].getWater().getWaterQuality();
                    if (currentWaterQuality > bestWaterQuality) {
                        bestWaterQuality = currentWaterQuality;
                        bestIndex = i;
                    }
                }
            }
        } else if (bestPriority == 2) {
            for (int i = 0; i < bestCandidate.length; i++) {
                if (bestCandidate[i] == 2) {
                    bestIndex = i;
                    break;
                }
            }
        } else if (bestPriority == 1) {
            for (int i = 0; i < bestCandidate.length; i++) {
                if (bestCandidate[i] == 1) {
                    double currentWaterQuality = adjacentCells[i].getWater().getWaterQuality();
                    if (currentWaterQuality > bestWaterQuality) {
                        bestWaterQuality = currentWaterQuality;
                        bestIndex = i;
                    }
                }
            }
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

    public void feedAnimalWithPrey(Map map) {
        String bestPositionForCell = bestMoveForAnimal(map);
        Animal animalPrey = null;
        Map.MapCell animalPreyCell = null;
        //aici mi l fac null din ceva motiv , e posibil sa trebuiasca sa mut animalul
        //din ceva motiv acolo in debug, sa ma mai uit !!!

        switch (bestPositionForCell) {
            case "NORTH" -> animalPrey = map.getMapCell(this.x, this.y + 1).getAnimal();
            case "EAST" -> animalPrey = map.getMapCell(this.x + 1, this.y).getAnimal();
            case "SOUTH" -> animalPrey = map.getMapCell(this.x, this.y - 1).getAnimal();
            case "WEST" -> animalPrey = map.getMapCell(this.x - 1, this.y).getAnimal();
        }

        if (animalPrey == null) {
            return;
        }

        animalPreyCell = map.getMapCell(animalPrey.getX(), animalPrey.getY());

        this.setPreyMass(animalPrey.getMass());
        this.feedWithPrey = true;
        animalPreyCell.setAnimal(null);
    }

    @Override
    public void updateMapWithScannedObject(Robot robot, Map map, Map.MapCell cell, int timestamp) {
        if (this.processedForThisTimestamp) {
            return;
        }

        int differentiationBetweenTimestamps = timestamp - this.timestampAtWhichItWasScanned;

        if (this.getTimestampAtWhichItWasScanned() == timestamp) {
            return;
        }

        if (this.stateOfHunger.equals("well-fed")) {
            if (this.hasEatenPlant && this.hasDrankWater) {
                map.getMapCell(this.x, this.y).getSoil().
                        setOrganicMatter(map.getMapCell(this.x, this.y).
                                getSoil().getOrganicMatter() + 0.8);
                this.stateOfHunger = "hungry";
                this.hasDrankWater = false;
                this.hasEatenPlant = false;
                this.hasEatenPrey = false;
            }

            if (this.hasEatenPlant || this.hasEatenPrey || this.hasDrankWater) {
                map.getMapCell(this.x, this.y).getSoil().
                        setOrganicMatter(map.getMapCell(this.x, this.y).
                                getSoil().getOrganicMatter() + 0.5);
                this.stateOfHunger = "hungry";
                this.hasDrankWater = false;
                this.hasEatenPlant = false;
                this.hasEatenPrey = false;
            }

        }

        if (this.type.equals("Carnivores") || this.type.equals("Parasites")) {
            if (differentiationBetweenTimestamps % 2 == 0) {
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
        } else {
            this.feedAnimalWithoutPrey(map);
            if (differentiationBetweenTimestamps % 2 == 0) {
                String bestMove = bestMoveForAnimal(map);
                moveAnimal(map, bestMove);
            }
        }
        this.setProcessedForThisTimestamp(true);
    }

}

class Herbivores extends Animal {
    public Herbivores(final String name, final double mass) {
        super(name, mass, "Herbivores");
        this.possibilityToAttackRobot = calculatePossibilityToAttackRobot(85);
    }
}

class Carnivores extends Animal {
    public Carnivores(final String name, final double mass) {
        super(name, mass, "Carnivores");
        this.possibilityToAttackRobot = calculatePossibilityToAttackRobot(30);
    }
}

class Omnivores extends Animal {
    public Omnivores(final String name, final double mass) {
        super(name, mass, "Omnivores");
        this.possibilityToAttackRobot = calculatePossibilityToAttackRobot(60);
    }
}

class Detritivores extends Animal {
    public Detritivores(final String name, final double mass) {
        super(name, mass, "Detritivores");
        this.possibilityToAttackRobot = calculatePossibilityToAttackRobot(90);
    }
}

class Parasites extends Animal {
    public Parasites(final String name, final double mass) {
        super(name, mass, "Parasites");
        this.possibilityToAttackRobot = calculatePossibilityToAttackRobot(10);
    }
}
