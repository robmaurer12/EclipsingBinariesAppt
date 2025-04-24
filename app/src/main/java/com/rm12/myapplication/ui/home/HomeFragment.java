package com.rm12.myapplication.ui.home;

import static com.rm12.myapplication.MyStarsClass.getJulianDate;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.rm12.myapplication.MyStarsClass;
import com.rm12.myapplication.Stars;
import com.rm12.myapplication.TimeClasses;
import com.rm12.myapplication.databinding.FragmentHomeBinding;
import com.rm12.myapplication.ui.dashboard.DashboardFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rm12.myapplication.R;
import android.widget.Switch;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch degreeSwitch, decdegreeSwitch;
    private EditText raNumberInput, radiusNumberInput, decNumberInput;
    private Spinner raHourSpinner, raminSpinner, rasecSpinner, decdegspinner,decminspinner,decsecspinner;
    private Button dateButton, timePickerButton, enddateButton, endtimePickerButton;

    @SuppressLint("DefaultLocale")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final TextView textView = binding.textENDT;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d %s",
                (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) % 12 == 0) ? 12 : Calendar.getInstance().get(Calendar.HOUR_OF_DAY) % 12,
                Calendar.getInstance().get(Calendar.MINUTE),
                (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 12) ? "PM" : "AM");
        String currentDate = TimeClasses.makeDateString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                Calendar.getInstance().get(Calendar.MONTH) + 1,
                Calendar.getInstance().get(Calendar.YEAR));

        if (timePickerButton == null || timePickerButton.getText().toString().isEmpty()||"Set Time".equals(timePickerButton.getText().toString())){
            timePickerButton = binding.timePickerButton;
            timePickerButton.setText(time);  // Set it only if it's null or empty
        }
        time = (String) timePickerButton.getText();
        timePickerButton = binding.timePickerButton;
        timePickerButton.setText(time);
        timePickerButton.setOnClickListener(this::openTimePicker);

        if (dateButton == null || dateButton.getText().toString().isEmpty()||"Set Date".equals(dateButton.getText().toString())){
            dateButton = binding.datePickerButton;
            dateButton.setText(currentDate);  // Set it only if it's null or empty
        }
        currentDate = (String) dateButton.getText();
        dateButton = binding.datePickerButton;
        dateButton.setText(currentDate);
        dateButton.setOnClickListener(this::openDatePicker);

        radiusNumberInput = binding.RADIUSNumberInput;  // Set it only if it's null or empty

        decNumberInput = binding.DECNumberInput;

        if (enddateButton == null || enddateButton.getText().toString().isEmpty()||"Set Date".equals(enddateButton.getText().toString())){
            enddateButton = binding.enddatePickerButton;
        }
        String endDate = (String) enddateButton.getText();
        enddateButton = binding.enddatePickerButton;
        enddateButton.setText(endDate);
        enddateButton.setOnClickListener(this::openDatePicker);

        if (endtimePickerButton == null || endtimePickerButton.getText().toString().isEmpty()||"Set Time".equals(endtimePickerButton.getText().toString())){
            endtimePickerButton = binding.endtimePickerButton;
        }
        String endtime = (String) endtimePickerButton.getText();
        endtimePickerButton = binding.endtimePickerButton;
        endtimePickerButton.setText(endtime);
        endtimePickerButton.setOnClickListener(this::openTimePicker);

        //Find Stars button click
        Button findStarsButton = binding.getRoot().findViewById(R.id.find_stars_button);
        findStarsButton.setOnClickListener(v -> {
            String radiusValue = radiusNumberInput.getText().toString();
            String decValue = decNumberInput.getText().toString();
            String hour = raHourSpinner.getSelectedItem().toString(); // If the degreeSwitch is unchecked, use the values from the spinners
            String minute = raminSpinner.getSelectedItem().toString();
            String second = rasecSpinner.getSelectedItem().toString();
            String decdeg = decdegspinner.getSelectedItem().toString(); // If the degreeSwitch is unchecked, use the values from the spinners
            String decminute = decminspinner.getSelectedItem().toString();
            String decsecond = decsecspinner.getSelectedItem().toString();
            String raValue = raNumberInput.getText().toString();
            String Start_time_hours = timePickerButton.getText().toString();
            String Start_time_days = dateButton.getText().toString();
            String Start_time = Start_time_hours + " " + Start_time_days;
            String end_time_hours = endtimePickerButton.getText().toString();
            String end_time_days = enddateButton.getText().toString();
            String end_time = end_time_hours + " " + end_time_days;
            String raValue2 = hour + ":" + minute + ":" + second; // Create the RA value from hour, minute, and second
            String decValue2 = decdeg + ":" + decminute + ":" + decsecond;
            double raValuedecimal = 0;
            double decValuedecimal = 0;
            if (radiusValue.isEmpty()) {radiusValue = "0";}// If it's empty, set to 0
            if (decValue.isEmpty()) {
                decValue = "0";
            } else {
                if (decdegreeSwitch.isChecked()) {
                    // If the degreeSwitch is checked, use the value from raNumberInput
                    decValue = decNumberInput.getText().toString();
                    decValuedecimal = Double.parseDouble(decValue);
                } else {
                    String degString = decdeg.replace("°", "");
                    double decimaldeg = Double.parseDouble(degString);
                    String decminString = decminute.replace("m", "");
                    double decimalmindec = Double.parseDouble(decminString);
                    String decsecString = decsecond.replace("s", "");
                    double decimalsecdec = Double.parseDouble(decsecString);
                    if (decimaldeg < 0) {
                        decValuedecimal = decimaldeg - decimalmindec / 60 - decimalsecdec / 3600;
                    } else {
                        decValuedecimal = decimaldeg + decimalmindec / 60 + decimalsecdec / 3600;
                    }
                }
            }
            if (raValue.isEmpty()) {
                raValue = "0";  // If it's empty, set to 0
            } else {
                if (degreeSwitch.isChecked()) {
                    // If the degreeSwitch is checked, use the value from raNumberInput
                    raValue = raNumberInput.getText().toString();
                    raValuedecimal = Double.parseDouble(raValue);
                } else {
                    String hourString = hour.replace("h", "");
                    double decimalhour = Double.parseDouble(hourString);
                    String minString = minute.replace("m", "");
                    double decimalmin = Double.parseDouble(minString);
                    String secString = second.replace("s", "");
                    double decimalsec = Double.parseDouble(secString);
                    raValuedecimal = 360 * (decimalhour / 24 + decimalmin / 1440 + decimalsec / 86400);
                }
            }
            MyStarsClass.runStarsLogic(raValuedecimal, decValuedecimal, radiusValue, Start_time, end_time);
            if (end_time.contains("Set Time") || end_time.contains("Set Date")) {
                end_time = "01:00 AM JAN 01 2050"; // Set to "hi" if either condition is met
            }
            List<Integer> starsWithinRadius = MyStarsClass.findIndexMax(Double.parseDouble(radiusValue), raValuedecimal, decValuedecimal, Start_time, end_time);

            List<String> arrayNames = Stars.getArrayNames(),
                    arrayvmag = Stars.getV(), arraypri = Stars.getPRI(),
                    arrayra = Stars.getRA(), arraydec = Stars.getDEC(),
                    First_var = Stars.getFIRST_VAR(), Second_var = Stars.getSECOND_VAR();

            String[] starsArray;
            starsArray = new String[]{
                    "RA Input: " + (degreeSwitch.isChecked() ? raValue + "°" : raValue2),
                    "DEC Input: " + (decdegreeSwitch.isChecked() ? decValue + "°" : decValue2),
                    "Search Radius: " + radiusValue + "°",
                    "",
                    "Results:"
            };

            if (!starsWithinRadius.isEmpty()) {
                List<String> resultList = new ArrayList<>();
                for (Integer index : starsWithinRadius) {
                    String starName = arrayNames.get(index);  // Get the star name at the index
                    String[] starNameparts = starName.split("_");
                    String starNameflipped = starNameparts[1] + "_" + starNameparts[0];
                    String vmag = arrayvmag.get(index);
                    String pri = arraypri.get(index);
                    String ra = String.valueOf(Math.round(Double.parseDouble(arrayra.get(index)) * 1000.0) / 1000.0)+ "°";
                    String dec = String.valueOf(Math.round(Double.parseDouble(arraydec.get(index)) * 1000.0) / 1000.0)+ "°";
                    double jdDatestart = getJulianDate(Start_time); // In UTC
                    double data_First_var = Double.parseDouble(First_var.get(index));
                    double data_Second_var = Double.parseDouble(Second_var.get(index));
                    double primaryMax = data_First_var + (Math.ceil((jdDatestart - data_First_var) / data_Second_var)) * data_Second_var;
                    double secondsUntil = (primaryMax - jdDatestart) * 86400; //43200 is 12 hours
                    String timeuntil = TimeClasses.convertSecondsToTime((int) secondsUntil);
                    String eclp_time;

                    if (degreeSwitch.isChecked()) {
                        ra = String.valueOf(Math.round(Double.parseDouble(arrayra.get(index)) * 1000.0) / 1000.0)+ "°";
                    } else {
                        double totalHours = Double.parseDouble(arrayra.get(index)) / 15.0;
                        int hours = (int) totalHours;
                        double remainingMinutes = (totalHours - hours) * 60;
                        int minutes = (int) remainingMinutes;
                        double seconds = (remainingMinutes - minutes) * 60;
                        ra = String.format("%dh %dm %.2fs", hours, minutes, seconds);
                    }
                    if (decdegreeSwitch.isChecked()) {
                        dec = String.valueOf(Math.round(Double.parseDouble(arraydec.get(index)) * 1000.0) / 1000.0)+ "°";
                    } else {
                        double decimalDegree = Double.parseDouble(arraydec.get(index));
                        int degrees = (int) decimalDegree;
                        double fractionalPart = decimalDegree - degrees;
                        int minutes = (int) (fractionalPart * 60);
                        double fractionalMinutes = (fractionalPart * 60) - minutes;
                        double seconds = fractionalMinutes * 60;
                        dec = String.format("%d° %dm %.2fs", degrees, minutes, seconds);
                    }

                    try {
                        eclp_time = TimeClasses.addtimetodate(Start_time, timeuntil);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    resultList.add(starNameflipped);
                    resultList.add("Next eclipse " + eclp_time);  // Add the star name to the result list
                    resultList.add(timeuntil);
                    resultList.add("Vmag: " + vmag + "   Depth Pri: " + pri);
                    resultList.add("RA: " + ra);
                    resultList.add("Dec: " + dec);
                    resultList.add(" ");
                }
                starsArray = combineArrays(starsArray, resultList.toArray(new String[0]));
            } else {
                starsArray = combineArrays(starsArray, new String[]{"No stars found within radius."});
            }
            // Pass the data to DashboardFragment
            Bundle bundle = new Bundle();
            bundle.putStringArray("stars_data", starsArray);  // Pass the array of stars
            DashboardFragment dashboardFragment = new DashboardFragment();
            dashboardFragment.setArguments(bundle);  // Set the data in the fragment

            // Navigate to DashboardFragment
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
            bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard); // Select the Dashboard tab
            Navigation.findNavController(v).navigate(R.id.action_home_to_dashboard, bundle);  // Pass data via navigation
        });

        setUpSpinner(binding.raHourSpinner, R.array.spinner_items_hour);
        setUpSpinner(binding.raMinSpinner, R.array.spinner_items_min);
        setUpSpinner(binding.raSecSpinner, R.array.spinner_items_sec);
        setUpSpinner(binding.decDegSpinner, R.array.spinner_items_deg);
        setUpSpinner(binding.decMinSpinner, R.array.spinner_items_min);
        setUpSpinner(binding.decSecSpinner, R.array.spinner_items_sec);

        degreeSwitch = root.findViewById(R.id.degree_switch);
        decdegreeSwitch = root.findViewById(R.id.dec_degree_switch);
        raNumberInput = root.findViewById(R.id.RA_Number_Input);
        raHourSpinner = root.findViewById(R.id.ra_hour_spinner);
        raminSpinner = root.findViewById(R.id.ra_min_spinner);
        rasecSpinner = root.findViewById(R.id.ra_sec_spinner);
        decdegspinner = root.findViewById(R.id.dec_deg_spinner);
        decminspinner = root.findViewById(R.id.dec_min_spinner);
        decsecspinner = root.findViewById(R.id.dec_sec_spinner);
        decdegspinner.setSelection(89);

        TextView textRA = root.findViewById(R.id.text_RA);
        TextView text_DEC = root.findViewById(R.id.text_DEC);
        raNumberInput.setVisibility(View.GONE);
        degreeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            boolean isVisible = isChecked;  // RA number input visibility toggle
            raNumberInput.setVisibility(isVisible ? View.VISIBLE : View.GONE);
            raHourSpinner.setVisibility(isVisible ? View.GONE : View.VISIBLE);
            raminSpinner.setVisibility(isVisible ? View.GONE : View.VISIBLE);
            rasecSpinner.setVisibility(isVisible ? View.GONE : View.VISIBLE);
            textRA.setText(isVisible ? "Input RA (0° to 360°)" : "Input RA");
        });
        decNumberInput.setVisibility(View.GONE);
        decdegreeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            boolean isVisible = isChecked;  // RA number input visibility toggle
            decNumberInput.setVisibility(isVisible ? View.VISIBLE : View.GONE);
            decdegspinner.setVisibility(isVisible ? View.GONE : View.VISIBLE);
            decminspinner.setVisibility(isVisible ? View.GONE : View.VISIBLE);
            decsecspinner.setVisibility(isVisible ? View.GONE : View.VISIBLE);
            text_DEC.setText(isVisible ? "Input DEC (-90° to 90°)" : "Input DEC");
        });

        EditText raNumberInput = binding.RANumberInput;
        raNumberInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable editable) {
                String inputText = editable.toString();
                if (!inputText.isEmpty()) {
                    try {
                        double value = Double.parseDouble(inputText);
                        if (value < 0) {raNumberInput.setText("0");
                        } else if (value > 360) {raNumberInput.setText("360");
                        } else {
                            String str = Double.toString(value);
                            if (str.contains(".") && str.split("\\.")[1].length() > 3) {
                                double truncatedValue = (double) Math.floor(value * 1000) / 1000.0;
                                raNumberInput.setText(String.valueOf(truncatedValue));
                            }
                        }
                    } catch (NumberFormatException e) {
                        raNumberInput.setText("0");
                    }
                    raNumberInput.setSelection(raNumberInput.getText().length());
                }
            }
        });

        EditText radiusNumberInput = binding.RADIUSNumberInput;
        radiusNumberInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable editable) {
                String inputText = editable.toString();
                if (!inputText.isEmpty()) {
                    try {
                        double value = Double.parseDouble(inputText);
                        if (value < 0) {radiusNumberInput.setText("0");
                        } else if (value > 360) {radiusNumberInput.setText("360");
                        } else {
                            String str = Double.toString(value);
                            if (str.contains(".") && str.split("\\.")[1].length() > 3) {
                                double truncatedValue = (double) Math.floor(value * 1000) / 1000.0;
                                radiusNumberInput.setText(String.valueOf(truncatedValue));
                            }
                        }
                    } catch (NumberFormatException e) {
                        radiusNumberInput.setText("0");
                    }
                    radiusNumberInput.setSelection(radiusNumberInput.getText().length());
                }
            }
        });

        EditText decNumberInput = binding.DECNumberInput;
        decNumberInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable editable) {
                String inputText = editable.toString();

                if ((inputText.equals("-")) || (inputText.equals("-."))) {
                    return;
                }
                if (!inputText.isEmpty()) {
                    try {
                        double value = Double.parseDouble(inputText);
                        if (value < -90) {decNumberInput.setText("-90");
                        } else if (value > 90) {decNumberInput.setText("90");
                        } else {
                            String str = Double.toString(value);
                            if (str.contains(".") && str.split("\\.")[1].length() > 3)  {
                                if (value > 0) {
                                    double truncatedValue = (double) Math.floor(value * 1000) / 1000.0;
                                    decNumberInput.setText(String.valueOf(truncatedValue));
                                } else {
                                    double truncatedValue = (double) Math.ceil(value * 1000) / 1000.0;
                                    decNumberInput.setText(String.valueOf(truncatedValue));
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        decNumberInput.setText("0");
                    }
                    decNumberInput.setSelection(decNumberInput.getText().length());
                }
            }
        });
        return root;
    }
    public void openTimePicker(View view) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getActivity(),
                (timePicker, hourOfDay, minute1) -> {
                    String amPm = (hourOfDay >= 12) ? "PM" : "AM";
                    int hour12 = (hourOfDay % 12 == 0) ? 12 : hourOfDay % 12;
                    String time = String.format("%02d:%02d %s", hour12, minute1, amPm);
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
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                selectedMonth = selectedMonth + 1;
                String date = TimeClasses.makeDateString(selectedDay, selectedMonth, selectedYear);
                if (view.getId() == R.id.datePickerButton) {
                    dateButton.setText(date);
                } else if (view.getId() == R.id.enddatePickerButton) {
                    enddateButton.setText(date);
                }
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(), // Context (activity)
                AlertDialog.THEME_HOLO_LIGHT, // Theme
                dateSetListener, // Listener for when the date is set
                year, // Initial year
                month, // Initial month
                day  // Initial day
        );
        int[] yearRange = {1950, 2050};
        datePickerDialog.getDatePicker().setMinDate(new GregorianCalendar(yearRange[0], 0, 1).getTimeInMillis());  // Set minimum date
        datePickerDialog.getDatePicker().setMaxDate(new GregorianCalendar(yearRange[1], 11, 31).getTimeInMillis());  // Set maximum date
        datePickerDialog.show();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private String[] combineArrays(String[] array1, String[] array2) {
        String[] combined = new String[array1.length + array2.length];
        System.arraycopy(array1, 0, combined, 0, array1.length);
        System.arraycopy(array2, 0, combined, array1.length, array2.length);
        return combined;
    }
    private void setUpSpinner(Spinner spinner, int arrayResId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                arrayResId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedOption = (String) parentView.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parentView) {
                // Handle case when no item is selected
            }
        });
    }

}