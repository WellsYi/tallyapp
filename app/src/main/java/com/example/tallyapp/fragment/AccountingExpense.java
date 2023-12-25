package com.example.tallyapp.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
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
import android.widget.TextView;

import com.example.tallyapp.R;
import com.example.tallyapp.activity.LoginActivity;
import com.example.tallyapp.dbhelper.DBHelper;
import com.example.tallyapp.entity.Expense;
import com.example.tallyapp.entity.Users;
import com.example.tallyapp.utils.ShowDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private Drawable icon;
    private EditText selectedDateTextView, selectedTitleTextView,  writeInAmountTextView, writeInRemarkTextView;
    private Button addExpenseButton;
    private int year, month, day;
    private Calendar calendar;

    private DBHelper dbHelper; //定义数据库帮助类对象
    private SQLiteDatabase db;

    private String selectedtype, selectedtypename, formattedDate, username;
    private int userID, typeID;
    private double amount;
    private Date date;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private ShowDialog showDialog;

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
        //获取日期
        chickDateTextView();
        // 初始化DatePickerDialog
        initDatePicker();
        showExpenseTypesAndWriteIn();
        initAmount();
        checkButton();
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void chickDateTextView() {
        selectedDateTextView.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (selectedDateTextView.getRight() - selectedDateTextView.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    showHideDatePicker();
                }
            }
            return true;
        });
    }

    private void initView(View view){
        selectedDateTextView = view.findViewById(R.id.selectedDateTextView);
        selectedTitleTextView = view.findViewById(R.id.selectedTitleTextView);
        writeInAmountTextView = view.findViewById(R.id.writeInAmountTextView);
        writeInRemarkTextView = view.findViewById(R.id.writeInRemarkTextView);
        addExpenseButton = view.findViewById(R.id.addExpenseButton);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dbHelper = new DBHelper(requireContext());
        db = dbHelper.getWritableDatabase();
        sharedPref = requireContext().getSharedPreferences("user_info", 0);
        username = sharedPref.getString("username", "");
        showDialog = new ShowDialog(requireContext());

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
                selectedDateTextView.setText(formattedDate);
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

    //获取消费类型
    private ArrayList<String> getExpenseTypes() {
        ArrayList<String> ExpenseTypesList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT typename FROM expense_type", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String typeName = cursor.getString(cursor.getColumnIndex("typename"));
                ExpenseTypesList.add(typeName);
            } while (cursor.moveToNext());
        }

        return ExpenseTypesList;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showExpenseTypesAndWriteIn() {
        selectedTitleTextView.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (selectedTitleTextView.getRight() - selectedTitleTextView.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    ListPopupWindow listPopupWindow = new ListPopupWindow(requireContext());

                    //获取类型列表
                    ArrayList<String> expensetypes = getExpenseTypes();
                    // 创建适配器
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, expensetypes);
                    listPopupWindow.setAdapter(adapter);

                    // 设置列表项点击事件
                    listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
                       selectedtype = expensetypes.get(position);
                       selectedtypename = selectedtype;

                        selectedTitleTextView.setText(selectedtypename);

                        listPopupWindow.dismiss();
                    });


                    // 设置ListPopupWindow的锚点及显示位置
                    listPopupWindow.setAnchorView(selectedTitleTextView);
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
        writeInAmountTextView.setOnClickListener(v -> {
            writeInAmountTextView.setText("");
        });
    }

    //将输入的内容保存到Expense类中然后存入数据库
    private boolean saveExpense() throws ParseException {
        Expense expense = getExpenseInformation();
        ContentValues values = new ContentValues();
        values.put("userID", expense.getUserID());
        values.put("expensetypeID", expense.getExpenseTypeID());
        values.put("Amount", expense.getAmount());
        values.put("Date", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(expense.getDate()));

        long result = db.insert("expense", null, values);
        return result != -1; // 返回是否成功插入数据
    }

    //获取信息，并存入Expense类
    @SuppressLint("Range")
    private Expense getExpenseInformation() throws ParseException {
        Expense expense = new Expense();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        //保存时间
        //保存typeId
        //通过typename获取typeid
        // 查询语句
        String query = "SELECT id FROM expense_type WHERE typename = ?";
        Cursor cursor = db.rawQuery(query, new String[]{selectedtypename});
        if (cursor != null && cursor.moveToFirst()) {
            typeID = cursor.getInt(cursor.getColumnIndex("id"));
        }

        //通过username查找userid
        cursor = db.rawQuery("SELECT id FROM users where username = ?", new String[]{username});
        if (cursor != null && cursor.moveToFirst()) {
            userID = cursor.getInt(cursor.getColumnIndex("id"));
        }
        amount = Double.parseDouble(writeInAmountTextView.getText().toString());

        //保存数据
        expense.setDate(sdf.parse(formattedDate));
        expense.setExpenseTypeID(typeID);
        expense.setUserID(userID);
        expense.setAmount(amount);
        return expense;
    }

    private void checkButton(){
        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!getExpenseTypes().contains(selectedTitleTextView.getText().toString()) ||
                            !writeInAmountTextView.getText().toString().matches("[0-9]+(\\.\\d+)?")) {
                        return;
                    }else {
                        saveExpense();
                        showDialog.showDialog("添加成功！");
                        selectedTitleTextView.setText("点击输入类型");
                        writeInAmountTextView.setText("输入金额");
                        selectedDateTextView.setText("点击输入日期");
                        writeInRemarkTextView.setText("输入备注");
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}