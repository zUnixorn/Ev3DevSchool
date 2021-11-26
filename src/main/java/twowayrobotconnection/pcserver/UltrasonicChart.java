package twowayrobotconnection.pcserver;

import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;

import java.util.Arrays;
import java.util.LinkedList;

public class UltrasonicChart extends Thread {
    LinkedList<Float> data = new LinkedList<Float>(Arrays.asList());
    XYChart ultrasonic_chart;

    public void run() {
        for (int i = 0; i < 200; i++) {
            data.add((float) i);
        }

        ultrasonic_chart = new XYChartBuilder().width(800).height(800).theme(Styler.ChartTheme.Matlab).build();

        ultrasonic_chart.addSeries("values", data);

        while (true) {
            ultrasonic_chart.updateXYSeries("values", null, data, null);
        }
    }

    public void updateData(float dataValue) {
        data.pop();
        data.addFirst(dataValue);
    }
}
