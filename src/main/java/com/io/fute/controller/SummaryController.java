package com.io.fute.controller;

import com.io.fute.dto.draw.DrawInfo;
import com.io.fute.dto.summary.SummaryInfo;
import com.io.fute.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/summary")
public class SummaryController {
    private SummaryService summaryService;

    @Autowired
    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @GetMapping
    public ResponseEntity<SummaryInfo> fetchUserSummary(
            @AuthenticationPrincipal(expression = "id") UUID userId){

        return ResponseEntity.status(HttpStatus.OK)
                .body(summaryService.getUserSummary(userId));
    }
}
