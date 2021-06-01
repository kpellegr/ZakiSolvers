package be.bluebanana.zakitestapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Random;

import be.bluebanana.zakisolver.NumberSolver;


public class NumbersFragment extends Fragment implements View.OnClickListener {

    final NumberSolver solver = new NumberSolver();
    final ArrayList<Integer> numbers = new ArrayList<>();
    int target = 0;

    TextView results_tv;
    TextView input_tv;

    public NumbersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_numbers, container, false);

        results_tv = rootView.findViewById(R.id.tv_results);
        input_tv = rootView.findViewById(R.id.tv_input);

        rootView.findViewById(R.id.button_generate).setOnClickListener(this);
        rootView.findViewById(R.id.button_solve).setOnClickListener(this);

        return rootView;
    }

    public void solve (View v) {
        // set up the solver
        solver.setInput(numbers, target,
                results -> {
                    Log.d("ZAKI", String.format("Found %d matches.", results.size()));

                    if (results.size() == 0) {
                        results_tv.append("No solutions found.");
                        return;
                    }
                    results.stream()
                            .limit(10)
                            .forEach(result -> Log.d("TAG", String.format("%s\n", result)));
                });

        // Start the solver
        new Thread(solver).start();
    }

    public void generate(View v) {
        Random rand = new Random();
        Integer[] large = {10, 25, 50, 100};

        // clear the previous results
        results_tv.setText("");
        numbers.clear();

        // Generate two large numbers
        for (int i = 0; i<2; i++) {
            numbers.add(large[rand.nextInt(large.length)]);
        }

        // Generate four more numbers
        for (int i = 0; i<4; i++) {
            numbers.add(rand.nextInt(9) + 1);
        }

        // generate target between 100 and 999
        target = rand.nextInt(900) + 100;

        input_tv.setText(String.format("%s -> %d", numbers, target));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_generate) generate(v);
        if (v.getId() == R.id.button_solve) solve(v);
    }
}