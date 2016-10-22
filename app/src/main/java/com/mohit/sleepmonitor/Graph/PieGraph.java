package com.mohit.sleepmonitor.Graph;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

/**
 * Created by Mohit on 22-10-2016.
 */
public class PieGraph {

    public Intent getIntent(Context context) {
        int[] values = {1, 2, 3, 4, 5};

        CategorySeries series = new CategorySeries("Pie Graph");
        int i = 0;
        for (int value : values) {
            series.add("Section " + ++i, value);
        }

        int[] colors = new int[]{Color.BLUE, Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.CYAN};

        DefaultRenderer renderer = new DefaultRenderer();
        for (int color : colors) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            renderer.addSeriesRenderer(r);
        }

        Intent intent = ChartFactory.getPieChartIntent(context, series, renderer, "Pie");

        return intent;
    }

    public View getView(Context context) {
        int[] values = {1, 2, 3, 4, 5};

        CategorySeries series = new CategorySeries("Pie Graph");
        int i = 0;
        for (int value : values) {
            series.add("Section " + ++i, value);
        }

        int[] colors = new int[]{Color.BLUE, Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.RED};

        DefaultRenderer renderer = new DefaultRenderer();
        for (int color : colors) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            renderer.addSeriesRenderer(r);
        }

        //Set properties of pie chart
        renderer.setChartTitle("Sleep Hours Distribution");
        renderer.setChartTitleTextSize(30);
        renderer.setLabelsTextSize(20);
        renderer.setLabelsColor(Color.BLACK);
        renderer.setLegendTextSize(20);
        renderer.setZoomButtonsVisible(true);
        renderer.setScale(2);

        GraphicalView pieChartView = ChartFactory.getPieChartView(context, series, renderer);

        return pieChartView;
    }
}
