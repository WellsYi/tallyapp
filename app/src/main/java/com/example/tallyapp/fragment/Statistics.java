package com.example.tallyapp.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.tallyapp.R;
import com.example.tallyapp.adpter.StatisticsAdtpter;
import com.example.tallyapp.dbhelper.DBHelper;
import com.example.tallyapp.entity.Balances;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Statistics#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Statistics extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView changetypeTextView, yearPickerTextView, balanceamountTextView, yearexpenseamountTextView, yearincomeamountTextView, yearTextView;
    private ListView detailListView;
    private ImageView yearPickerImageView;
    private BottomSheetDialog bottomSheetDialog;
    private DBHelper dbHelper; //定义数据库帮助类对象
    private SQLiteDatabase db;
    private SharedPreferences sharedPref;
    private String username;
    private NumberPicker yearPicker;
    private Button cancel, confirm;
    private int year;



    public Statistics() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Chart.
     */
    // TODO: Rename and change types and number of parameters
    public static Statistics newInstance(String param1, String param2) {
        Statistics fragment = new Statistics();
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
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        initView(view);
        selectYear();
        ArrayList<Balances> balances = getExpenseData(String.valueOf(getCurrentBeijingYear()));
        StatisticsAdtpter adtpter = new StatisticsAdtpter(getContext(), balances);
        detailListView.setAdapter(adtpter);
        setChangetypeTextView();
        setInitYear();
        return view;
    }

    private void initView(View view){
        yearPickerTextView = view.findViewById(R.id.yearPickerTextView);
        balanceamountTextView = view.findViewById(R.id.balanceamountTextView);
        yearexpenseamountTextView = view.findViewById(R.id.yearexpenseamountTextView);
        yearincomeamountTextView = view.findViewById(R.id.yearincomeamountTextView);
        yearTextView = view.findViewById(R.id.yearTextView);
        changetypeTextView = view.findViewById(R.id.changetypeTextView);
        detailListView = view.findViewById(R.id.detailListView);
        yearPickerImageView = view.findViewById(R.id.yearPickerImageView);
        bottomSheetDialog = new BottomSheetDialog(requireContext());
        sharedPref = requireContext().getSharedPreferences("user_info", 0);
        username = sharedPref.getString("username", "");
        dbHelper = new DBHelper(requireContext());
        db = dbHelper.getWritableDatabase();
        year = Integer.valueOf(getCurrentBeijingYear());
        getSelectedYearRecord(getCurrentBeijingYear());

    }

    private void setInitYear(){
        yearPickerTextView.setText(String.valueOf(year) + "年度账单");
        yearTextView.setText(String.valueOf(year) + "年");
    }

    public void showYearPickerDialog(){
        @SuppressLint("InflateParams") View dialogView = getLayoutInflater().inflate(R.layout.year_picker_layout, null);
        yearPicker = dialogView.findViewById(R.id.yearPicker);
        cancel = dialogView.findViewById(R.id.cancel_button);
        confirm = dialogView.findViewById(R.id.confirm_button);

        // 设置年份范围
        int currentYear = Integer.valueOf("2023"); // 设置当前年份
        int minYear = 1900; // 设置最小年份
        int maxYear = 2100; // 设置最大年份
        String[] displayedYears = new String[maxYear - minYear + 1];

        for (int i = minYear; i <= maxYear; i++) {
            displayedYears[i - minYear] = String.valueOf(i);
        }

        yearPicker.setMinValue(minYear);
        yearPicker.setMaxValue(maxYear);
        yearPicker.setDisplayedValues(displayedYears);
        yearPicker.setValue(currentYear);

        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = (int)yearPicker.getValue();
                yearPickerTextView.setText(String.valueOf(year) + "年度账单");
                yearTextView.setText(String.valueOf(year) + "年");
                //获取该年的结余，支出，收入
                getSelectedYearRecord(String.valueOf(year));
                //刷新列表
                ArrayList<Balances> balances = getExpenseData(String.valueOf(year));
                StatisticsAdtpter adtpter = new StatisticsAdtpter(getContext(), balances);
                detailListView.setAdapter(adtpter);
                bottomSheetDialog.dismiss();
            }
        });
    }

    @SuppressLint("Range")
    private void getSelectedYearRecord(String year){
        String sql = "SELECT\n" +
                "    TotalIncomes.TotalAmount AS Incomes,\n" +
                "    TotalExpenses.TotalAmount AS Expenses,\n" +
                "    (TotalIncomes.TotalAmount - TotalExpenses.TotalAmount) AS Balance\n" +
                "FROM\n" +
                "    (\n" +
                "        SELECT\n" +
                "            SUM(CASE WHEN income.userID = users.id AND strftime('%Y', income.Date) = ? THEN income.Amount ELSE 0 END) AS TotalAmount\n" +
                "        FROM income\n" +
                "        JOIN users ON income.userID = users.id\n" +
                "        WHERE users.username = ?\n" +
                "    ) AS TotalIncomes,\n" +
                "    (\n" +
                "        SELECT\n" +
                "            SUM(CASE WHEN expense.userID = users.id AND strftime('%Y', expense.Date) = ? THEN expense.Amount ELSE 0 END) AS TotalAmount\n" +
                "        FROM expense\n" +
                "        JOIN users ON expense.userID = users.id\n" +
                "        WHERE users.username = ?\n" +
                "    ) AS TotalExpenses;\n";
        Cursor cursor = db.rawQuery(sql, new String[]{year, username, year, username});
        if (cursor != null && cursor.moveToFirst()) {
            // 将数据库中的 double 值转换成 BigDecimal，并设置精度为两位小数
            BigDecimal totalIncomes = BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex("Incomes"))).setScale(2, RoundingMode.HALF_UP);
            BigDecimal totalExpense = BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex("Expenses"))).setScale(2, RoundingMode.HALF_UP);
            Double incomes = cursor.getDouble((cursor.getColumnIndex("Incomes")));
            Double expense = cursor.getDouble(cursor.getColumnIndex("Expenses"));
            yearexpenseamountTextView.setText("-￥" + String.valueOf(totalExpense));
            yearincomeamountTextView.setText("￥" + String.valueOf(totalIncomes));
            balanceamountTextView.setText("￥" + String.valueOf(BigDecimal.valueOf(incomes-expense).setScale(2, RoundingMode.HALF_UP)));

        }
    }

    private String getCurrentBeijingYear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai")); // 设置时区为北京时间
        Date currentDate = new Date();
        return sdf.format(currentDate);
    }

    private void selectYear(){
        yearPickerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showYearPickerDialog();
            }
        });
        yearPickerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showYearPickerDialog();
            }
        });
    }

    //获取某年的各类型的支出
    @SuppressLint("Range")
    private ArrayList<Balances> getExpenseData(String year){
        ArrayList<Balances> Balances = new ArrayList<>();
        String type;
        Double percentage;
        BigDecimal totalAmount;
        String sql = "SELECT Type,\n" +
                "       COALESCE(SUM(TotalAmount), 0) AS TotalAmount\n" +
                "FROM (\n" +
                "    SELECT 'Expense' AS RecordType,\n" +
                "           et.typename AS Type,\n" +
                "           e.Amount AS TotalAmount\n" +
                "    FROM expense e\n" +
                "    INNER JOIN users u ON e.userID = u.id\n" +
                "    INNER JOIN expense_type et ON e.expensetypeID = et.id\n" +
                "    WHERE u.username = ? AND strftime('%Y', e.Date) = ?\n" +
                ") AS ExpenseResults\n" +
                "GROUP BY Type\n" +
                "ORDER BY TotalAmount DESC;\n";
        Cursor cursor = db.rawQuery(sql, new String[]{username, year});
        if (cursor != null && cursor.moveToFirst()) {
           do{
               type = cursor.getString(cursor.getColumnIndex("Type"));
               totalAmount = BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex("TotalAmount")));
               Balances balance = new Balances(type, totalAmount);
               Balances.add(balance);
           }while (cursor.moveToNext());
        }
        return Balances;
    }

    //获取某年的各类型的收入
    @SuppressLint("Range")
    private ArrayList<Balances> getIncomeData(String year){
        ArrayList<Balances> Balances = new ArrayList<>();
        String type;
        Double percentage;
        BigDecimal totalAmount;
        String sql = "SELECT Type,\n" +
                "       COALESCE(SUM(TotalAmount), 0) AS TotalAmount\n" +
                "FROM (\n" +
                "    SELECT 'Income' AS RecordType,\n" +
                "           it.typename AS Type,\n" +
                "           i.Amount AS TotalAmount\n" +
                "    FROM income i\n" +
                "    INNER JOIN users u ON i.userID = u.id\n" +
                "    INNER JOIN income_type it ON i.incometypeID = it.id\n" +
                "    WHERE u.username = ? AND strftime('%Y', i.Date) = ?\n" +
                ") AS IncomeResults\n" +
                "GROUP BY Type\n" +
                "ORDER BY TotalAmount DESC;\n";
        Cursor cursor = db.rawQuery(sql, new String[]{username, year});
        if (cursor != null && cursor.moveToFirst()) {
            do{
                type = cursor.getString(cursor.getColumnIndex("Type"));
                totalAmount = BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex("TotalAmount")));
                Balances balance = new Balances(type, totalAmount);
                Balances.add(balance);
            }while (cursor.moveToNext());
        }
        return Balances;
    }



    private void setChangetypeTextView(){
        changetypeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String flag = "";
                flag = changetypeTextView.getText().toString();
                if(flag.equals("支出排行榜")){
                    changetypeTextView.setText("收入排行榜");
                    flag = "收入排行榜";
                    ArrayList<Balances> balances = getIncomeData(String.valueOf(year));
                    StatisticsAdtpter adtpter = new StatisticsAdtpter(getContext(), balances);
                    detailListView.setAdapter(adtpter);
                }else {
                    changetypeTextView.setText("支出排行榜");
                    flag = "支出排行榜";
                    ArrayList<Balances> balances = getExpenseData(String.valueOf(year));
                    StatisticsAdtpter adtpter = new StatisticsAdtpter(getContext(), balances);
                    detailListView.setAdapter(adtpter);
                }
            }
        });
    }
}