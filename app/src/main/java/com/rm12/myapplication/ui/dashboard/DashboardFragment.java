package com.rm12.myapplication.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.rm12.myapplication.R;
import com.rm12.myapplication.databinding.FragmentDashboardBinding;


public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private TableLayout tableLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tableLayout = root.findViewById(R.id.tableLayout);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String[] starsArray = bundle.getStringArray("stars_data");

            if (starsArray != null) {
                populateTable(starsArray);  // Populate the TableLayout
            }
        }

        return root;
    }

    private void populateTable(String[] starsArray) {
        for (String star : starsArray) {
            TableRow tableRow = new TableRow(getContext());
            TextView textView = new TextView(getContext());
            textView.setText(star);
            textView.setTextSize(18);
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            tableRow.addView(textView);
            tableLayout.addView(tableRow);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
