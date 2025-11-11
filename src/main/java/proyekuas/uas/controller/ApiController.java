package proyekuas.uas.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
public class ApiController {

    @Value("${api.goapi.key}")
    private String goApiKey;

    private static final String GOAPI_BASE_URL = "https://api.goapi.io/regional";

    /**
     * Endpoint ini berfungsi sebagai proxy ke GoAPI.
     * Ia menerima jenis data ('provinsi', 'kota', dll.) dan parameter lainnya,
     * lalu meneruskannya ke GoAPI dengan menambahkan API key secara aman di sisi server.
     */
    @GetMapping("/api/regional")
    public ResponseEntity<String> getRegionalData(@RequestParam String type,
                                                  @RequestParam(required = false) Map<String, String> allParams) {
        
        RestTemplate restTemplate = new RestTemplate();
        
        // Membangun URL dengan parameter yang diberikan
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(GOAPI_BASE_URL + "/" + type);
        
        // Menambahkan semua parameter query KECUALI 'type'
        allParams.entrySet().stream()
            .filter(entry -> !entry.getKey().equals("type"))
            .forEach(entry -> builder.queryParam(entry.getKey(), entry.getValue()));

        String url = builder.toUriString();

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.set("X-API-KEY", goApiKey);
        org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);

        try {
            // Mengeksekusi permintaan dan mendapatkan respons sebagai String
            ResponseEntity<String> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            // Penanganan error sederhana
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error fetching data from external API.");
        }
    }
}