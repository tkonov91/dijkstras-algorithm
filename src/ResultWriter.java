import java.io.*;

/**
 * Класс для записи результатов в файл
 */
public class ResultWriter {
    private final BufferedWriter writer;
    
    public ResultWriter(String filename) throws IOException {
        this.writer = new BufferedWriter(new FileWriter(filename));
    }
    
    /**
     * Запись результата маршрута с указанием типа
     */
    public void writeRouteResult(String routeType, Route route) throws IOException {
        if (route.isEmpty()) {
            writer.write(routeType + ": Маршрут не найден");
        } else {
            String cities = String.join(" -> ", route.getCityNames());
            writer.write(routeType + ": " + cities + " | Д=" + route.getTotalLength() + 
                        ", В=" + route.getTotalTime() + ", С=" + route.getTotalCost());
        }
        writer.newLine();
    }
    
    /**
     * Запись пустой строки для разделения запросов
     */
    public void writeEmptyLine() throws IOException {
        writer.newLine();
    }
    
    /**
     * Закрытие потока записи
     */
    public void close() throws IOException {
        writer.close();
    }
}