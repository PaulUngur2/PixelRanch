package types;

public enum AnimalType {
    //Information on each animal
    Pig("Potatoes", 50, "Pork"),
    Chicken("Corn", 30,  "Eggs", "Chicken_Meat"),
    Rabbit("Carrots", 40, "Hide", "Rabbit_Meat"),
    Cow("Wheat", 100, "Milk", "Hide", "Beef"),
    Sheep("Hay", 80, "Wool", "Mutton");

    private final String food;
    private final String[] produce;
    private final int cost;

    AnimalType(String food, int cost, String... produce) {
        this.food = food;
        this.produce = produce;
        this.cost = cost;
    }

    public String getFood() {
        return food;
    }

    public String[] getProduce() {
        return produce;
    }

    public int getCost() {
        return cost;
    }
}

