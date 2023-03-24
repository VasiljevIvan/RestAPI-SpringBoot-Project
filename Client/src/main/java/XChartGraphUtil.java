import dto.MeasurementGraphDTO;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class XChartGraphUtil {
    public void drawGraph(MeasurementGraphDTO measurements) {

        XYChart chart = new XYChartBuilder().width(1920).height(1080)
                .xAxisTitle("Дата").yAxisTitle("Температура").build();

        chart.getStyler().setPlotContentSize(0.97);
        chart.getStyler().setDecimalPattern("#0.00 °С");
        chart.getStyler().setDatePattern("dd.MM.yy");

        List<Date> dates = measurements.getDates();
        List<Double> values = measurements.getValues();

        chart.addSeries("График температур", dates, values);

        try {
            BitmapEncoder.saveBitmap(chart, "./Sample_Chart", BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}