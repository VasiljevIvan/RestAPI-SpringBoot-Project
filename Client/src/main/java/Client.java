import com.google.gson.Gson;
import dto.*;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client {
    private RestTemplate restTemplate;

    public Client() {
        restTemplate = new RestTemplate();
    }

    public void run() {
        String nameOfSensor;
        System.out.print(Constants.uiMessage);
        String inputStr = new Scanner(System.in).nextLine();
        while (!inputStr.equalsIgnoreCase("exit")) {
            try {
                switch (inputStr) {
                    case "reg" -> {
                        System.out.print(Constants.nameMessage);
                        nameOfSensor = new Scanner(System.in).nextLine();
                        System.out.println(reg(nameOfSensor));
                    }
                    case "sensors" -> {
                        SensorDTOList sensors = sensors();
                        System.out.println(new Gson().toJson(sensors));
                    }
                    case "add" -> {
                        System.out.print(Constants.nameMessage);
                        nameOfSensor = new Scanner(System.in).nextLine();
                        System.out.print("Введите температуру (-100 до 100): ");
                        String value = new Scanner(System.in).nextLine();
                        System.out.print("Введите 0 если нет дождя и 1 если есть: ");
                        boolean isRaining = new Scanner(System.in).nextLine().equals("1");
                        System.out.println(add(Double.parseDouble(value), isRaining, nameOfSensor));
                    }
                    case "addRandoms" -> {
                        System.out.print(Constants.nameMessage);
                        nameOfSensor = new Scanner(System.in).nextLine();
                        System.out.print("Введите количество случайных замеров для добавления: ");
                        int n = Integer.parseInt(new Scanner(System.in).nextLine());
                        for (int i = 0; i < n; i++)
                            System.out.println(
                                    addRandoms((int) ((Math.random() * 20000) - 10000) / 100.0,
                                            Math.random() < 0.5,
                                            nameOfSensor));
                    }
                    case "get" -> {
                        MeasurementDTOList measurements = get();
                        System.out.println(new Gson().toJson(measurements));
                    }
                    case "rdCount" -> System.out.println(rdCount());
                    case "showGraph" -> {
                        MeasurementGraphDTO dateValue = showGraph();
                        new XChartGraphUtil().drawGraph(dateValue);
                        System.out.println("График температур сохранен в корне этого проекта \"Sample_Chart.png\"show");
                    }
                    default -> System.out.println("Введена неверная команда");
                }
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
            System.out.print(Constants.uiMessage);
            inputStr = new Scanner(System.in).nextLine();
        }
    }

    private String reg(String nameOfSensor) {
        Map<String, String> jsonToSend = new HashMap<>();
        jsonToSend.put("name", nameOfSensor);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(jsonToSend);
        String url = Constants.urlPrefix + "/sensors/registration";
        return restTemplate.postForObject(url, request, String.class);
    }

    private SensorDTOList sensors() {
        return restTemplate.getForObject(Constants.urlPrefix + "/sensors", SensorDTOList.class);
    }

    private String add(Double value, Boolean raining, String nameOfSensor) {
        HttpEntity<MeasurementDTO> request = createRequestToAdd(value, raining, nameOfSensor);
        return restTemplate.postForObject(Constants.urlPrefix + "/measurements/add", request, String.class);
    }

    private String addRandoms(Double value, Boolean raining, String nameOfSensor) {
        HttpEntity<MeasurementDTO> request = createRequestToAdd(value, raining, nameOfSensor);
        return restTemplate.postForObject(Constants.urlPrefix + "/measurements/addwithrandomtime", request, String.class);
    }

    private HttpEntity<MeasurementDTO> createRequestToAdd(Double value, Boolean raining, String nameOfSensor) {
        SensorDTO sensorDTO = new SensorDTO();
        sensorDTO.setName(nameOfSensor);
        MeasurementDTO measurementDTO = new MeasurementDTO();
        measurementDTO.setValue(value);
        measurementDTO.setRaining(raining);
        measurementDTO.setSensor(sensorDTO);
        return new HttpEntity<>(measurementDTO);
    }

    private MeasurementDTOList get() {
        return restTemplate.getForObject(Constants.urlPrefix + "/measurements", MeasurementDTOList.class);
    }

    private String rdCount() {
        return restTemplate.getForObject(Constants.urlPrefix + "/measurements/rainyDaysCount", String.class);
    }

    private MeasurementGraphDTO showGraph() {
        return restTemplate.getForObject(Constants.urlPrefix + "/measurements/showGraph", MeasurementGraphDTO.class);
    }
}
