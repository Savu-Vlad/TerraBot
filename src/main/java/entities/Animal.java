package entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

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

    public double calculatePossibilityToAttackRobot(double attackPossibility) {
        return (oneHundred - attackPossibility) / 10.0;
    }

    public Animal() {
    }

    public Animal(String name, double mass, String type) {
        super(name, mass, type);
        this.stateOfHunger = "hungry";
    }

    public boolean checkIfInBounds(Map map, int xToMoveAnimal, int yToMoveAnimal) {
        return xToMoveAnimal >= 0 && xToMoveAnimal < map.getRowLength() && yToMoveAnimal >= 0 && yToMoveAnimal < map.getColumnLength();
    }

    public void moveAnimal(String bestMove, Map.MapCell cell) {
        switch(bestMove) {
            case "NORTH" -> {
                cell.getAnimal().setY(cell.getAnimal().getY() + 1);
                this.y += 1;
            }
            case "EAST" -> {
                cell.getAnimal().setX(cell.getAnimal().getX() + 1);
                this.x += 1;
            }
            case "SOUTH" -> {
                cell.getAnimal().setY(cell.getAnimal().getY() - 1);
                this.y -= 1;
            }
            case "WEST" -> {
                cell.getAnimal().setX(cell.getAnimal().getX() - 1);
                this.x -= 1;
            }
        }
    }

    /**
    * Will have a default implementation for all other types except for Carnivore and Parasite
    * They eat other animals and will be Overridden in their specific classes !!!
     * */
    public void feedAnimal(Map map, Map.MapCell cell, int timestamp) {
        int differentiationBetweenTimestamps = timestamp - cell.getAnimal().getTimestampAtWhichItWasScanned();
        feedAnimalWithoutPrey(map);
        if (differentiationBetweenTimestamps % 2 == 0) {
            String bestMove = bestMoveForAnimal(map);
            moveAnimal(bestMove, cell);
            Map.MapCell newCell = map.getMapCell(cell.getAnimal().getX(), cell.getAnimal().getY());
            newCell.setAnimal(cell.getAnimal());
            cell.setAnimal(null);
        }
    }

    public void feedAnimalWithoutPrey(Map map) {
        Map.MapCell currentCell = map.getMapCell(this.x, this.y);

        if (currentCell.getAnimal().getPreyMass() != 0.0) {
            currentCell.getAnimal().setMass(currentCell.getAnimal().getMass() + currentCell.getAnimal().getPreyMass());
            currentCell.getAnimal().setPreyMass(0.0);
            currentCell.getAnimal().setStateOfHunger("well-fed");
            return;
        }

        if (currentCell.getPlant() != null && currentCell.getWater() != null) {
            if (currentCell.getPlant().isScannedByRobot() && currentCell.getWater().isScannedByRobot()) {
                double plantMass = currentCell.getPlant().getMass();
                double intakeRate = 0.08;
                double waterToDrink = Math.min(currentCell.getAnimal().getMass() * intakeRate, currentCell.getWater().getMass());
                currentCell.getWater().setMass(currentCell.getWater().getMass() - waterToDrink);
                currentCell.getAnimal().setMass(waterToDrink + plantMass);
                currentCell.setPlant(null);
                currentCell.getAnimal().setStateOfHunger("well-fed");
            } else if (currentCell.getPlant().isScannedByRobot()) {
                double plantMass = currentCell.getPlant().getMass();
                currentCell.getAnimal().setMass(plantMass + currentCell.getAnimal().getMass());
                currentCell.setPlant(null);
                currentCell.getAnimal().setStateOfHunger("well-fed");
            } else if (currentCell.getWater().isScannedByRobot()) {
                double intakeRate = 0.08;
                double waterToDrink = Math.min(currentCell.getAnimal().getMass() * intakeRate, currentCell.getWater().getMass());
                currentCell.getWater().setMass(currentCell.getWater().getMass() - waterToDrink);
                currentCell.getAnimal().setMass(waterToDrink + currentCell.getAnimal().getMass());
                currentCell.getAnimal().setStateOfHunger("well-fed");
            }
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

    public void feedAnimalWithPrey(Map map, Map.MapCell cell) {
        String bestPositionForCell = bestMoveForAnimal(map);
        Animal animalPrey = null;

        switch (bestPositionForCell) {
            case "NORTH" -> animalPrey = map.getMapCell(this.x, this.y + 1).getAnimal();
            case "EAST" -> animalPrey = map.getMapCell(this.x + 1, this.y).getAnimal();
            case "SOUTH" -> animalPrey = map.getMapCell(this.x, this.y - 1).getAnimal();
            case "WEST" -> animalPrey = map.getMapCell(this.x - 1, this.y).getAnimal();
        }

        if (animalPrey == null) {
            return;
        }

        cell.getAnimal().setPreyMass(animalPrey.getMass());
        map.getMapCell(animalPrey.getX(), animalPrey.getY()).setAnimal(null);

    }

    @Override
    public void updateMapWithScannedObject(Map map, Map.MapCell cell, int timestamp) {

        //nu cred ca a rezolvat probleme pe deplin!!!!
        if (cell.getAnimal() == null) {
            return;
        }

        int differentiationBetweenTimestamps = timestamp - cell.getAnimal().getTimestampAtWhichItWasScanned();


        if (cell.getAnimal().getTimestampAtWhichItWasScanned() == timestamp) {
            return;
        }

        this.feedAnimal(map, cell, timestamp);
        if (this.getStateOfHunger().equals("well-fed")) {
            cell.getSoil().setOrganicMatter(roundScore(cell.getSoil().getOrganicMatter() + 0.5));
        }
    }

}

class Herbivores extends Animal {
    public Herbivores(String name, double mass) {
        super(name, mass, "Herbivores");
        this.possibilityToAttackRobot = calculatePossibilityToAttackRobot(85);
    }
}

class Carnivores extends Animal {
    public Carnivores(String name, double mass) {
        super(name, mass, "Carnivores");
        this.possibilityToAttackRobot = calculatePossibilityToAttackRobot(30);
    }

    @Override
    public void feedAnimal(Map map, Map.MapCell cell , int timestamp) {
        int differentiationBetweenTimestamps = timestamp - cell.getAnimal().getTimestampAtWhichItWasScanned();
        feedAnimalWithPrey(map, cell);
        if (differentiationBetweenTimestamps % 2 == 0) {
            String bestMove = bestMoveForAnimal(map);
            moveAnimal(bestMove, cell);
            Map.MapCell newCell = map.getMapCell(cell.getAnimal().getX(), cell.getAnimal().getY());
            newCell.setAnimal(cell.getAnimal());
            cell.setAnimal(null);
        }
    }
}

class Omnivores extends Animal {
    public Omnivores(String name, double mass) {
        super(name, mass, "Omnivores");
        this.possibilityToAttackRobot = calculatePossibilityToAttackRobot(60);
    }
}

class Detritivores extends Animal {
    public Detritivores(String name, double mass) {
        super(name, mass, "Detritivores");
        this.possibilityToAttackRobot = calculatePossibilityToAttackRobot(90);
    }
}

class Parasites extends Animal {
    public Parasites(String name, double mass) {
        super(name, mass, "Parasites");
        this.possibilityToAttackRobot = calculatePossibilityToAttackRobot(10);
    }

    @Override
    public void feedAnimal(Map map, Map.MapCell cell, int timestamp) {
        int differentiationBetweenTimestamps = timestamp - cell.getAnimal().getTimestampAtWhichItWasScanned();
        feedAnimalWithPrey(map, cell);
        if (differentiationBetweenTimestamps % 2 == 0) {
            String bestMove = bestMoveForAnimal(map);
            moveAnimal(bestMove, cell);
            Map.MapCell newCell = map.getMapCell(cell.getAnimal().getX(), cell.getAnimal().getY());
            newCell.setAnimal(cell.getAnimal());
            cell.setAnimal(null);
        }
    }
}
