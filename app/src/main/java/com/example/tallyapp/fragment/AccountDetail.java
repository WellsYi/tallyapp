package com.example.tallyapp.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;

import androidx.fragment.app.Fragment;

import com.example.tallyapp.R;
import com.example.tallyapp.adpter.BanlancesAdapter;
import com.example.tallyapp.dbhelper.DBHelper;
import com.example.tallyapp.entity.Balances;
import com.example.tallyapp.entity.Expense;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountDetail extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DBHelper dbHelper; //定义数据库帮助类对象
    private SQLiteDatabase db;
    private ArrayList<Expense> expenses;
    private ListView listView;
    private NumberPicker yearPicker;
    private NumberPicker monthPicker;
    private EditText monthEditText, yearEditText, incomeAmountEditText, expenseAmountEditText;
    private BottomSheetDialog bottomSheetDialog;
    private Button cancel, confirm;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private String username;
    private Integer id;
    private ArrayList<BigDecimal> Data;

    public AccountDetail() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountDetail.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountDetail newInstance(String param1, String param2) {
        AccountDetail fragment = new AccountDetail();
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
        View view = inflater.inflate(R.layout.fragment_account_detail, container, false);
        initView(view);
        initTimeAndTally();
        selectDate();
        return view;
    }

    private void initView(View view) {
        dbHelper = new DBHelper(requireContext());
        db = dbHelper.getWritableDatabase();
        listView = view.findViewById(R.id.listView);
        monthEditText = view.findViewById(R.id.monthEditText);
        yearEditText = view.findViewById(R.id.yearEditText);
        incomeAmountEditText = view.findViewById(R.id.income_amount_edit_text);
        expenseAmountEditText = view.findViewById(R.id.expense_amount_edit_text);
        bottomSheetDialog = new BottomSheetDialog(requireContext());
        sharedPref = requireContext().getSharedPreferences("user_info", 0);
        username = sharedPref.getString("username", "");
        Data = getTotalIncomeAndExpense();
        id = getUserIdByUserName(username);
    }

    private void selectDate(){
        monthEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showYearMonthPickerDialog();
            }
        });
    }

    public void showYearMonthPickerDialog() {
        @SuppressLint("InflateParams") View dialogView = getLayoutInflater().inflate(R.layout.year_month_picker_layout, null);
        yearPicker = dialogView.findViewById(R.id.yearPicker);
        monthPicker = dialogView.findViewById(R.id.monthPicker);
        cancel = dialogView.findViewById(R.id.cancel_button);
        confirm = dialogView.findViewById(R.id.confirm_button);


        // 设置年份范围
        int currentYear = 2023; // 设置当前年份
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

        // 设置月份范围
        String[] displayedMonths = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setDisplayedValues(displayedMonths);
        monthPicker.setValue(Integer.valueOf(getCurrentBeijingMonth())); // 设置初始月份为1月
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
                int year = (int)yearPicker.getValue();
                int month = (int)monthPicker.getValue();

                yearEditText.setText(String.valueOf(year + "年"));
                monthEditText.setText(String.valueOf(month + "月"));
                ArrayList<Balances> selectedBalances = getExpensesAndIncomesForSelectedMonthYear(year, month);
                BanlancesAdapter adapter = new BanlancesAdapter(getContext(), selectedBalances);
                listView.setAdapter(adapter);
                bottomSheetDialog.dismiss();
            }
        });
    }

    private String getCurrentBeijingYear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai")); // 设置时区为北京时间
        Date currentDate = new Date();
        return sdf.format(currentDate);
    }

    private String getCurrentBeijingMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai")); // 设置时区为北京时间
        Date currentDate = new Date();
        return sdf.format(currentDate);
    }

    @SuppressLint("SetTextI18n")
    private void initTimeAndTally(){
        monthEditText.setText(getCurrentBeijingMonth() + "月");
        yearEditText.setText(getCurrentBeijingYear() + "年");
        int year = Integer.parseInt(getCurrentBeijingYear());
        int month = Integer.parseInt(getCurrentBeijingMonth());
        ArrayList<Balances> selectedBalances = getExpensesAndIncomesForSelectedMonthYear(year, month);
        BanlancesAdapter adapter = new BanlancesAdapter(getContext(), selectedBalances);
        setIncomesAndExpenses();
        listView.setAdapter(adapter);

    }

    private ArrayList<BigDecimal> getTotalIncomeAndExpense(){
        ArrayList<BigDecimal>Data = new ArrayList<>();
        String sql = "SELECT\n" +
                "TotalIncomes.TotalAmount AS Incomes, TotalExpenses.TotalAmount AS Expenses\n" +
                "FROM\n" +
                "(\n" +
                "    SELECT\n" +
                "        SUM(CASE WHEN income.userID = users.id THEN income.Amount ELSE 0 END) AS TotalAmount,\n" +
                "        COUNT(CASE WHEN income.userID = users.id THEN 1 ELSE NULL END) AS TotalCount\n" +
                "    FROM income\n" +
                "    JOIN users ON income.userID = users.id\n" +
                "    WHERE users.username = ?\n" +
                ") AS TotalIncomes,\n" +
                "(\n" +
                "    SELECT\n" +
                "        SUM(CASE WHEN expense.userID = users.id THEN Expense.Amount ELSE 0 END) AS TotalAmount,\n" +
                "        COUNT(CASE WHEN expense.userID = users.id THEN 1 ELSE NULL END) AS TotalCount\n" +
                "    FROM expense\n" +
                "    JOIN users ON expense.userID = users.id\n" +
                "    WHERE users.username = ?\n" +
                ") AS TotalExpenses;";

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, new String[]{username, username});

        if (cursor != null && cursor.moveToFirst()) {
            // 将数据库中的 double 值转换成 BigDecimal，并设置精度为两位小数
            @SuppressLint("Range") BigDecimal totalIncomes = BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex("Incomes"))).setScale(2, RoundingMode.HALF_UP);
            @SuppressLint("Range") BigDecimal totalExpense = BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex("Expenses"))).setScale(2, RoundingMode.HALF_UP);
            Data.add(totalIncomes);
            Data.add(totalExpense);
        }

        return Data; // 返回保留两位小数的 BigDecimal 数值列表
    }

    private void setIncomesAndExpenses(){
        Data = getTotalIncomeAndExpense();
        incomeAmountEditText.setText(String.valueOf(Data.get(0)));
        expenseAmountEditText.setText(String.valueOf(Data.get(1)));
    }
    @SuppressLint("Range")
    public ArrayList<Balances> getExpensesAndIncomesForSelectedMonthYear(int selectedYear, int selectedMonth) {
        ArrayList<Balances> Balances = new ArrayList<>();

        // 构建符合数据库格式的日期字符串，例如："YYYY-MM"
        String selectedDate = String.format(Locale.getDefault(), "%04d-%02d", selectedYear, selectedMonth);

        // 修改 SQL 查询语句，筛选选定年份和月份的用户支出信息
        String query = "SELECT 'Expense' AS RecordType, e.id AS RecordID, et.typename AS Type, e.Amount, e.Date AS RecordDate " +
                "FROM expense e " +
                "INNER JOIN users u ON e.userID = u.id " +
                "INNER JOIN expense_type et ON e.expensetypeID = et.id " +
                "WHERE e.userID = ? AND e.Date LIKE ? " +
                "UNION ALL " +
                "SELECT 'Income' AS RecordType, i.id, it.typeName AS Type, i.Amount, i.Date AS RecordDate " +
                "FROM income i " +
                "INNER JOIN users u ON i.userID = u.id " +
                "INNER JOIN income_type it ON i.incometypeID = it.id " +
                "WHERE i.userID = ? AND i.Date LIKE ? " +
                "ORDER BY RecordDate DESC;";


        String queryDateFilter = selectedDate + "-%";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id), queryDateFilter, String.valueOf(id), queryDateFilter});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // 从数据库中读取数据并创建 Balances 对象
                int id = cursor.getInt(cursor.getColumnIndex("RecordID"));
                String RecordType = cursor.getString(cursor.getColumnIndex("RecordType"));
                String Type = cursor.getString(cursor.getColumnIndex("Type"));
                BigDecimal amount = BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex("Amount")));
                String dateString = cursor.getString(cursor.getColumnIndex("RecordDate"));

                // 将字符串类型的日期转换为 Date 类型
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date = null;
                try {
                    date = sdf.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // 创建 Banlances 对象并添加到列表中
                Balances balance = new Balances(id, RecordType, Type, amount, date);
                Balances.add(balance);
            } while (cursor.moveToNext());
        }

        return Balances;
    }

    @SuppressLint("Range")
    private Integer getUserIdByUserName(String username){
        String query = "SELECT id FROM users WHERE userName = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        if (cursor != null && cursor.moveToFirst()){
            id = cursor.getInt(cursor.getColumnIndex("id"));
            return id;
        }
        return null;
    }

    //刷新界面
    @Override
    public void onResume() {
        super.onResume();
        // 在这里执行刷新操作，可以是重新加载数据或调用特定的刷新方法
        refreshFragmentData();
    }

    private void refreshFragmentData(){
        initTimeAndTally();
        setIncomesAndExpenses();
    }

}