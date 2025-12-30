/**
 * Класс для представления сегмента маршрута (часть дороги)
 */
public class RoadSegment {
    private final String fromCity;
    private final String toCity;
    private final int length;
    private final int time;
    private final int cost;
    
    public RoadSegment(String fromCity, String toCity, int length, int time, int cost) {
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
    
    @Override
    public String toString() {
        return fromCity + " -> " + toCity + " (" + length + "km, " + time + "min, " + cost + "rub)";
    }
}