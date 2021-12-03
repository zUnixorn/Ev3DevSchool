package twowayrobotconnection.pcserver;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;


public class UltrasonicChart extends Thread {
    LinkedList<Float> data = new LinkedList<Float>(Arrays.asList());
    XYChart ultrasonicChart;
    LinkedList<Float> xAxis = new LinkedList<Float>(Arrays.asList());
    SwingWrapper<XYChart> swingUC;

    UltrasonicChart() {
        super();

        for (int i = 0; i < 200; i++) {
            data.add(0.0f);
            xAxis.add((float) i);
        }

        ultrasonicChart = new XYChartBuilder().width(16 * 100).height(9 * 100).theme(Styler.ChartTheme.Matlab).build();

        swingUC = new SwingWrapper<XYChart>(ultrasonicChart);

        ultrasonicChart.addSeries("values", xAxis, data);

        swingUC.displayChart();
    }

    public void run() {
        while (true) {
            try {
                ultrasonicChart.updateXYSeries("values", xAxis, data, null);
                swingUC.repaintChart();
                Thread.sleep(100, 0);
            } catch (ConcurrentModificationException | IllegalArgumentException | NullPointerException | InterruptedException ignore) {}
        }
    }

    public void updateData(float dataValue) {
        System.out.println(dataValue);
        data.removeFirst();
        data.add(dataValue);
    }
}
