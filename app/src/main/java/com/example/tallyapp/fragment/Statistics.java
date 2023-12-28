package com.example.tallyapp.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.tallyapp.R;
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

import java.util.ArrayList;

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
        HorizontalBarChart horizontalBarChart = view.findViewById(R.id.horizontal_bar_chart);

        // 创建一个数据集用于存储柱状图的数据
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 10)); // 示例数据
        entries.add(new BarEntry(2, 20)); // 示例数据
        entries.add(new BarEntry(3, 30)); // 示例数据
        entries.add(new BarEntry(4, 25)); // 示例数据
        entries.add(new BarEntry(5, 15)); // 示例数据

        // 创建数据集并设置标签
        BarDataSet dataSet = new BarDataSet(entries, "Sample Data");

        // 创建 BarData 对象并将数据集添加到其中
        BarData barData = new BarData(dataSet);
        // 获取X轴和Y轴
        XAxis xAxis = horizontalBarChart.getXAxis();
        YAxis leftyAxis = horizontalBarChart.getAxisLeft(); // 或者右侧的Y轴，根据你的需求
        YAxis rightAxis = horizontalBarChart.getAxisRight();

        // 移除网格线
        xAxis.setDrawGridLines(false);
        leftyAxis.setDrawGridLines(false);
        rightAxis.setDrawGridLines(false);

        // 设置数据
        horizontalBarChart.setData(barData);

        Description description = new Description();
        description.setText("Horizontal Bar Chart Example");
        description.setTextSize(15);
        description.setTextAlign(Paint.Align.RIGHT); // 设置文本对齐方式为右对齐
        horizontalBarChart.setDescription(description);

        // 刷新图表显示
        horizontalBarChart.invalidate();

        return view;
    }
}