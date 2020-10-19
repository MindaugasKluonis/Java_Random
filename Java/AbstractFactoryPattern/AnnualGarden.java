public class AnnualGarden implements Garden {
    public Plant getShade() {
        return new Plant("Coleus", "Good soil");
    }
    public Plant getCenter() {
        return new Plant("Marigold","Good soil");
    }
    public Plant getBorder() {
        return new Plant("Alyssum","Good soil");
    }

}
