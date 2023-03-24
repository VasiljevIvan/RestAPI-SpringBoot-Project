import com.google.gson.Gson;
import dto.*;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class Client {
    private RestTemplate restTemplate;

    public Client() {
        restTemplate = new RestTemplate();
    }

    public void run() {
        System.out.print(Constants.uiMessage);
        String inputStr = new Scanner(System.in).nextLine();
        while (!inputStr.equalsIgnoreCase("exit")) {
            try {
                switch (inputStr) {
                    case "reg" -> reg();
                    case "sensors" -> sensors();
                    case "add" -> add();
                    case "addRandoms" -> addRandoms();
                    case "get" -> get();
                    case "rdCount" -> rdCount();
                    case "showGraph" -> showGraph();
                    default -> System.out.println("Введена неверная команда");
                }
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
            System.out.print(Constants.uiMessage);
            inputStr = new Scanner(System.in).nextLine();
        }
    }

    private void reg() {
        System.out.print(Constants.nameMessage);
        String nameOfSensor = new Scanner(System.in).nextLine();
        Map<String, String> jsonToSend = new HashMap<>();
        jsonToSend.put("name", nameOfSensor);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(jsonToSend);
        String url = Constants.urlPrefix + "/sensors/registration";
        System.out.println(restTemplate.postForObject(url, request, String.class));
    }

    private void sensors() {
        System.out.println(new Gson().toJson(restTemplate.getForObject(Constants.urlPrefix + "/sensors", SensorDTOList.class)));
    }

    private void add() {
        System.out.print(Constants.nameMessage);
        String nameOfSensor = new Scanner(System.in).nextLine();
        System.out.print("Введите температуру (-100 до 100): ");
        String value = new Scanner(System.in).nextLine();
        System.out.print("Введите 0 если нет дождя и 1 если есть: ");
        boolean isRaining = new Scanner(System.in).nextLine().equals("1");
        HttpEntity<MeasurementDTO> request = createRequestToAdd(Double.parseDouble(value), isRaining, nameOfSensor);
        System.out.println(restTemplate.postForObject(Constants.urlPrefix + "/measurements/add", request, String.class));
    }

    private void addRandoms() {
        System.out.print(Constants.nameMessage);
        String nameOfSensor = new Scanner(System.in).nextLine();
        System.out.print("Введите количество случайных замеров для добавления: ");
        int n = Integer.parseInt(new Scanner(System.in).nextLine());
        for (int i = 0; i < n; i++) {
            HttpEntity<MeasurementDTO> request = createRequestToAdd(
                    (int) ((Math.random() * 20000) - 10000) / 100.0,
                    Math.random() < 0.5, nameOfSensor);
            System.out.println(restTemplate.postForObject(Constants.urlPrefix + "/measurements/addwithrandomtime", request, String.class));
        }
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

    private void get() {
        System.out.println(new Gson().toJson(restTemplate.getForObject(Constants.urlPrefix + "/measurements", MeasurementDTOList.class)));
    }

    private void rdCount() {
        System.out.println(restTemplate.getForObject(Constants.urlPrefix + "/measurements/rainyDaysCount", String.class));
    }

    private void showGraph() {
        new XChartGraphUtil().drawGraph(
                Objects.requireNonNull(
                        restTemplate.getForObject(Constants.urlPrefix + "/measurements/showGraph", MeasurementGraphDTO.class)));
        System.out.println("График температур сохранен в корне этого проекта \"Sample_Chart.png\"show");
    }
}