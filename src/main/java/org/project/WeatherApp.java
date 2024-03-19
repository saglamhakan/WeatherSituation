package org.project;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class WeatherApp {

    private static final String API_KEY =  "20f8f31b3f0ce454d01fe8200ac3d921";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter city name: ");
        String city = scanner.nextLine();
        scanner.close();

        String requestUrl = String.format(BASE_URL, city, API_KEY);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUrl))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response.body());

        String weatherDescription = rootNode.at("/weather/0/description").asText();
        double tempInKelvin = rootNode.at("/main/temp").asDouble();
        double tempInCelsius = tempInKelvin - 273.15; // Convert Kelvin to Celsius

        System.out.println("Weather in " + city + ": " + weatherDescription);
        System.out.printf("Temperature: %.2f°C\n", tempInCelsius);

    }
}
