import com.google.gson.Gson;
import dto.*;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Consumer {
    public static void main(String[] args) {
        String nameOfSensor;
        String uiMessage =
                        "\nreg - Зарегистрировать новый датчик, " +
                        "\nsensors - Получить список зарегистрированых датчиков, " +
                        "\nadd - Добавить новый замер, " +
                        "\naddRandoms - Добавить N случайных замеров, " +
                        "\nget - Получить все замеры, " +
                        "\nrdCount - Получить количество дождливых дней, " +
                        "\nshowGraph - Получить график температур, " +
                        "\nexit - Выход" +
                        "\nВведите команду: ";
        System.out.print(uiMessage);
        String inputStr = new Scanner(System.in).nextLine();
        while (!inputStr.equalsIgnoreCase("exit")) {
            switch (inputStr) {
                case "reg":
                    System.out.print("Введите имя датчика: ");
                    nameOfSensor = new Scanner(System.in).nextLine();
                    try {
                        System.out.println(reg(nameOfSensor));
                    } catch (RuntimeException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "sensors":
                    try {
                        SensorDTOList sensors = sensors();
                        System.out.println(new Gson().toJson(sensors));
                    } catch (RuntimeException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "add":
                    try {
                        System.out.print("Введите имя датчика: ");
                        nameOfSensor = new Scanner(System.in).nextLine();

                        System.out.print("Введите температуру (-100 до 100): ");
                        String value = new Scanner(System.in).nextLine();

                        System.out.print("Введите 0 если нет дождя и 1 если есть: ");
                        boolean isRaining = new Scanner(System.in).nextLine().equals("1");

                        System.out.println(add(Double.parseDouble(value), isRaining, nameOfSensor));
                    } catch (RuntimeException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "addRandoms":
                    try {
                        System.out.print("Введите имя датчика: ");
                        nameOfSensor = new Scanner(System.in).nextLine();
                        System.out.print("Введите количество случайных замеров для добавления: ");
                        int n = Integer.parseInt(new Scanner(System.in).nextLine());
                        for (int i = 0; i < n; i++)
                            System.out.println(
                                    addRandoms((int) ((Math.random() * 20000) - 10000) / 100.0,
                                            Math.random() < 0.5,
                                            nameOfSensor));
                    } catch (RuntimeException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "get":
                    try {
                        MeasurementDTOList measurements = get();
                        System.out.println(new Gson().toJson(measurements));
                    } catch (RuntimeException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "rdCount":
                    try {
                        System.out.println(rdCount());
                    } catch (RuntimeException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "showGraph":
                    try {
                        MeasurementGraphDTO dateValue = showGraph();
                        XChartGraphUtil.main(dateValue);
                        System.out.println("График температур сохранен в корне этого проекта \"Sample_Chart.png\"show");
                    } catch (RuntimeException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                default:
                    System.out.println("Введена неверная команда");
            }
            System.out.print(uiMessage);
            inputStr = new Scanner(System.in).nextLine();
        }
    }

    public static String reg(String nameOfSensor) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> jsonToSend = new HashMap<>();
        jsonToSend.put("name", nameOfSensor);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(jsonToSend);
        String url = "http://localhost:8080/sensors/registration";
        return restTemplate.postForObject(url, request, String.class);
    }

    public static SensorDTOList sensors() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(
                "http://localhost:8080/sensors", SensorDTOList.class);
    }

    public static String add(Double value, Boolean raining, String nameOfSensor) {
        SensorDTO sensorDTO = new SensorDTO();
        sensorDTO.setName(nameOfSensor);
        MeasurementDTO measurementDTO = new MeasurementDTO();
        measurementDTO.setValue(value);
        measurementDTO.setRaining(raining);
        measurementDTO.setSensor(sensorDTO);
        HttpEntity<MeasurementDTO> request = new HttpEntity<>(measurementDTO);
        return new RestTemplate().postForObject("http://localhost:8080/measurements/add", request, String.class);
    }

    public static String addRandoms(Double value, Boolean raining, String nameOfSensor) {
        SensorDTO sensorDTO = new SensorDTO();
        sensorDTO.setName(nameOfSensor);
        MeasurementDTO measurementDTO = new MeasurementDTO();
        measurementDTO.setValue(value);
        measurementDTO.setRaining(raining);
        measurementDTO.setSensor(sensorDTO);
        HttpEntity<MeasurementDTO> request = new HttpEntity<>(measurementDTO);
        return new RestTemplate().postForObject("http://localhost:8080/measurements/addwithrandomtime", request, String.class);
    }

    public static MeasurementDTOList get() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(
                "http://localhost:8080/measurements", MeasurementDTOList.class);
    }

    public static String rdCount() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(
                "http://localhost:8080/measurements/rainyDaysCount", String.class);
    }

    public static MeasurementGraphDTO showGraph() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(
                "http://localhost:8080/measurements/showGraph", MeasurementGraphDTO.class);
    }
}
