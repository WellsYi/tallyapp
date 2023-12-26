package com.example.tallyapp.adpter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.tallyapp.R;
import com.example.tallyapp.dbhelper.DBHelper;
import com.example.tallyapp.entity.Balances;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BanlancesAdapter extends ArrayAdapter<Balances> {

    private final ArrayList<Balances> balances;
    private final LayoutInflater inflater;


    public BanlancesAdapter(Context context, ArrayList<Balances> balances) {
        super(context, 0, balances);
        this.balances = balances;
        inflater = LayoutInflater.from(context);
    }
    public interface OnItemClickListener {
        void onItemClick(Balances balance);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Balances balance = balances.get(position);
        String typeName = balance.getTypeName();
        ViewHolder holder;


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_detail_layout, parent, false);
            holder = new ViewHolder(getContext());
            holder.typeImageView = convertView.findViewById(R.id.typeImageView);
            holder.typeTextView = convertView.findViewById(R.id.typeTextView);
            holder.amountTextView = convertView.findViewById(R.id.amountTextView);
            holder.dateTextView = convertView.findViewById(R.id.dateTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 定义类型映射
        Map<String, Integer> typeMap = new HashMap<>();
        typeMap.put("餐饮", R.mipmap.ic_food);
        typeMap.put("购物", R.mipmap.ic_shopping);
        typeMap.put("日用", R.mipmap.ic_daily_necessities);
        typeMap.put("交通", R.mipmap.ic_traffic);
        typeMap.put("水果", R.mipmap.ic_fruits);
        typeMap.put("零食", R.mipmap.ic_snacks);
        typeMap.put("运动", R.mipmap.ic_sports);
        typeMap.put("娱乐", R.mipmap.ic_amusement);
        typeMap.put("服装", R.mipmap.ic_clothing);
        typeMap.put("宠物", R.mipmap.ic_pet);
        typeMap.put("住房", R.mipmap.ic_house);
        typeMap.put("居家", R.mipmap.ic_living);
        typeMap.put("旅行", R.mipmap.ic_travel);
        typeMap.put("烟酒", R.mipmap.ic_drink_and_smoke);
        typeMap.put("数码", R.mipmap.ic_digital);
        typeMap.put("汽车", R.mipmap.ic_vehicle_repairing);
        typeMap.put("学习", R.mipmap.ic_study);
        typeMap.put("礼物", R.mipmap.ic_gift);
        typeMap.put("工资", R.mipmap.ic_salary);
        typeMap.put("兼职", R.mipmap.ic_part_time_job);
        typeMap.put("礼金", R.mipmap.ic_gift_crash);
        typeMap.put("理财", R.mipmap.ic_money_management);

        // 默认资源值
        int defaultImageResource = R.mipmap.ic_amusement; // 替换为你的默认图片资源
        String defaultAmount = ""; // 如果需要的话，设置默认的金额文本

        // 使用类型映射
        if (typeMap.containsKey(typeName)) {
            holder.typeImageView.setImageResource(typeMap.get(typeName));
            if (!typeName.equals("工资") && !typeName.equals("兼职") && !typeName.equals("礼金") && !typeName.equals("理财")) {
                holder.amountTextView.setText("-" + String.valueOf(balance.getAmount()));
            } else {
                holder.amountTextView.setText(String.valueOf(balance.getAmount()));
            }
        } else {
            holder.typeImageView.setImageResource(defaultImageResource);
            holder.amountTextView.setText(defaultAmount);
        }

        // 设置视图数据
        holder.typeTextView.setText(typeName);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        holder.dateTextView.setText(sdf.format(balance.getRecordDate()));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(balance);
                }
            }
        });


        return convertView;
    }



    public static class ViewHolder {
        ImageView typeImageView;
        TextView typeTextView;
        TextView amountTextView;
        TextView dateTextView;
        DBHelper dbHelper;
        SQLiteDatabase db;

        ViewHolder(Context context) {
            dbHelper = new DBHelper(context);
            db = dbHelper.getWritableDatabase();
        }
    }

    //通过id获取花销类名
    @SuppressLint("Range")
    private String getTypeNameById(int id) {
        ViewHolder holder;
        holder = new ViewHolder(getContext());
        String typeName = null;
        String query = "SELECT typename FROM expense_type WHERE id = ?";
        Cursor cursor = holder.db.rawQuery(query, new String[]{String.valueOf(id)});
        if (cursor != null && cursor.moveToFirst()) {
            typeName = cursor.getString(cursor.getColumnIndex("typename"));
            cursor.close();
        }
        return typeName;
    }

    // 新增方法获取特定位置的 Balances 对象
    public Balances getItemAtPosition(int position) {
        if (position >= 0 && position < balances.size()) {
            return balances.get(position);
        } else {
            return null;
        }
    }

}

