package com.example.tallyapp.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListPopupWindow;

import com.example.tallyapp.R;
import com.example.tallyapp.dbhelper.DBHelper;
import com.example.tallyapp.entity.Income;
import com.example.tallyapp.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountingIncome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountingIncome extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatePickerDialog datePickerDialog;
    private EditText selectedTitleEditText, writeInAmountEditText, selectedDateEditText, writeInRemarkEditText;
    private Button addIncome;
    private int userID, typeID;
    private double amount;
    private Date date;
    private String username, formattedDate, selectedtype, selectedtypename;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private Utils utils;
    private int year, month, day;
    private Calendar calendar;

    private DBHelper dbHelper; //定义数据库帮助类对象
    private SQLiteDatabase db;

    public AccountingIncome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountingIncome.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountingIncome newInstance(String param1, String param2) {
        AccountingIncome fragment = new AccountingIncome();
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
        View view = inflater.inflate(R.layout.fragment_accounting_income, container, false);
        //初始化
        initView(view);
        //初始化日期选择器
        initDatePicker();
        //点击获取日期
        chickDateTextView();
        //获取收入类型
        showIncomeTypesAndWriteIn();
        //点击文本将文本置空
        initAmount();
        //按钮点击事件
        checkButton();
        return view;
    }

    private void initView(View view){
        selectedTitleEditText = view.findViewById(R.id.selectedTitleEditText);
        selectedDateEditText = view.findViewById(R.id.selectedDateEditText);
        writeInAmountEditText = view.findViewById(R.id.writeInAmountEditText);
        writeInRemarkEditText = view.findViewById(R.id.writeInRemarkEditText);
        addIncome = view.findViewById(R.id.addIncome);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dbHelper = new DBHelper(requireContext());
        db = dbHelper.getWritableDatabase();
        sharedPref = requireContext().getSharedPreferences("user_info", 0);
        username = sharedPref.getString("username", "");
        utils = new Utils(requireContext());
    }

    @SuppressLint("ClickableViewAccessibility")
    private void chickDateTextView() {
        selectedDateEditText.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (selectedDateEditText.getRight() - selectedDateEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    showHideDatePicker();
                }
            }
            return true;
        });
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
    private void initDatePicker() {
        // 使用Calendar获取当前日期
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        // 设置DatePickerDialog的语言环境为中文
        Locale locale = Locale.CHINESE; // 中文语言环境
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());


        // 创建DatePickerDialog，并设置日期选择器的监听器
        datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 处理所选日期的逻辑，这里可以将日期设置到TextView中或进行其他操作
                formattedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                // 修改日期格式为 SQLite 所支持的格式

                // 将格式化后的日期设置到TextView中
                selectedDateEditText.setText(formattedDate);
            }
        }, year, month, day);

        // 可以设置其他DatePickerDialog的属性，例如最小/最大日期等
        // datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showIncomeTypesAndWriteIn() {
        selectedTitleEditText.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (selectedTitleEditText.getRight() - selectedTitleEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    ListPopupWindow listPopupWindow = new ListPopupWindow(requireContext());

                    //获取类型列表
                    ArrayList<String> incometypes = utils.getIncomeTypes(db);
                    // 创建适配器
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, incometypes);
                    listPopupWindow.setAdapter(adapter);

                    // 设置列表项点击事件
                    listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
                        selectedtype = incometypes.get(position);
                        selectedtypename = selectedtype;

                        selectedTitleEditText.setText(selectedtypename);

                        listPopupWindow.dismiss();
                    });


                    // 设置ListPopupWindow的锚点及显示位置
                    listPopupWindow.setAnchorView(selectedTitleEditText);
                    listPopupWindow.setDropDownGravity(Gravity.END);

                    // 显示ListPopupWindow
                    listPopupWindow.show();
                    return true;
                }
            }
            return false;
        });
    }

    private void initAmount(){
        writeInAmountEditText.setOnClickListener(v -> {
            writeInAmountEditText.setText("");
        });
    }

    //获取信息，并存入Expense类
    @SuppressLint("Range")
    private Income getIncomeInformation() throws ParseException {
        Income income = new Income();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        //保存时间
        //保存typeId
        //通过typename获取typeid
        // 查询语句
        String query = "SELECT id FROM income_type WHERE typename = ?";
        Cursor cursor = db.rawQuery(query, new String[]{selectedtypename});
        if (cursor != null && cursor.moveToFirst()) {
            typeID = cursor.getInt(cursor.getColumnIndex("id"));
        }

        //通过username查找userid
        cursor = db.rawQuery("SELECT id FROM users where username = ?", new String[]{username});
        if (cursor != null && cursor.moveToFirst()) {
            userID = cursor.getInt(cursor.getColumnIndex("id"));
        }
        amount = Double.parseDouble(writeInAmountEditText.getText().toString());

        //保存数据
        income.setDate(sdf.parse(formattedDate));
        income.setIncomeTypeID(typeID);
        income.setUserID(userID);
        income.setAmount(amount);
        return income;
    }

    private boolean saveIncome() throws ParseException {
        Income income = getIncomeInformation();
        ContentValues values = new ContentValues();
        values.put("userID", income.getUserID());
        values.put("incometypeID", income.getIncomeTypeID());
        values.put("Amount", income.getAmount());
        values.put("Date", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(income.getDate()));

        long result = db.insert("income", null, values);
        return result != -1; // 返回是否成功插入数据
    }

    //将本次输入的数据存储在Income类中
    private void checkButton(){
        addIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(!utils.getIncomeTypes(db).contains(selectedTitleEditText.getText().toString()) || !writeInAmountEditText.getText().toString().matches("[0-9]+")){
                        return;
                    }else {
                        saveIncome();
                        utils.showDialog("添加成功！");
                        selectedTitleEditText.setText("点击输入类型");
                        writeInAmountEditText.setText("输入金额");
                        selectedDateEditText.setText("点击输入金额");
                        writeInRemarkEditText.setText("输入备注");
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}