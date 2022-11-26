package app.darts.stats.trackers;

import app.darts.DartThrow;
import app.darts.stats.LegDataProvider;
import app.darts.stats.ConcreteGameEventDispatcher;
import app.darts.stats.GameEventDispatcher;
import app.darts.stats.LegEvent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AverageTrackerTest {
    final GameEventDispatcher gameEventDispatcher = new ConcreteGameEventDispatcher();
    final AverageTracker tracker = new AverageTracker(gameEventDispatcher);

    @Test
    void averageCalculatedForNormalRound() {
        gameEventDispatcher.publish(LegEvent.CURRENT_PLAYER_THROW,
                LegDataProvider.getConstantProvider(501, 441,null, null));
        assertEquals(180, tracker.getAverage());

        gameEventDispatcher.publish(LegEvent.CURRENT_PLAYER_THROW,
                LegDataProvider.getConstantProvider(441, 421,null, null));
        assertEquals(120, tracker.getAverage());

        gameEventDispatcher.publish(LegEvent.CURRENT_PLAYER_THROW,
                LegDataProvider.getConstantProvider(421, 361,null, null));
        assertEquals(140, tracker.getAverage());

        gameEventDispatcher.publish(LegEvent.END_OF_CURRENT_PLAYER_ROUND,
                LegDataProvider.getConstantProvider(421, 361,null,
                        List.of(DartThrow.of("t20"), DartThrow.of("20"), DartThrow.of("t20"))));
        assertEquals(140, tracker.getAverage());
    }

    @Test
    void bustedThrowIsRecordedButReversedAtEndOfRound() {
        gameEventDispatcher.publish(LegEvent.CURRENT_PLAYER_THROW,
                LegDataProvider.getConstantProvider(501, 441,null, null));
        gameEventDispatcher.publish(LegEvent.CURRENT_PLAYER_THROW,
                LegDataProvider.getConstantProvider(441, 381,null, null));
        assertEquals(180, tracker.getAverage());

        gameEventDispatcher.publish(LegEvent.END_OF_CURRENT_PLAYER_ROUND,
                LegDataProvider.getConstantProvider(441, 501,null,
                        List.of(DartThrow.getNoScore(), DartThrow.getNoScore(), DartThrow.getNoThrow())));
        assertEquals(0, tracker.getAverage());
    }

    @Test
    void onlyActuallyThrownDartsAreCalculatedForEarlyFinish() {
        gameEventDispatcher.publish(LegEvent.CURRENT_PLAYER_THROW,
                LegDataProvider.getConstantProvider(36, 0,null, null));
        gameEventDispatcher.publish(LegEvent.END_OF_CURRENT_PLAYER_ROUND,
                LegDataProvider.getConstantProvider(36, 0,null,
                        List.of(DartThrow.of("d18"), DartThrow.getNoThrow(), DartThrow.getNoThrow())));
        assertEquals(108, tracker.getAverage());
    }

    @Test
    void trackerDoesNotReactToNonRelevantEvents() {
        gameEventDispatcher.publish(LegEvent.CURRENT_PLAYER_THROW,
                LegDataProvider.getConstantProvider(60, 0,null, null));
        assertEquals(180, tracker.getAverage());

        for (LegEvent event : LegEvent.values()) {
            if (event != LegEvent.CURRENT_PLAYER_THROW && event != LegEvent.END_OF_CURRENT_PLAYER_ROUND) {
                gameEventDispatcher.publish(event,
                        LegDataProvider.getConstantProvider(50, 0,null, null));
                assertEquals(180, tracker.getAverage());
            }
        }
    }

}