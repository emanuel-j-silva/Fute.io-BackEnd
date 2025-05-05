package com.io.fute.dto.draw;

import com.io.fute.dto.team.TeamInfo;

import java.time.LocalDateTime;
import java.util.List;

public record DrawInfo(LocalDateTime timestamp, List<TeamInfo> teams){
}
