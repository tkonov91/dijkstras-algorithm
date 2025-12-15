import java.io.IOException;
import java.util.List;

/**
 * Главный класс приложения для оптимизации маршрутов.
 * Координирует работу всех компонентов системы.
 */
public class Main {
    public static void main(String[] args) {
        try {
            // Парсинг входных данных
            Parser parser = new Parser("input.txt");
            Graph graph = parser.parse();
            List<Request> requests = parser.getRequests();
            
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
            System.out.println("Результаты сохранены в output.txt");
            
        } catch (IOException e) {
            System.err.println("Ошибка при обработке файлов: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
    
    /**
     * Выбор компромиссного маршрута на основе приоритетов
     */
    private static Route selectCompromiseRoute(Route route1, Route route2, Route route3, List<Criteria> priorities) {
        List<Route> routes = List.of(route1, route2, route3);
        
        // Сортируем маршруты по приоритетам
        routes.sort((r1, r2) -> {
            for (Criteria priority : priorities) {
                int compare = compareByCriteria(r1, r2, priority);
                if (compare != 0) {
                    return compare;
                }
            }
            return 0;
        });
        
        return routes.get(0);
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