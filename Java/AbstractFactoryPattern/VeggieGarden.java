public class VeggieGarden implements Garden {
    public Plant getShade() {
        return new Plant("Broccoli","Other");
    }
    public Plant getCenter() {
        return new Plant("Corn","Other");
    }
    public Plant getBorder() {
        return new Plant("Peas", "Water");
    }

}
