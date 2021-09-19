package com.example.myapplication2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private Map<String, Integer> emotionToColors;

    FirebaseDatabase database;
    String[] typeEmotions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = findViewById(R.id.calendarView);
        database = FirebaseDatabase.getInstance();

        ExtendedFloatingActionButton addEntry = (ExtendedFloatingActionButton) findViewById(R.id.addEntry);
        addEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(), Input.class);
                CalendarDay date = calendarView.getSelectedDate();
                String selected = Integer.toString(date.getYear()) + ',' +
                        Integer.toString((date.getMonth()+1)) + ',' +
                        Integer.toString(date.getDay());
                System.out.println("SELECTED DAY: " + selected);
                startIntent.putExtra("date", selected);
                startActivity(startIntent);
            }
        });

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(), Graph.class);
                CalendarDay date = calendarView.getSelectedDate();
                String selected = Integer.toString(date.getYear()) + ',' +
                        Integer.toString((date.getMonth()+1)) + ',' +
                        Integer.toString(date.getDay());
                startIntent.putExtra("date", selected);
                startActivity(startIntent);

            }
        });

        emotionToColors = new HashMap<>();
        emotionToColors.put("happy", Color.YELLOW);
        emotionToColors.put("sad", Color.BLUE);
        emotionToColors.put("angry", Color.RED);
        emotionToColors.put("fear", Color.rgb(102, 0, 153)); // purple
        emotionToColors.put("surprise", Color.rgb(255, 102,0)); // orange
        emotionToColors.put("disgust", Color.GREEN);

        int[] threeColors = {
                Color.rgb(0, 0, 255),
                Color.rgb(0, 255, 0),
                Color.rgb(255, 0, 0)};

        List<CalendarDay> tenEventDays = new ArrayList<>();
        for(int i = 10; i < 20; i++)
            tenEventDays.add(CalendarDay.from(2021,8, i));

        typeEmotions = new String[]{"happy", "sad", "angry", "fear", "surprise", "disgust"};

        updateEmotions(Calendar.getInstance().getTime().getMonth()+1);

        calendarView.setOnMonthChangedListener((widget, date) -> {
            try {
                Field currentMonthField = MaterialCalendarView.class.getDeclaredField("currentMonth");
                currentMonthField.setAccessible(true);
                int currentMonth = ((CalendarDay) currentMonthField.get(widget)).getMonth();

                updateEmotions(currentMonth+1);
                // Do something, currentMonth is between 1 and 12.

            } catch (NoSuchFieldException | IllegalAccessException e) {
                // Failed to get field value, maybe library was changed.
            }
        });


    }

    void updateEmotions(int month){
        System.out.println("IDENTIFIER: " + MainActivity.getIdentifier());

        DatabaseReference monthRef = database.getReference(MainActivity.getIdentifier())
                .child("2021," + Integer.toString(month));

        monthRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot date : snapshot.getChildren()){
                    if (date.exists()){

                    }
                    CalendarDay currDate = CalendarDay.from(2021, month-1, Integer.parseInt(date.getKey()));

                    HashMap<String,String>map = (HashMap<String, String>) date.getValue();

                    System.out.println("MONTH QUERY: " + map.get("emotion") + date.getKey() + ", " );

                    if (map.containsKey("emotion")){
                        String[] emotions = map.get("emotion").split(",");

                        ArrayList<Integer> currColours = new ArrayList<>();
                        for (int i=0; i<emotions.length; i++){
                            double intensity = Double.parseDouble(emotions[i]);
                            if (intensity >=0.3){
                                System.out.println("TYPE: " + typeEmotions[i]);
                                currColours.add(emotionToColors.get(typeEmotions[i]));
                            }
                        }
                        System.out.println("LENGTH: " + currColours.size());

                        int[] colourArray = new int[currColours.size()];
                        for (int i=0; i<colourArray.length; i++){
                            colourArray[i] = currColours.get(i);
                        }
//                    int[] currColours = new int[6];
//                    for (int i=0; i<ranges.length; i++){
//                        currColours[0] = emotionToColors.get(emotions[i]);
//                    }
                        calendarView.addDecorator(new Decorator(currDate, colourArray));
                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}

