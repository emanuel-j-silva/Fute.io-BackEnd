package com.io.fute.service;

import com.io.fute.dto.summary.SummaryInfo;
import com.io.fute.entity.draw.Draw;
import com.io.fute.repository.DrawRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class SummaryService {
    private DrawRepository drawRepository;

    @Autowired
    public SummaryService(DrawRepository drawRepository) {
        this.drawRepository = drawRepository;
    }

    public SummaryInfo getUserSummary(UUID userId){
        var optionalLastDraw = drawRepository.findFirstByGroupUserIdOrderByDateDesc(userId);
        Long totalDraws = drawRepository.countByUserId(userId);

        if (optionalLastDraw.isEmpty()) {
            return new SummaryInfo("", "", totalDraws);
        }

        Draw lastDraw = optionalLastDraw.get();
        return new SummaryInfo(lastDraw.getDate()
                .format(DateTimeFormatter.ofPattern("dd/MM/yy")),
                lastDraw.getGroupName(), totalDraws);
    }
}
