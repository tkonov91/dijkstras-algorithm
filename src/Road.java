/**
 * Класс, представляющий дорогу между двумя городами
 */
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
    
    // Геттеры
    public String getFromCity() { 
        return fromCity; 
    }
    
    public String getToCity() { 
        return toCity; 
    }
    
    public int getLength() { 
        return length; 
    }
    
    public int getTime() { 
        return time; 
    }
    
    public int getCost() { 
        return cost; 
    }
    
    /**
     * Получить город на противоположном конце дороги
     */
    public String getOtherCity(String city) {
        if (fromCity.equals(city)) return toCity;
        if (toCity.equals(city)) return fromCity;
        return null;
    }
    
    /**
     * Создать обратную дорогу (для двустороннего движения)
     */
    public Road getReverse() {
        return new Road(toCity, fromCity, length, time, cost);
    }
    
    @Override
    public String toString() {
        return fromCity + " - " + toCity + ": " + length + "km, " + time + "min, " + cost + "rub";
    }
}