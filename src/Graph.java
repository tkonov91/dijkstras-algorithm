import java.util.*;

/**
 * Класс, представляющий граф городов и дорог.
 * Реализует алгоритм Дейкстры для поиска кратчайших путей.
 */
public class Graph {
    private final Map<String, City> cities = new HashMap<>();
    private final Map<String, List<Road>> adjacencyList = new HashMap<>();
    
    /**
     * Добавляет город в граф
     */
    public void addCity(City city) {
        cities.put(city.getName(), city);
        adjacencyList.putIfAbsent(city.getName(), new ArrayList<>());
    }
    
    /**
     * Добавляет дорогу в граф (двустороннюю)
     */
    public void addRoad(Road road) {
        // Добавляем дорогу в обоих направлениях
        adjacencyList.get(road.getFromCity()).add(road);
        adjacencyList.get(road.getToCity()).add(road.getReverse());
    }
    
    /**
     * Поиск оптимального маршрута по заданному критерию
     * используя алгоритм Дейкстры
     */
    public Route findShortestRoute(String from, String to, Criteria criteria) {
        if (!cities.containsKey(from) || !cities.containsKey(to)) {
            throw new IllegalArgumentException("Город не найден");
        }
        
        // Инициализация структур данных для алгоритма Дейкстры
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> predecessors = new HashMap<>();
        PriorityQueue<CityNode> queue = new PriorityQueue<>();
        
        // Устанавливаем начальные расстояния
        for (String cityName : cities.keySet()) {
            distances.put(cityName, Integer.MAX_VALUE);
        }
        distances.put(from, 0);
        
        queue.offer(new CityNode(from, 0));
        
        // Основной цикл алгоритма Дейкстры
        while (!queue.isEmpty()) {
            CityNode current = queue.poll();
            
            // Если достигли целевого города
            if (current.cityName.equals(to)) {
                break;
            }
            
            // Пропускаем, если нашли более короткий путь до этой вершины
            if (current.distance > distances.get(current.cityName)) {
                continue;
            }
            
            // Обход соседей
            for (Road road : adjacencyList.get(current.cityName)) {
                String neighbor = road.getOtherCity(current.cityName);
                if (neighbor == null) continue;
                
                int newDistance = current.distance + getRoadValue(road, criteria);
                
                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    predecessors.put(neighbor, current.cityName);
                    queue.offer(new CityNode(neighbor, newDistance));
                }
            }
        }
        
        // Восстановление пути
        List<String> path = reconstructPath(predecessors, from, to);
        
        // Если путь не найден
        if (path.isEmpty() || !path.get(path.size() - 1).equals(to)) {
            return new Route(Collections.emptyList());
        }
        
        // Создание маршрута с вычислением всех параметров
        return createRoute(path);
    }
    
    /**
     * Получение значения дороги по критерию
     */
    private int getRoadValue(Road road, Criteria criteria) {
        return switch (criteria) {
            case LENGTH -> road.getLength();
            case TIME -> road.getTime();
            case COST -> road.getCost();
        };
    }
    
    /**
     * Восстановление пути по предшественникам
     */
    private List<String> reconstructPath(Map<String, String> predecessors, String from, String to) {
        LinkedList<String> path = new LinkedList<>();
        String current = to;
        
        while (current != null) {
            path.addFirst(current);
            current = predecessors.get(current);
            
            // Проверка на наличие цикла
            if (path.contains(current)) {
                break;
            }
        }
        
        return path;
    }
    
    /**
     * Создание объекта Route из пути
     */
    private Route createRoute(List<String> cityNames) {
        List<RoadSegment> segments = new ArrayList<>();
        
        for (int i = 0; i < cityNames.size() - 1; i++) {
            String from = cityNames.get(i);
            String to = cityNames.get(i + 1);
            
            Road road = findRoadBetween(from, to);
            if (road != null) {
                segments.add(new RoadSegment(from, to, 
                    road.getLength(), road.getTime(), road.getCost()));
            }
        }
        
        return new Route(segments);
    }
    
    /**
     * Поиск дороги между двумя городами
     */
    private Road findRoadBetween(String city1, String city2) {
        for (Road road : adjacencyList.get(city1)) {
            if ((road.getFromCity().equals(city1) && road.getToCity().equals(city2)) ||
                (road.getFromCity().equals(city2) && road.getToCity().equals(city1))) {
                return road;
            }
        }
        return null;
    }
    
    /**
     * Вспомогательный класс для алгоритма Дейкстры
     */
    private static class CityNode implements Comparable<CityNode> {
        String cityName;
        int distance;
        
        CityNode(String cityName, int distance) {
            this.cityName = cityName;
            this.distance = distance;
        }
        
        @Override
        public int compareTo(CityNode other) {
            return Integer.compare(this.distance, other.distance);
        }
    }
}