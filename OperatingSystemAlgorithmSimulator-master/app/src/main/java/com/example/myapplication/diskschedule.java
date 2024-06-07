package com.example.myapplication;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import java.util.ArrayList;
import java.util.List;

public class diskschedule extends AppCompatActivity {

    EditText inputEditText;
    EditText initialHeadText;
    Button scheduleButton;
    LineChart lineChart;
    TextView result;
    View diskHeadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disk_sch);

        inputEditText = findViewById(R.id.inputEditText);
        initialHeadText = findViewById(R.id.initialHeadEditText); 
        scheduleButton = findViewById(R.id.scheduleButton);
        lineChart = findViewById(R.id.lineChart);
        result = findViewById(R.id.resultTextView);
        diskHeadView = findViewById(R.id.diskHeadView);

        setupChart();

        scheduleButton.setOnClickListener(v -> {
            String input = inputEditText.getText().toString();
            String[] inputArray = input.split(",");
            // Convert input string array to integer array
            int[] requests = new int[inputArray.length];
            for (int i = 0; i < inputArray.length; i++) {
                requests[i] = Integer.parseInt(inputArray[i].trim());
            }
            String initialHeadInput = initialHeadText.getText().toString();
            int initialHeadPosition;
            try {
                initialHeadPosition = Integer.parseInt(initialHeadInput);
            } catch (NumberFormatException e) {

                Toast.makeText(diskschedule.this, "Invalid initial head position", Toast.LENGTH_SHORT).show();
                return;
            }
            int totalHeadMovement = calculateTotalHeadMovement(requests, initialHeadPosition);
            String resultstr="Total head movement using FCFS: " + totalHeadMovement;
            result.setText(resultstr);
            animateDiskHead(requests);
            updateLineChart(requests);
        });
    }
    
    private int calculateTotalHeadMovement(int[] requests, int initialHeadPosition) {
        int totalHeadMovement = 0;
        int currentHeadPosition = initialHeadPosition;

        for (int request : requests) {
            totalHeadMovement += Math.abs(request - currentHeadPosition);
            currentHeadPosition = request;
        }

        return totalHeadMovement;
    }
    // Set up line chart
    private void setupChart() {
        lineChart.getDescription().setEnabled(false); // Disable chart description

        // Customize the X-axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP); // Set X-axis position to bottom
        xAxis.setDrawGridLines(false); // Disable grid lines on X-axis

        // Customize the Y-axis
        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setDrawGridLines(false); // Disable grid lines on Y-axis

        // Hide the right Y-axis
        lineChart.getAxisRight().setEnabled(false);
    }

    // Animate the disk head movement to a specific position
    private void animateDiskHeadToPosition(int position) {
        // Calculate the translationX value to move the disk head to the specified position

        // Create an ObjectAnimator to animate the translationX property of the disk head view
        final ObjectAnimator animator = ObjectAnimator.ofFloat(diskHeadView, "translationY", (float) position);
        animator.setDuration(1000); // Animation duration in milliseconds

        // Start the animation
        animator.start();
    }

    // Animate the disk head movement along with the disk requests
    private void animateDiskHead(final int[] requests) {
        final Handler handler = new Handler();
        for (int i = 0; i < requests.length; i++) {
            final int request = requests[i];
            handler.postDelayed(() -> animateDiskHeadToPosition(request), i * 1000L);
        }
    }
    // Update line chart with disk requests
    private void updateLineChart(int[] requests) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < requests.length; i++) {
            entries.add(new Entry(requests[i],-i));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Requests");
        dataSet.setDrawCircles(false); // Disable drawing circles at data points
        dataSet.setDrawValues(false); // Disable drawing values on data points

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.animateY(1500);
    }
}
