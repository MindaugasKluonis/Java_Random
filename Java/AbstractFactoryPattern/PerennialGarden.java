public class PerennialGarden implements Garden {
    public Plant getShade() {
        return new Plant("Astilbe","BULLSHIT");
    }
    public Plant getCenter() {
        return new Plant("Dicentrum","BULLSHIT2");
    }
    public Plant getBorder() {
        return new Plant("Sedum","BULLSHIT3");
    }

}
