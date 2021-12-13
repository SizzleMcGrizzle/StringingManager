package me.p3074098.stringingmanager.util;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.util.Duration;

import java.util.*;
import java.util.stream.Stream;

public class AnimationUtil {

    private static Map<Region, Timeline> timelineMap = new HashMap<>();

    public static void animateBorder(Region region) {

        if (timelineMap.containsKey(region)) {
            timelineMap.get(region).play();
            return;
        }

        Color[] colors = new Color[]{
                Color.DARKORANGE, Color.TOMATO, Color.DEEPPINK, Color.BLUEVIOLET,
                Color.STEELBLUE, Color.CORNFLOWERBLUE, Color.LIGHTSEAGREEN, Color.CHARTREUSE, Color.CRIMSON,
                Color.web("#6fba82")
        };

        final int incrementAmount = 150;
        int[] mills = {-1*incrementAmount};

        KeyFrame[] keyframes = Stream.iterate(0, i -> i+1)
                .limit(500)
                .map(i -> new LinearGradient(0, 0, 1, 1,
                        true,
                        CycleMethod.NO_CYCLE,
                        new Stop(0, colors[i%colors.length]), new Stop(1, colors[(i+1)%colors.length])))
                .map(lg -> new Border(new BorderStroke(lg, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(2))))
                .map(b -> new KeyFrame(Duration.millis(mills[0]+=incrementAmount), new KeyValue(region.borderProperty(), b, Interpolator.EASE_IN)))
                .toArray(KeyFrame[]::new);

        Timeline timeline = new Timeline(keyframes);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        timelineMap.put(region, timeline);
    }

    public static void stopAnimateBorder(Region r, String borderColor) {
        Optional.ofNullable(timelineMap.get(r)).ifPresent(t -> {
            t.stop();
            r.setStyle("-fx-border-color: " + borderColor);
        });
    }
}
