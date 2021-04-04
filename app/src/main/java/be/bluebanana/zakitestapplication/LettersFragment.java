package be.bluebanana.zakitestapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import be.bluebanana.zakisolver.LetterSolver;


public class LettersFragment extends Fragment implements View.OnClickListener {

    final LetterSolver solver = new LetterSolver();
    final ArrayList<Character> letters = new ArrayList<>();

    TextView results_tv;
    TextView input_tv;

    public LettersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the dictionary for the LetterSolver
        // You can change this by adding a new file to the "raw" resource directory and passing
        // the resource ID to the loadDictionary method.
        solver.loadDictionary(requireActivity(), R.raw.dictionary_nl);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_letters, container, false);

        results_tv = rootView.findViewById(R.id.tv_results);
        input_tv = rootView.findViewById(R.id.tv_input);

        rootView.findViewById(R.id.button_generate).setOnClickListener(this);
        rootView.findViewById(R.id.button_solve).setOnClickListener(this);

        return rootView;
    }

    public void solve (View v) {
        // set up the solver
        solver.setInput(letters);

        // Start the solver
        solver.solve(
                results -> {
                    Log.d("ZAKI", String.format("Found %d matches.", results.size()));

                    if (results.size() == 0) {
                        results_tv.append("No solutions found.");
                        return;
                    }
                    results.stream()
                            .limit(10)
                            .forEach(result -> results_tv.append(String.format("%s (%d)\n", result, result.length())));
                }
        );
    }

    public void generate(View v) {
        Random rand = new Random();
        Character[] vowels = {'a', 'e', 'i', 'o', 'u'};

        // clear the previous results
        results_tv.setText("");
        letters.clear();

        // Generate at least three vowels
        for (int i = 0; i<3; i++) {
            letters.add(vowels[rand.nextInt(vowels.length)]);
        }

        // Generate five more letters
        for (int i = 0; i<6; i++) {
            letters.add((char)(rand.nextInt(26)+97));
        }

        input_tv.setText(String.format("%s", letters));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_generate) generate(v);
        if (v.getId() == R.id.button_solve) solve(v);
    }
}