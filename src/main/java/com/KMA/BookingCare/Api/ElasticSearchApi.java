package com.KMA.BookingCare.Api;

import com.KMA.BookingCare.Service.HandbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/elasticsearch")
public class ElasticSearchApi {

    @Autowired
    private HandbookService handbookService;

    @GetMapping("/get-all-handbook")
    public ResponseEntity<?> getAll() {
        handbookService.getAll();
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
