package com.example.myapplication2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Graph extends AppCompatActivity {

    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        lineChart = findViewById(R.id.lineChart);
        Map<String, Integer> emotions = (Map<String, Integer>) getIntent().getSerializableExtra("hashMap");

        Button calButton = findViewById(R.id.calButton);
        calButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(), CalendarActivity.class);
                startIntent.putExtra("hashMap", (Serializable) emotions);
                startActivity(startIntent);
            }
        });

        List<Integer> xAXES = new ArrayList<>();
        List<Entry> yAXES1 = new ArrayList<>();
        List<Entry> yAXES2 = new ArrayList<>();
        List<Entry> yAXES3 = new ArrayList<>();
        List<Entry> yAXES4 = new ArrayList<>();
        List<Entry> yAXES5 = new ArrayList<>();
        List<Entry> yAXES6 = new ArrayList<>();

        List<List<String>> allEmotions = new ArrayList<>();
        allEmotions.add(new ArrayList<>(Arrays.asList("Happy", "Surprise"))); // 9.10 (Yellow, Orange)
        allEmotions.add(new ArrayList<>(Arrays.asList("Fear"))); // 9.11 (Purple)
        allEmotions.add(new ArrayList<>(Arrays.asList("Fear"))); // 9.12 (Purple)
        allEmotions.add(new ArrayList<>(Arrays.asList("Happy"))); // 9.13 (Yellow)
        allEmotions.add(new ArrayList<>(Arrays.asList("Angry", "Disgust"))); // 9.14 (Red, Green)
        allEmotions.add(new ArrayList<>(Arrays.asList("Angry", "Surprise"))); // 9.15 (Orange, Red)
        allEmotions.add(new ArrayList<>(Arrays.asList("Happy", "Surprise"))); // 9.16 (Orange, Yellow)
        allEmotions.add(new ArrayList<>(Arrays.asList("Fear"))); // 9.17 (Purple)
        allEmotions.add(new ArrayList<>(Arrays.asList("Happy", "Surprise"))); // 9.18 (Orange, Yellow)
        String[] typeEmotions = new String[]{"Happy", "Sad", "Angry", "Fear", "Surprise", "Disgust"};

        int numPoints = 9;
        for(int i = 0; i < numPoints; i++) {
            xAXES.add(i);
            if(allEmotions.get(i).contains("Happy"))
                yAXES1.add(new Entry(1, i));
            else
                yAXES1.add(new Entry(0, i));

            if(allEmotions.get(i).contains("Sad"))
                yAXES2.add(new Entry(1, i));
            else
                yAXES2.add(new Entry(0, i));

            if(allEmotions.get(i).contains("Angry"))
                yAXES3.add(new Entry(1, i));
            else
                yAXES3.add(new Entry(0, i));

            if(allEmotions.get(i).contains("Fear"))
                yAXES4.add(new Entry(1, i));
            else
                yAXES4.add(new Entry(0, i));

            if(allEmotions.get(i).contains("Surprises"))
                yAXES5.add(new Entry(1, i));
            else
                yAXES5.add(new Entry(0, i));

            if(allEmotions.get(i).contains("Disgust"))
                yAXES6.add(new Entry(1, i));
            else
                yAXES6.add(new Entry(0, i));

            //yAXES2.add(new Entry(Math.round(Math.random()), i));
            //yAXES3.add(new Entry(Math.round(Math.random()), i));
            //yAXES4.add(new Entry(Math.round(Math.random()), i));
            //yAXES5.add(new Entry(Math.round(Math.random()), i));
            //yAXES6.add(new Entry(Math.round(Math.random()), i));
        }

        if(emotions != null) {
            xAXES.add(9);
            yAXES1.add(new Entry(emotions.get("Happy"), 9));
            yAXES2.add(new Entry(emotions.get("Sad"), 9));
            yAXES3.add(new Entry(emotions.get("Angry"), 9));
            yAXES4.add(new Entry(emotions.get("Fear"), 9));
            yAXES5.add(new Entry(emotions.get("Surprise"), 9));
            yAXES6.add(new Entry(emotions.get("Disgust"), 9));
        }

        String[] xaxis = new String[xAXES.size()];
        for(int i = 0; i < xAXES.size(); i++)
            xaxis[i] = xAXES.get(i).toString();

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
        LineDataSet lineDataSet1 = new LineDataSet(yAXES1, "Happy");
        lineDataSet1.setDrawCircles(false);
        lineDataSet1.setColor(Color.YELLOW);

        LineDataSet lineDataSet2 = new LineDataSet(yAXES2, "Sad");
        lineDataSet2.setDrawCircles(false);
        lineDataSet2.setColor(Color.BLUE);

        LineDataSet lineDataSet3 = new LineDataSet(yAXES3, "Angry");
        lineDataSet3.setDrawCircles(false);
        lineDataSet3.setColor(Color.RED);

        LineDataSet lineDataSet4 = new LineDataSet(yAXES4, "Fear");
        lineDataSet4.setDrawCircles(false);
        lineDataSet4.setColor(Color.rgb(102, 0, 153));

        LineDataSet lineDataSet5 = new LineDataSet(yAXES5, "Surprise");
        lineDataSet5.setDrawCircles(false);
        lineDataSet5.setColor(Color.rgb(255, 102,0));

        LineDataSet lineDataSet6 = new LineDataSet(yAXES6, "Disgust");
        lineDataSet6.setDrawCircles(false);
        lineDataSet6.setColor(Color.GREEN);

        lineDataSets.add(lineDataSet1);
        lineDataSets.add(lineDataSet2);
        lineDataSets.add(lineDataSet3);
        lineDataSets.add(lineDataSet4);
        lineDataSets.add(lineDataSet5);
        lineDataSets.add(lineDataSet6);

        lineChart.setData(new LineData(xaxis, lineDataSets));
        lineChart.setVisibleXRangeMaximum(65f);
        lineChart.setDescription("Your emotional changes gathered by ThoughtSloth");
    }
}