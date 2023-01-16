package types;

public enum CropType {
    //Information on each crop
    Wheat(2,5),
    Corn(3,4),
    Potatoes(3,3),
    Carrots(4,2),
    Hay(1,6);

    private final int growTime;
    private final int cost;

    CropType(int growTime, int cost) {
        this.growTime = growTime;
        this.cost = cost;
    }

    public int getGrowTime() {
        return growTime;
    }

    public int getCost() {
        return cost;
    }
}
