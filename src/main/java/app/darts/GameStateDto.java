package app.darts;

import app.darts.stats.PlayerStatisticsDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public record GameStateDto(Optional<String> winner,
                           String nextToThrow,
                           int arrowLeftForNextPlayer,
                           Map<String, Map<String, Integer>> currentStanding,
                           GameStyleDto gameStyle,
                           List<PlayerStatisticsDto> playerStatistics) {

}
