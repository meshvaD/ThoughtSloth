package com.example.myapplication2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

//        Map<String, Integer> emotions = (Map<String, Integer>) getIntent().getSerializableExtra("hashMap");
//        List<List<String>> allEmotions = new ArrayList<>();
//        allEmotions.add(new ArrayList<>(Arrays.asList("Happy", "Surprise"))); // 9.10 (Yellow, Orange)
//        allEmotions.add(new ArrayList<>(Arrays.asList("Fear"))); // 9.11 (Purple)
//        allEmotions.add(new ArrayList<>(Arrays.asList("Fear"))); // 9.12 (Purple)
//        allEmotions.add(new ArrayList<>(Arrays.asList("Happy"))); // 9.13 (Yellow)
//        allEmotions.add(new ArrayList<>(Arrays.asList("Angry", "Disgust"))); // 9.14 (Red, Green)
//        allEmotions.add(new ArrayList<>(Arrays.asList("Angry", "Surprise"))); // 9.15 (Orange, Red)
//        allEmotions.add(new ArrayList<>(Arrays.asList("Happy", "Surprise"))); // 9.16 (Orange, Yellow)
//        allEmotions.add(new ArrayList<>(Arrays.asList("Fear"))); // 9.17 (Purple)
//        allEmotions.add(new ArrayList<>(Arrays.asList("Happy", "Surprise"))); // 9.18 (Orange, Yellow)
        String[] typeEmotions = new String[]{"Happy", "Sad", "Angry", "Fear", "Surprise", "Disgust"};

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


//        List<String> temp = new ArrayList<>();
//        if(emotions != null) {
//            for(int i = 0; i < typeEmotions.length; i++) {
//                if(emotions.get(typeEmotions[i]) == 1)
//                    temp.add(typeEmotions[i]);
//            }
//        }
//        allEmotions.add(temp);
//
//        for(int i = 0; i < tenEventDays.size(); i++) {
//            CalendarDay currDay = tenEventDays.get(i);
//            List<String> currEmotions = allEmotions.get(i);
//            int[] currColors = new int[currEmotions.size()];
//            for(int j = 0; j < currEmotions.size(); j++)
//                currColors[j] = emotionToColors.get(currEmotions.get(j));
//
//            calendarView.addDecorator(new Decorator(currDay, currColors));
//        }

        List<CalendarDay> threeEventDays = new ArrayList<>();
        threeEventDays.add(CalendarDay.today());
        threeEventDays.add(CalendarDay.from(2021, 9, 11));
        threeEventDays.add( CalendarDay.from(2021, 9,10));

        List<CalendarDay> fourEventDays = new ArrayList<>();
        threeEventDays.add(CalendarDay.from(2021, 8, 9));
        threeEventDays.add(CalendarDay.from(2021, 8, 11));
        threeEventDays.add( CalendarDay.from(2021, 8,10));

    }

    void updateEmotions(int month){

        DatabaseReference monthRef = database.getReference(MainActivity.getIdentifier())
                .child("2021," + Integer.toString(month));

        monthRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot date : snapshot.getChildren()){
                    CalendarDay currDate = CalendarDay.from(2021, month-1, Integer.parseInt(date.getKey()));

                    HashMap<String,String>map = (HashMap<String, String>) date.getValue();

                    System.out.println("MONTH QUERY: " + map.get("emotion") + date.getKey() + ", " );

                    String[] emotions = map.get("emotion").split(",");
                    int[] currColours = new int[emotions.length];
                    for (int i=0; i<emotions.length; i++){
                        currColours[0] = emotionToColors.get(emotions[i]);
                    }
                    calendarView.addDecorator(new Decorator(currDate, currColours));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}

