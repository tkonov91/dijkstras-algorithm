public class Road {
    private final String fromCity;
    private final String toCity;
    private final int length;
    private final int time;
    private final int cost;
    
    public Road(String fromCity, String toCity, int length, int time, int cost) {
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.length = length;
        this.time = time;
        this.cost = cost;
    }
    
    // Геттеры и другие методы
    public String getOtherCity(String city) {
        if (fromCity.equals(city)) return toCity;
        if (toCity.equals(city)) return fromCity;
        return null;
    }
    
    public Road getReverse() {
        return new Road(toCity, fromCity, length, time, cost);
    }
}