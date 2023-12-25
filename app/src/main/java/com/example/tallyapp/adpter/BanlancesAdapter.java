package com.example.tallyapp.adpter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class BanlancesAdapter extends ArrayAdapter<Balances> {

    private final ArrayList<Balances> balances;
    private final LayoutInflater inflater;


    public BanlancesAdapter(Context context, ArrayList<Balances> balances) {
        super(context, 0, balances);
        this.balances = balances;
        inflater = LayoutInflater.from(context);
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

        switch (typeName) {
            case "餐饮":
                holder.typeImageView.setImageResource((R.mipmap.ic_food));
                holder.amountTextView.setText("-" + String.valueOf(balance.getAmount()));
                break;
            case "购物":
                holder.typeImageView.setImageResource(R.mipmap.ic_shopping);
                holder.amountTextView.setText("-" + String.valueOf(balance.getAmount()));
                break;
            case "日用":
                holder.typeImageView.setImageResource((R.mipmap.ic_daily_necessities));
                holder.amountTextView.setText("-" + String.valueOf(balance.getAmount()));
                break;
            case "交通":
                holder.typeImageView.setImageResource(R.mipmap.ic_traffic);
                holder.amountTextView.setText("-" + String.valueOf(balance.getAmount()));
                break;
            case "水果":
                holder.typeImageView.setImageResource((R.mipmap.ic_fruits));
                holder.amountTextView.setText("-" + String.valueOf(balance.getAmount()));
                break;
            case "零食":
                holder.typeImageView.setImageResource(R.mipmap.ic_snacks);
                holder.amountTextView.setText("-" + String.valueOf(balance.getAmount()));
                break;
            case "运动":
                holder.typeImageView.setImageResource((R.mipmap.ic_sports));
                holder.amountTextView.setText("-" + String.valueOf(balance.getAmount()));
                break;
            case "娱乐":
                holder.typeImageView.setImageResource(R.mipmap.ic_amusement);
                holder.amountTextView.setText("-" + String.valueOf(balance.getAmount()));
                break;
            case "服装":
                holder.typeImageView.setImageResource((R.mipmap.ic_clothing));
                holder.amountTextView.setText("-" + String.valueOf(balance.getAmount()));
                break;
            case "宠物":
                holder.typeImageView.setImageResource(R.mipmap.ic_pet);
                holder.amountTextView.setText("-" + String.valueOf(balance.getAmount()));
                break;
            case "住房":
                holder.typeImageView.setImageResource((R.mipmap.ic_house));
                holder.amountTextView.setText("-" + String.valueOf(balance.getAmount()));
                break;
            case "居家":
                holder.typeImageView.setImageResource(R.mipmap.ic_living);
                holder.amountTextView.setText("-" + String.valueOf(balance.getAmount()));
                break;
            case "旅行":
                holder.typeImageView.setImageResource((R.mipmap.ic_travel));
                holder.amountTextView.setText("-" + String.valueOf(balance.getAmount()));
                break;
            case "烟酒":
                holder.typeImageView.setImageResource(R.mipmap.ic_drink_and_smoke);
                holder.amountTextView.setText("-" + String.valueOf(balance.getAmount()));
                break;
            case "数码":
                holder.typeImageView.setImageResource((R.mipmap.ic_digital));
                holder.amountTextView.setText("-" + String.valueOf(balance.getAmount()));
                break;
            case "汽车":
                holder.typeImageView.setImageResource(R.mipmap.ic_vehicle_repairing);
                holder.amountTextView.setText("-" + String.valueOf(balance.getAmount()));
                break;
            case "学习":
                holder.typeImageView.setImageResource((R.mipmap.ic_study));
                holder.amountTextView.setText("-" + String.valueOf(balance.getAmount()));
                break;
            case "礼物":
                holder.typeImageView.setImageResource(R.mipmap.ic_gift);
                holder.amountTextView.setText("-" + String.valueOf(balance.getAmount()));
                break;
            case "工资":
                holder.typeImageView.setImageResource((R.mipmap.ic_salary));
                holder.amountTextView.setText(String.valueOf(balance.getAmount()));
                break;
            case "兼职":
                holder.typeImageView.setImageResource(R.mipmap.ic_part_time_job);
                holder.amountTextView.setText(String.valueOf(balance.getAmount()));
                break;
            case "礼金":
                holder.typeImageView.setImageResource((R.mipmap.ic_gift_crash));
                holder.amountTextView.setText(String.valueOf(balance.getAmount()));
                break;
            case "理财":
                holder.typeImageView.setImageResource(R.mipmap.ic_money_management);
                holder.amountTextView.setText(String.valueOf(balance.getAmount()));
                break;
        }
        // Set data to the views
        holder.typeTextView.setText(typeName);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        holder.dateTextView.setText(sdf.format(balance.getRecordDate()));

        //长按事件
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return true;
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

