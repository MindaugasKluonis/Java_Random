public class TreeGarden implements Garden {
    public Plant getShade() {
        return new Plant("Tree 1", "GROUND");
    }
    public Plant getCenter() {
        return new Plant("Tree 2","GROUND2");
    }
    public Plant getBorder() {
        return new Plant("Tree 3","GROUND");
    }

}