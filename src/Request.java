import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий запрос на поиск маршрута
 */
public class Request {
    private final String fromCity;
    private final String toCity;
    private final List<Criteria> priorities;
    
    public Request(String fromCity, String toCity, List<Criteria> priorities) {
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.priorities = new ArrayList<>(priorities);
    }
    
    // Геттеры
    public String getFromCity() { 
        return fromCity; 
    }
    
    public String getToCity() { 
        return toCity; 
    }
    
    public List<Criteria> getPriorities() { 
        return new ArrayList<>(priorities); 
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(fromCity).append(" -> ").append(toCity).append(" | (");
        
        for (int i = 0; i < priorities.size(); i++) {
            switch (priorities.get(i)) {
                case LENGTH -> sb.append("Д");
                case TIME -> sb.append("В");
                case COST -> sb.append("С");
            }
            if (i < priorities.size() - 1) {
                sb.append(",");
            }
        }
        sb.append(")");
        
        return sb.toString();
    }
}