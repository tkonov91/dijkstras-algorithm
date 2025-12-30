import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий маршрут между городами
 */
public class Route {
    private final List<RoadSegment> segments;
    private final List<String> cityNames;
    
    public Route(List<RoadSegment> segments) {
        this.segments = segments;
        this.cityNames = extractCityNames(segments);
    }
    
    /**
     * Извлечь названия городов из сегментов маршрута
     */
    private List<String> extractCityNames(List<RoadSegment> segments) {
        List<String> names = new ArrayList<>();
        if (segments.isEmpty()) {
            return names;
        }
        
        // Добавляем первый город
        names.add(segments.get(0).getFromCity());
        
        // Добавляем все промежуточные города
        for (RoadSegment segment : segments) {
            names.add(segment.getToCity());
        }
        
        return names;
    }
    
    // Геттеры для общих параметров маршрута
    public int getTotalLength() {
        return segments.stream().mapToInt(RoadSegment::getLength).sum();
    }
    
    public int getTotalTime() {
        return segments.stream().mapToInt(RoadSegment::getTime).sum();
    }
    
    public int getTotalCost() {
        return segments.stream().mapToInt(RoadSegment::getCost).sum();
    }
    
    public List<String> getCityNames() {
        return new ArrayList<>(cityNames);
    }
    
    public boolean isEmpty() {
        return segments.isEmpty();
    }
    
    @Override
    public String toString() {
        if (isEmpty()) {
            return "Маршрут не найден";
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cityNames.size(); i++) {
            sb.append(cityNames.get(i));
            if (i < cityNames.size() - 1) {
                sb.append(" -> ");
            }
        }
        
        sb.append(" | Д=").append(getTotalLength())
          .append(", В=").append(getTotalTime())
          .append(", С=").append(getTotalCost());
        
        return sb.toString();
    }
}