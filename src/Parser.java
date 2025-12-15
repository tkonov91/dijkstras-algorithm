import java.io.*;
import java.util.*;

/**
 * Парсер для чтения и разбора входного файла
 */
public class Parser {
    private final String filename;
    private final List<Request> requests = new ArrayList<>();
    private final Graph graph = new Graph();
    
    public Parser(String filename) {
        this.filename = filename;
    }
    
    /**
     * Основной метод парсинга файла
     */
    public Graph parse() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            ParseMode mode = ParseMode.NONE;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                // Пропускаем пустые строки
                if (line.isEmpty()) continue;
                
                // Определение секции
                if (line.equals("[CITIES]")) {
                    mode = ParseMode.CITIES;
                    continue;
                } else if (line.equals("[ROADS]")) {
                    mode = ParseMode.ROADS;
                    continue;
                } else if (line.equals("[REQUESTS]")) {
                    mode = ParseMode.REQUESTS;
                    continue;
                }
                
                // Обработка строк в зависимости от секции
                switch (mode) {
                    case CITIES -> parseCity(line);
                    case ROADS -> parseRoad(line);
                    case REQUESTS -> parseRequest(line);
                    default -> {}
                }
            }
        }
        
        return graph;
    }
    
    /**
     * Парсинг строки с городом
     */
    private void parseCity(String line) {
        String[] parts = line.split(":", 2);
        if (parts.length == 2) {
            int id = Integer.parseInt(parts[0].trim());
            String name = parts[1].trim();
            graph.addCity(new City(id, name));
        }
    }
    
    /**
     * Парсинг строки с дорогой
     */
    private void parseRoad(String line) {
        // Формат: "ID1 - ID2: длина, время, стоимость"
        String[] mainParts = line.split(":");
        if (mainParts.length != 2) return;
        
        String citiesPart = mainParts[0].trim();
        String paramsPart = mainParts[1].trim();
        
        // Извлекаем ID городов
        String[] cityIds = citiesPart.split("-");
        if (cityIds.length != 2) return;
        
        int cityId1 = Integer.parseInt(cityIds[0].trim());
        int cityId2 = Integer.parseInt(cityIds[1].trim());
        
        // Извлекаем параметры
        String[] params = paramsPart.split(",");
        if (params.length != 3) return;
        
        int length = Integer.parseInt(params[0].trim());
        int time = Integer.parseInt(params[1].trim());
        int cost = Integer.parseInt(params[2].trim());
        
        // Создаем дорогу (городы будут добавлены позже)
        // В реальной реализации нужно получить названия городов по ID
        Road road = new Road("City" + cityId1, "City" + cityId2, length, time, cost);
        graph.addRoad(road);
    }
    
    /**
     * Парсинг строки с запросом
     */
    private void parseRequest(String line) {
        // Формат: "Город1 -> Город2 | (Приоритеты)"
        String[] parts = line.split("\\|");
        if (parts.length != 2) return;
        
        String citiesPart = parts[0].trim();
        String prioritiesPart = parts[1].trim();
        
        // Извлекаем города
        String[] cities = citiesPart.split("->");
        if (cities.length != 2) return;
        
        String fromCity = cities[0].trim();
        String toCity = cities[1].trim();
        
        // Извлекаем приоритеты
        prioritiesPart = prioritiesPart.substring(1, prioritiesPart.length() - 1);
        String[] priorityTokens = prioritiesPart.split(",");
        List<Criteria> priorities = new ArrayList<>();
        
        for (String token : priorityTokens) {
            switch (token.trim()) {
                case "Д" -> priorities.add(Criteria.LENGTH);
                case "В" -> priorities.add(Criteria.TIME);
                case "С" -> priorities.add(Criteria.COST);
            }
        }
        
        requests.add(new Request(fromCity, toCity, priorities));
    }
    
    public List<Request> getRequests() {
        return requests;
    }
    
    /**
     * Перечисление режимов парсинга
     */
    private enum ParseMode {
        NONE, CITIES, ROADS, REQUESTS
    }
}