package com.example.myapplication.ui.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.myapplication.R;
import android.widget.Switch;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch degreeSwitch;
    private EditText raNumberInput;
    private Spinner raHourSpinner;
    private Spinner raminSpinner;
    private Spinner rasecSpinner;
    private Button dateButton;
    private Button timePickerButton;
    private Button enddateButton;
    private Button endtimePickerButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textENDT;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        Button findStarsButton = binding.getRoot().findViewById(R.id.find_stars_button);

        // Set an OnClickListener to handle the button click
        findStarsButton.setOnClickListener(v -> {
            // Use BottomNavigationView to select the "Dashboard" tab
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
            bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);

            // Also make sure the "Dashboard" fragment is displayed properly
            // This will ensure that when you navigate to the Dashboard tab, it is fully loaded
            Navigation.findNavController(v).navigate(R.id.action_home_to_dashboard);
        });

        Spinner spinner_hour = binding.getRoot().findViewById(R.id.ra_hour_spinner);
        Spinner spinner_min = binding.getRoot().findViewById(R.id.ra_min_spinner);
        Spinner spinner_sec = binding.getRoot().findViewById(R.id.ra_sec_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.spinner_items_hour, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_hour.setAdapter(adapter);
        spinner_hour.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedOption = (String) parentView.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parentView) {
                // Optionally handle when no item is selected
            }
        });

        ArrayAdapter<CharSequence> adapter_min = ArrayAdapter.createFromResource(getContext(),
                R.array.spinner_items_min, android.R.layout.simple_spinner_item);
        adapter_min.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_min.setAdapter(adapter_min);
        spinner_min.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedOption = (String) parentView.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parentView) {
                // Optionally handle when no item is selected
            }
        });

        ArrayAdapter<CharSequence> adapter_sec = ArrayAdapter.createFromResource(getContext(),
                R.array.spinner_items_sec, android.R.layout.simple_spinner_item);
        adapter_sec.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sec.setAdapter(adapter_sec);
        spinner_sec.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedOption = (String) parentView.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parentView) {
                // Optionally handle when no item is selected
            }
        });

// This is the degree or hh:mm:ss switch code
        degreeSwitch = root.findViewById(R.id.degree_switch);
        raNumberInput = root.findViewById(R.id.RA_Number_Input);
        raHourSpinner = root.findViewById(R.id.ra_hour_spinner);
        raminSpinner = root.findViewById(R.id.ra_min_spinner);
        rasecSpinner = root.findViewById(R.id.ra_sec_spinner);
        TextView textRA = root.findViewById(R.id.text_RA);
        raNumberInput.setVisibility(View.GONE);
        degreeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                raNumberInput.setVisibility(View.VISIBLE);
                raHourSpinner.setVisibility(View.GONE);
                raminSpinner.setVisibility(View.GONE);
                rasecSpinner.setVisibility(View.GONE);
                textRA.setText("Input RA (0-360°)");
            } else {
                raNumberInput.setVisibility(View.GONE);
                raHourSpinner.setVisibility(View.VISIBLE);
                raminSpinner.setVisibility(View.VISIBLE);
                rasecSpinner.setVisibility(View.VISIBLE);
                textRA.setText("Input RA");
            }
        });
// End

        EditText raNumberInput = binding.RANumberInput;
        raNumberInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // No need to implement this for your use case
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // No need to implement this for your use case
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable editable) {
                String inputText = editable.toString();

                if (!inputText.isEmpty()) {
                    try {
                        double value = Double.parseDouble(inputText);
                        if (value < 0) {
                            raNumberInput.setText("0");
                        } else if (value > 360) {
                            raNumberInput.setText("360");
                        } else {
                            String str = Double.toString(value);
                            if (str.contains(".")) {
                                String[] parts = str.split("\\.");
                                int numdecimals = parts[1].length();
                                if (numdecimals > 3) {
                                    double truncatedValue = (double) Math.floor(value * 1000) / 1000.0;
                                    raNumberInput.setText(String.valueOf(truncatedValue));
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        raNumberInput.setText("0");
                    }
                    raNumberInput.setSelection(raNumberInput.getText().length());
                }
            }
        });

        // Repeat this for DEC and Radius inputs...

        dateButton = root.findViewById(R.id.datePickerButton);
        dateButton.setOnClickListener(v -> openDatePicker(v));
        timePickerButton = root.findViewById(R.id.timePickerButton);
        timePickerButton.setOnClickListener(this::openTimePicker);
        enddateButton = root.findViewById(R.id.enddatePickerButton);
        enddateButton.setOnClickListener(v -> openDatePicker(v));
        endtimePickerButton = root.findViewById(R.id.endtimePickerButton);
        endtimePickerButton.setOnClickListener(this::openTimePicker);

        return root;
    }


    public void openTimePicker(View view) {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create the TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getActivity(),
                (timePicker, hourOfDay, minute1) -> {
                    // Format time as 12-hour format with AM/PM
                    String amPm = (hourOfDay >= 12) ? "PM" : "AM";
                    int hour12 = hourOfDay % 12;
                    if (hour12 == 0) hour12 = 12; // 12 AM/PM
                    String time = String.format("%02d:%02d %s", hour12, minute1, amPm);

                    // Set the formatted time on the button
                    if (view.getId() == R.id.timePickerButton) {
                        timePickerButton.setText(time);
                    } else if (view.getId() == R.id.endtimePickerButton) {
                        endtimePickerButton.setText(time);
                    }
                },
                hour,
                minute,
                false // false for 12-hour format, true for 24-hour format
        );
        timePickerDialog.show();
    }

    public void openDatePicker(View view) {
        // Initialize the DatePickerDialog
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                // Adjust for 0-based month index (January is 0)
                selectedMonth = selectedMonth + 1;

                // Format the date string
                String date = makeDateString(selectedDay, selectedMonth, selectedYear);

                // Update the Button's text with the selected date
                if (view.getId() == R.id.datePickerButton) {
                    dateButton.setText(date);
                } else if (view.getId() == R.id.enddatePickerButton) {
                    enddateButton.setText(date);
                }
            }
        };

        // Create the DatePickerDialog instance
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(), // Context (activity)
                AlertDialog.THEME_HOLO_LIGHT, // Theme
                dateSetListener, // Listener for when the date is set
                year, // Initial year
                month, // Initial month
                day  // Initial day
        );

        // Optionally set the range of years (unbounded or custom range)
        int minYear = 1900; // Minimum year (e.g., 1900)
        int maxYear = 2100; // Maximum year (e.g., 2100)
        datePickerDialog.getDatePicker().setMinDate(new GregorianCalendar(minYear, 0, 1).getTimeInMillis());  // Set minimum date
        datePickerDialog.getDatePicker().setMaxDate(new GregorianCalendar(maxYear, 11, 31).getTimeInMillis());  // Set maximum date

        // Show the date picker dialog
        datePickerDialog.show();
    }

    // Helper function to format the date as a string (e.g., JAN 01 2020)
    private String makeDateString(int day, int month, int year) {
        return String.format("%s %02d %d", getMonthString(month), day, year);
    }

    // Helper function to get the month name (e.g., "JAN", "FEB", etc.)
    private String getMonthString(int month) {
        String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        return months[month - 1];  // Subtract 1 because month is 0-based
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
