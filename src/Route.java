import java.util.List;

public class Route {
    private final List<RoadSegment> segments;
    
    public Route(List<RoadSegment> segments) {
        this.segments = segments;
    }
    
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
        // Возвращает список городов в маршруте
        // Реализация зависит от структуры RoadSegment
        return List.of();
    }
}