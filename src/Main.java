import java.io.IOException;
import java.util.*;

/**
 * Главный класс приложения для оптимизации маршрутов.
 * Координирует работу всех компонентов системы.
 */
public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Запуск системы оптимизации маршрутов...");
            
            // Парсинг входных данных
            Parser parser = new Parser("input.txt");
            Graph graph = parser.parse();
            List<Request> requests = parser.getRequests();
            
            System.out.println("Загружено запросов: " + requests.size());
            
            // Обработка запросов
            ResultWriter writer = new ResultWriter("output.txt");
            
            for (Request request : requests) {
                System.out.println("Обработка запроса: " + request);
                
                // Поиск оптимальных маршрутов по каждому критерию
                Route shortestRoute = graph.findShortestRoute(
                    request.getFromCity(), 
                    request.getToCity(), 
                    Criteria.LENGTH
                );
                
                Route fastestRoute = graph.findShortestRoute(
                    request.getFromCity(), 
                    request.getToCity(), 
                    Criteria.TIME
                );
                
                Route cheapestRoute = graph.findShortestRoute(
                    request.getFromCity(), 
                    request.getToCity(), 
                    Criteria.COST
                );
                
                // Выбор компромиссного маршрута
                Route compromiseRoute = selectCompromiseRoute(
                    shortestRoute, 
                    fastestRoute, 
                    cheapestRoute, 
                    request.getPriorities()
                );
                
                // Запись результатов
                writer.writeRouteResult("ДЛИНА", shortestRoute);
                writer.writeRouteResult("ВРЕМЯ", fastestRoute);
                writer.writeRouteResult("СТОИМОСТЬ", cheapestRoute);
                writer.writeRouteResult("КОМПРОМИСС", compromiseRoute);
                writer.writeEmptyLine();
            }
            
            writer.close();
            System.out.println("Результаты успешно сохранены в output.txt");
            
        } catch (IOException e) {
            System.err.println("Ошибка при обработке файлов: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Выбор компромиссного маршрута на основе приоритетов
     */
    private static Route selectCompromiseRoute(Route route1, Route route2, Route route3, 
                                               List<Criteria> priorities) {
        List<Route> routes = Arrays.asList(route1, route2, route3);
        
        // Удаляем пустые маршруты
        List<Route> validRoutes = new ArrayList<>();
        for (Route route : routes) {
            if (route != null && !route.isEmpty()) {
                validRoutes.add(route);
            }
        }
        
        if (validRoutes.isEmpty()) {
            return new Route(new ArrayList<>());
        }
        
        // Сортируем маршруты по приоритетам
        validRoutes.sort((r1, r2) -> {
            for (Criteria priority : priorities) {
                int compare = compareByCriteria(r1, r2, priority);
                if (compare != 0) {
                    return compare;
                }
            }
            return 0;
        });
        
        return validRoutes.get(0);
    }
    
    /**
     * Сравнение маршрутов по заданному критерию
     */
    private static int compareByCriteria(Route r1, Route r2, Criteria criteria) {
        return switch (criteria) {
            case LENGTH -> Integer.compare(r1.getTotalLength(), r2.getTotalLength());
            case TIME -> Integer.compare(r1.getTotalTime(), r2.getTotalTime());
            case COST -> Integer.compare(r1.getTotalCost(), r2.getTotalCost());
        };
    }
}