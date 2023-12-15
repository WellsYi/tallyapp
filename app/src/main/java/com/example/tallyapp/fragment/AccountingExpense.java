package com.example.tallyapp.fragment;

import android.app.DatePickerDialog;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.tallyapp.R;

import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountingExpense#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountingExpense extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatePickerDialog datePickerDialog;
    private TextView selectedDateTextView;

    public AccountingExpense() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountingExpense.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountingExpense newInstance(String param1, String param2) {
        AccountingExpense fragment = new AccountingExpense();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_accounting_expense, container, false);
        initView(view);
        // 设置DatePicker的监听器
        selectedDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHideDatePicker();
            }
        });

        // 初始化DatePickerDialog
        initDatePicker();
        return view;
    }

    private void initView(View view){
        selectedDateTextView = view.findViewById(R.id.selectedDateTextView);
    }

    private void initDatePicker() {
        // 使用Calendar获取当前日期
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // 设置DatePickerDialog的语言环境为中文
        Locale locale = Locale.CHINESE; // 中文语言环境
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());


        // 创建DatePickerDialog，并设置日期选择器的监听器
        datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 处理所选日期的逻辑，这里可以将日期设置到TextView中或进行其他操作
                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year; // 修改日期格式
                selectedDateTextView.setText("Selected Date: " + selectedDate);
            }
        }, year, month, day);

        // 可以设置其他DatePickerDialog的属性，例如最小/最大日期等
        // datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private void showHideDatePicker() {
        if (datePickerDialog != null && !datePickerDialog.isShowing()) {
            // 如果DatePickerDialog未显示，则显示它
            datePickerDialog.show();
        } else if (datePickerDialog != null && datePickerDialog.isShowing()) {
            // 如果DatePickerDialog正在显示，则隐藏它
            datePickerDialog.dismiss();
        }
    }
}