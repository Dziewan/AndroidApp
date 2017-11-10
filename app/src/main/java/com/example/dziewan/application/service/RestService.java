package com.example.dziewan.application.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.dziewan.application.model.Plyta;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class RestService extends AsyncTask<String, Void, ResponseEntity<Plyta[]>> {

    Plyta plyta;

    public RestService() {}

    public RestService(Plyta plyta) {
        this.plyta = plyta;
    }

    @Override
    protected void onPostExecute(ResponseEntity<Plyta[]> plytaResponseEntity) {
        HttpStatus status = plytaResponseEntity.getStatusCode();
        Plyta[] result = plytaResponseEntity.getBody();
    }

    @Override
    protected ResponseEntity<Plyta[]> doInBackground(String... strings) {

        final String url = strings[0];
        RestTemplate restTemplate = new RestTemplate();

        try {
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            HttpHeaders headers = new HttpHeaders();

            String auth = "user:user";
            String encodedAuth = Base64.encodeToString(auth.getBytes(), Base64.DEFAULT);
            String authHeader = "Basic " + new String(encodedAuth);
            headers.set("authorization", authHeader);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Plyta> entity = new HttpEntity<>(plyta, headers);
            if (plyta == null) {
                HttpEntity<Plyta> entity1 = new HttpEntity<>(headers);
                ResponseEntity<Plyta[]> response = restTemplate.exchange(url, HttpMethod.GET, entity1, Plyta[].class);
                return response;
            } else {
                Plyta[] response = {restTemplate.exchange(url, HttpMethod.POST, entity, Plyta.class).getBody()};
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            return null;
        }
    }
}
