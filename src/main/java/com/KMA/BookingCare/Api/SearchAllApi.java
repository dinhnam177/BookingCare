package com.KMA.BookingCare.Api;

import com.KMA.BookingCare.Service.SearchAllService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api")
public class SearchAllApi {

    private final Logger log = LoggerFactory.getLogger(SearchAllApi.class);

    @Autowired
    private SearchAllService searchAllService;

    @GetMapping("/search-all")
    public ResponseEntity<?> searchAllByFullText(@RequestParam("query") String query) {
        log.info("Request to searchAllByFullText with query: {}", query);
        return ResponseEntity.ok(searchAllService.searchAllByFullText(query));
    }
}
