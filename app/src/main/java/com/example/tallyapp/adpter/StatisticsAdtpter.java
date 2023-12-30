package com.example.tallyapp.adpter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tallyapp.R;
import com.example.tallyapp.dbhelper.DBHelper;
import com.example.tallyapp.entity.Balances;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatisticsAdtpter extends ArrayAdapter<Balances> {
    private final ArrayList<Balances> balances;
    private final LayoutInflater inflater;
    public StatisticsAdtpter(Context context, ArrayList<Balances> balances) {
        super(context, 0, balances);
        this.balances = balances;
        inflater = LayoutInflater.from(context);
    }
    public interface OnItemClickListener {
        void onItemClick(Balances balance);
    }
    private StatisticsAdtpter.OnItemClickListener mListener;

    public void setOnItemClickListener(StatisticsAdtpter.OnItemClickListener listener) {
        mListener = listener;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Balances balance = balances.get(position);
        String typeName = balance.getTypeName();
        ViewHolder viewHolder;


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_statistics_layout, parent, false);
            viewHolder = new StatisticsAdtpter.ViewHolder(getContext());
            viewHolder.typeImageView = convertView.findViewById(R.id.typeImageView);
            viewHolder.typeTextView = convertView.findViewById(R.id.typeTextView);
            viewHolder.amountTextView = convertView.findViewById(R.id.amountTextView);
            convertView.setTag(viewHolder);
        } else {
           viewHolder = (ViewHolder) convertView.getTag();
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
            viewHolder.typeImageView.setImageResource(typeMap.get(typeName));
            if (!typeName.equals("工资") && !typeName.equals("兼职") && !typeName.equals("礼金") && !typeName.equals("理财")) {
                viewHolder.amountTextView.setText("-￥" + String.valueOf(balance.getAmount()));
            } else {
                viewHolder.amountTextView.setText("￥" + String.valueOf(balance.getAmount()));
            }
        } else {
            viewHolder.typeImageView.setImageResource(defaultImageResource);
            viewHolder.amountTextView.setText(defaultAmount);
        }

        // 设置视图数据
        viewHolder.typeTextView.setText(typeName);
        return convertView;
    }

    public static class ViewHolder {
        ImageView typeImageView;
        TextView typeTextView;
        TextView amountTextView;
        TextView percentageTextView;
        DBHelper dbHelper;
        SQLiteDatabase db;

        ViewHolder(Context context) {
            dbHelper = new DBHelper(context);
            db = dbHelper.getWritableDatabase();
        }
    }

}
