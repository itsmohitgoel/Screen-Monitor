package com.mohit.sleepmonitor.Graph;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;

import java.util.ArrayList;

/**
 * Created by Mohit on 22-10-2016.
 */
public class PieGraph {
    private ArrayList<PieSection> dataList;

    /**
     * Create and return a PieChart View
     * which can be inserted in any Layout Manager
     * @param context
     * @return GraphicalView representing pie chart
     */
    public View getView(Context context) {
        if (dataList == null || dataList.isEmpty()) {
            return null;
        }

        CategorySeries series = new CategorySeries("Pie Graph");
        DefaultRenderer renderer = new DefaultRenderer();

        for (PieSection section: dataList){
            series.add(section.getName(), section.getValue());
            renderer.addSeriesRenderer(section.getSeriesRenderer());
        }

        //Set properties of pie chart
        renderer.setChartTitle("Sleep Hours Distribution");
        renderer.setChartTitleTextSize(30);
        renderer.setLabelsTextSize(20);
        renderer.setLabelsColor(Color.BLACK);
        renderer.setLegendTextSize(20);
        renderer.setPanEnabled(false);

        GraphicalView pieChartView = ChartFactory.getPieChartView(context, series, renderer);

        return pieChartView;
    }

    public void setData(ArrayList<PieSection> list) {
        this.dataList = list;
    }
}
