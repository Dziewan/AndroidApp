package com.example.dziewan.application.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.dziewan.application.model.ExtendedPlyta;
import com.example.dziewan.application.model.Plyta;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RestService extends AsyncTask<String, Void, ResponseEntity<?>> {

    Plyta plyta;
    byte[] obrazek;
    String operation;

    public RestService(String operation) {
        this.operation = operation;
    }

    public RestService(Plyta plyta, byte[] obrazek, String operation) {
        this.plyta = plyta;
        this.obrazek = obrazek;
        this.operation = operation;
    }

    @Override
    protected ResponseEntity<?> doInBackground(String... strings) {

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

            ResponseEntity<?> response = null;

            switch (operation) {
                case "getAll": {
                    HttpEntity<Plyta> entity = new HttpEntity<>(headers);
                    response = getAllSlabs(restTemplate, url, HttpMethod.GET, entity);
                    break;
                }
                case "getById": {
                    ExtendedPlyta extendedPlyta = new ExtendedPlyta(plyta, obrazek);
                    HttpEntity<ExtendedPlyta> entity = new HttpEntity<>(extendedPlyta, headers);
                    response = getSlabById(restTemplate, url, HttpMethod.GET, entity);
                    break;
                }
                case "addNew": {
                    ExtendedPlyta extendedPlyta = new ExtendedPlyta(plyta, obrazek);
                    HttpEntity<ExtendedPlyta> entity = new HttpEntity<>(extendedPlyta, headers);
                    response = addPlyta(restTemplate, url, HttpMethod.POST, entity);
                    break;
                }
            }

            return response;

        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            return null;
        }
    }

    public ResponseEntity<List<Plyta>> getAllSlabs(RestTemplate restTemplate, String url, HttpMethod method, HttpEntity entity) {
        ResponseEntity<Plyta[]> responseEntity = restTemplate.exchange(url, method, entity, Plyta[].class);
        return new ResponseEntity<List<Plyta>>(Arrays.asList(responseEntity.getBody()), HttpStatus.OK);
    }

    public ResponseEntity<ExtendedPlyta> getSlabById(RestTemplate restTemplate, String url, HttpMethod method, HttpEntity entity) {
        return restTemplate.exchange(url, method, entity, ExtendedPlyta.class);
    }

    public ResponseEntity<Plyta> addPlyta(RestTemplate restTemplate, String url, HttpMethod method, HttpEntity entity) {
        return restTemplate.exchange(url, method, entity, Plyta.class);
    }
}
