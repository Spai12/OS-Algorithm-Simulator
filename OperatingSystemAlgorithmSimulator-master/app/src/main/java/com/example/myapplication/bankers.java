package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class bankers extends AppCompatActivity {

    int[][] need, allocate, max, avail;
    int np, nr;
    EditText npEditText, nrEditText,allocationEditText, maxEditText, availableEditText;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banker);

        npEditText = findViewById(R.id.npEditText);
        nrEditText = findViewById(R.id.nrEditText);
        allocationEditText = findViewById(R.id.allocationEditText);
        maxEditText = findViewById(R.id.maxEditText);
        availableEditText = findViewById(R.id.availableEditText);
        result = findViewById(R.id.resultTextView);

        Button calculateButton = findViewById(R.id.calculateButton);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isSafe();
            }
        });
    }

    private void input() {
        np = Integer.parseInt(npEditText.getText().toString());
        nr = Integer.parseInt(nrEditText.getText().toString());
        need = new int[np][nr];
        max = new int[np][nr];
        allocate = new int[np][nr];
        avail = new int[1][nr];

        String allocationInput = allocationEditText.getText().toString();
        String[] allocationRows = allocationInput.split("\n");
        for (int i = 0; i < np; i++) {
            String[] allocationValues = allocationRows[i].split(" ");
            for (int j = 0; j < nr; j++) {
                allocate[i][j] = Integer.parseInt(allocationValues[j]);
            }
        }

        String maxInput = maxEditText.getText().toString();
        String[] maxRows = maxInput.split("\n");
        for (int i = 0; i < np; i++) {
            String[] maxValues = maxRows[i].split(" ");
            for (int j = 0; j < nr; j++) {
                max[i][j] = Integer.parseInt(maxValues[j]);
            }
        }

        String availableInput = availableEditText.getText().toString();
        String[] availableValues = availableInput.split(" ");
        for (int j = 0; j < nr; j++) {
            avail[0][j] = Integer.parseInt(availableValues[j]);
        }
    }

    private void calc_need() {
        for (int i = 0; i < np; i++)
            for (int j = 0; j < nr; j++)
                need[i][j] = max[i][j] - allocate[i][j];
    }

    private boolean check(int i) {
        for (int j = 0; j < nr; j++)
            if (avail[0][j] < need[i][j])
                return false;

        return true;
    }

    public void isSafe() {
        input();
        calc_need();
        boolean[] done = new boolean[np];
        int j = 0;

        StringBuilder output = new StringBuilder();

        while (j < np) {
            boolean allocated = false;
            for (int i = 0; i < np; i++)
                if (!done[i] && check(i)) {
                    for (int k = 0; k < nr; k++)
                        avail[0][k] = avail[0][k] - need[i][k] + max[i][k];
                    output.append("Allocated process : ").append(i).append("\n");
                    allocated = done[i] = true;
                    j++;
                }
            if (!allocated) break;
        }
        if (j == np)
            output.append("\nSafely allocated");
        else
            output.append("All processes can't be allocated safely");

        result.setText(output.toString());
    }
}
