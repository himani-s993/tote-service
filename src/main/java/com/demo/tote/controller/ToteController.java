package com.demo.tote.controller;

import com.demo.tote.dto.ToteBetRequest;
import com.demo.tote.service.ToteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ToteController {

    @Autowired
    private ToteService toteService;

    @GetMapping
    public void index() {}

    @PutMapping("/bets/dividends")
    public List<String> getDividends(@RequestBody ToteBetRequest toteBetRequest) {
        return toteService.getResultDividends(toteBetRequest);
    }
}
