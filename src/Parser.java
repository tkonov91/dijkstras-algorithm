import java.io.*;
import java.util.*;

/**
 * Парсер для чтения и разбора входного файла
 */
public class Parser {
    private final String filename;
    private final List<Request> requests = new ArrayList<>();
    private final Graph graph = new Graph();
    private final Map<Integer, String> cityIdToName = new HashMap<>();
    
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
            
            City city = new City(id, name);
            graph.addCity(city);
            cityIdToName.put(id, name);
        }
    }
    
    /**
     * Парсинг строки с дорогой
     */
    private void parseRoad(String line) {
        try {
            // Формат: "ID1 - ID2: длина, время, стоимость"
            String[] mainParts = line.split(":");
            if (mainParts.length != 2) {
                System.err.println("Некорректный формат дороги: " + line);
                return;
            }
            
            String citiesPart = mainParts[0].trim();
            String paramsPart = mainParts[1].trim();
            
            // Извлекаем ID городов
            String[] cityIds = citiesPart.split("-");
            if (cityIds.length != 2) {
                System.err.println("Некорректные ID городов: " + line);
                return;
            }
            
            int cityId1 = Integer.parseInt(cityIds[0].trim());
            int cityId2 = Integer.parseInt(cityIds[1].trim());
            
            // Получаем названия городов по ID
            String cityName1 = cityIdToName.get(cityId1);
            String cityName2 = cityIdToName.get(cityId2);
            
            if (cityName1 == null || cityName2 == null) {
                System.err.println("Город с указанным ID не найден: " + line);
                return;
            }
            
            // Извлекаем параметры
            String[] params = paramsPart.split(",");
            if (params.length != 3) {
                System.err.println("Некорректные параметры дороги: " + line);
                return;
            }
            
            int length = Integer.parseInt(params[0].trim());
            int time = Integer.parseInt(params[1].trim());
            int cost = Integer.parseInt(params[2].trim());
            
            // Создаем дорогу
            Road road = new Road(cityName1, cityName2, length, time, cost);
            graph.addRoad(road);
            
        } catch (NumberFormatException e) {
            System.err.println("Ошибка парсинга чисел в строке: " + line);
        } catch (Exception e) {
            System.err.println("Ошибка при обработке строки: " + line + " - " + e.getMessage());
        }
    }
    
    /**
     * Парсинг строки с запросом
     */
    private void parseRequest(String line) {
        try {
            // Формат: "Город1 -> Город2 | (Приоритеты)"
            String[] parts = line.split("\\|");
            if (parts.length != 2) {
                System.err.println("Некорректный формат запроса: " + line);
                return;
            }
            
            String citiesPart = parts[0].trim();
            String prioritiesPart = parts[1].trim();
            
            // Извлекаем города
            String[] cities = citiesPart.split("->");
            if (cities.length != 2) {
                System.err.println("Некорректные города в запросе: " + line);
                return;
            }
            
            String fromCity = cities[0].trim();
            String toCity = cities[1].trim();
            
            // Извлекаем приоритеты
            prioritiesPart = prioritiesPart.substring(1, prioritiesPart.length() - 1);
            String[] priorityTokens = prioritiesPart.split(",");
            List<Criteria> priorities = new ArrayList<>();
            
            for (String token : priorityTokens) {
                String trimmedToken = token.trim();
                switch (trimmedToken) {
                    case "Д" -> priorities.add(Criteria.LENGTH);
                    case "В" -> priorities.add(Criteria.TIME);
                    case "С" -> priorities.add(Criteria.COST);
                    default -> System.err.println("Неизвестный приоритет: " + trimmedToken);
                }
            }
            
            if (priorities.size() == 3) {
                requests.add(new Request(fromCity, toCity, priorities));
            } else {
                System.err.println("Не все приоритеты указаны в запросе: " + line);
            }
            
        } catch (Exception e) {
            System.err.println("Ошибка при обработке запроса: " + line + " - " + e.getMessage());
        }
    }
    
    public List<Request> getRequests() {
        return new ArrayList<>(requests);
    }
    
    /**
     * Перечисление режимов парсинга
     */
    private enum ParseMode {
        NONE, CITIES, ROADS, REQUESTS
    }
}