package me.hudsonclark.pouroverplus;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import me.hudsonclark.pouroverplus.view.TimerView;

public class InputFragment extends Fragment implements View.OnClickListener {

    private double numCups;
    private int coffeeGrams;
    private double coffeeTablespoons;

    private Button cupsUp;
    private Button cupsDown;
    private Button start;

    private TextView cupsText;
    private TextView coffeeGramsText;
    private TextView coffeeTablespoonsText;

    private int gramsRatio;
    private int tablespoonRatio;
    private int mlInCup;

    public InputFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.input, container, false);

        setUp(rootView);

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void setUp(View view) {
        cupsUp = (Button) view.findViewById(R.id.cups_up);
        cupsDown = (Button) view.findViewById(R.id.cups_down);
        start = (Button) view.findViewById(R.id.button_start);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

        setRatios();

        cupsUp.setOnClickListener(this);
        cupsDown.setOnClickListener(this);

        cupsText = (TextView) view.findViewById(R.id.cups);
        coffeeGramsText = (TextView) view.findViewById(R.id.coffee);
        coffeeTablespoonsText = (TextView) view.findViewById(R.id.coffee_tbsp);

        numCups = 1;
        update();

        cupsText.setText("" + numCups);
        coffeeGramsText.setText("" + coffeeGrams + "g");
        coffeeTablespoonsText.setText(("" + coffeeTablespoons + "tbsp"));

    }

    @Override
    public void onClick(View v) {
        setRatios();
        // This sets the max number of cups to five.
        if (v == cupsUp && numCups < 5) {
            numCups += 0.5;
            update();
        }
        // Use .5 here as that is the smallest amount of cups possible
        else if (v == cupsDown && numCups > .5) {
            numCups -= 0.5;
            update();
        }
    }

    private void update() {
        // Calculate grams.
        coffeeGrams = (int) Math.round(numCups * gramsRatio);

        // Calculate tablespoons.
        coffeeTablespoons = numCups * tablespoonRatio;

        // Update the text.
        coffeeGramsText.setText("" + coffeeGrams + "g");
        coffeeTablespoonsText.setText("" + coffeeTablespoons + "tbsp");
        cupsText.setText("" + numCups);
    }

    private void setRatios() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        try {
            gramsRatio = Integer.parseInt(prefs.getString("grams", "19"));
            tablespoonRatio = Integer.parseInt(prefs.getString("tablespoons", "2"));
            mlInCup = Integer.parseInt(prefs.getString("water", "300"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setRatios();
        update();
    }

    // Called when "let's go" button is clicked.
    private void start() {
        // Stop the animation before starting a new one.
        TimerView.stop = true;
        TimerFragment tf = (TimerFragment) getFragmentManager().findFragmentById(R.id.timer_frag);
        tf.startTimer(numCups, coffeeGrams);
    }

}
