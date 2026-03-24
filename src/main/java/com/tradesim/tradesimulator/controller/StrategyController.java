package com.tradesim.tradesimulator.controller;

import com.tradesim.tradesimulator.service.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StrategyController {

    private final MarketFeed marketFeed;
    private final RandomWalkStrategy randomWalk;
    private final MeanReversionStrategy meanReversion;
    private final TrendFollowingStrategy trendFollowing;

    public StrategyController(MarketFeed marketFeed,
                               RandomWalkStrategy randomWalk,
                               MeanReversionStrategy meanReversion,
                               TrendFollowingStrategy trendFollowing) {
        this.marketFeed = marketFeed;
        this.randomWalk = randomWalk;
        this.meanReversion = meanReversion;
        this.trendFollowing = trendFollowing;
    }

    @GetMapping("/strategies")
    public Map<String, Object> getStrategies() {
        return Map.of("strategies", List.of("RANDOM_WALK", "MEAN_REVERSION", "TREND_FOLLOWING"));
    }

    @PostMapping("/strategy")
    public Map<String, String> setStrategy(@RequestBody Map<String, String> body) {
        switch (body.get("strategy").toUpperCase()) {
            case "RANDOM_WALK"     -> marketFeed.setStrategy(randomWalk);
            case "MEAN_REVERSION"  -> marketFeed.setStrategy(meanReversion);
            case "TREND_FOLLOWING" -> marketFeed.setStrategy(trendFollowing);
            default -> throw new IllegalArgumentException("Unknown: " + body.get("strategy"));
        }
        return Map.of("message", "Strategy set to " + body.get("strategy"));
    }
}