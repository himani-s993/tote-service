package com.demo.tote.service;

import com.demo.tote.dto.ToteBetRequest;

import java.util.List;

public interface ToteService {

    List<String> getResultDividends(ToteBetRequest toteBetRequest);
}
