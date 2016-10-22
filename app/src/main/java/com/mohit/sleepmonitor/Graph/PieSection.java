package com.mohit.sleepmonitor.Graph;

import org.achartengine.renderer.SimpleSeriesRenderer;

/**
 * Item represented each section of pie chart
 * Created by Mohit on 22-10-2016.
 */
public class PieSection {
    private int value;
    private String name;
    private int color;

    public PieSection(int sectionValue, String sectionName, int sectionColor) {
        this.value = sectionValue;
        this.name = sectionName;
        this.color = sectionColor;
    }


    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public SimpleSeriesRenderer getSeriesRenderer() {
        SimpleSeriesRenderer r = new SimpleSeriesRenderer();
        r.setColor(color);
        return r;
    }
}
