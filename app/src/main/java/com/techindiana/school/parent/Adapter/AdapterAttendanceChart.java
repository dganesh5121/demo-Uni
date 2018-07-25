package com.techindiana.school.parent.Adapter;
/*
Created By: DGP 22/12/2017
Updated By: DGP
Class Name:
Class Details:
*/

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.techindiana.school.parent.R;
import com.techindiana.school.parent.Vars.Constant;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Retrofit;

/**
 * Created by TechIndiana on 26-07-2017.
 */

public class AdapterAttendanceChart extends RecyclerView.Adapter<AdapterAttendanceChart.ItemRowHolder> {
    private Context mContext;
    private ArrayList<HashMap<String, String>> aListChart;
    private Retrofit retrofit;

    public AdapterAttendanceChart(Context mContext, ArrayList<HashMap<String, String>> aListChart) {
        this.mContext = mContext;
        this.aListChart = aListChart;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_attendance_chart, null);
        //Methods.setFont(mContext, viewGroup);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, final int i) {
        try {
            // itemRowHolder.tvName.setText(aListChart.get(i).getType());
            //  itemRowHolder.tvDes.setText(aListChart.get(i).getNotification());
            // itemRowHolder.tvDate.setText(aListChart.get(i).getCreatedOn());
            ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

            // NOTE: The order of the entries when being added to the entries array determines their position around the center of
            // the chart.
            if (Integer.parseInt(aListChart.get(i).get("present")) > 0)
                entries.add(new PieEntry(Float.parseFloat(aListChart.get(i).get("present")),
                        "",
                        mContext.getResources().getDrawable(R.mipmap.ic_rating_fill)));

            if (Integer.parseInt(aListChart.get(i).get("holiday")) > 0)
                entries.add(new PieEntry(Float.parseFloat(aListChart.get(i).get("holiday")),
                        "",
                        mContext.getResources().getDrawable(R.mipmap.ic_rating_fill)));

            if (Integer.parseInt(aListChart.get(i).get("absent")) > 0)
                entries.add(new PieEntry(Float.parseFloat(aListChart.get(i).get("absent")),
                        "",
                        mContext.getResources().getDrawable(R.mipmap.ic_rating_fill)));




            PieDataSet dataSet = new PieDataSet(entries, "");

            dataSet.setDrawIcons(false);

            dataSet.setSliceSpace(3f);
            dataSet.setIconsOffset(new MPPointF(0, 40));
            dataSet.setSelectionShift(5f);


            dataSet.setColors(ColorTemplate.MATERIAL_COLORS_AST);

            //dataSet.setSelectionShift(0f);

            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.WHITE);
            data.setValueTypeface(itemRowHolder.mTfRegular);
            itemRowHolder.mChart.setData(data);

            // undo all highlights
            itemRowHolder.mChart.highlightValues(null);

            itemRowHolder.mChart.invalidate();
            itemRowHolder.tvHoliday.setText(aListChart.get(i).get("holiday")+" Days");
            itemRowHolder.tvAbsent.setText(aListChart.get(i).get("absent")+" Days");
            itemRowHolder.tvPresent.setText(aListChart.get(i).get("present")+" Days");
            itemRowHolder.tvtDays.setText(aListChart.get(i).get("totalDays")+" Days");

            itemRowHolder.tvPersent.setText(((Integer.parseInt(aListChart.get(i).get("present"))*100)/Integer.parseInt(aListChart.get(i).get("totalDays"))+"%"));
        } catch (Exception e) {
            e.getMessage();
        }
    }


    @Override
    public int getItemCount() {
        return (null != aListChart ? aListChart.size() : 0);
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {
        private TextView tvPresent, tvHoliday, tvAbsent,tvPersent,tvtDays;
        private PieChart mChart;
         LinearLayout llyParent;
        Typeface mTfRegular;

        ItemRowHolder(View view) {
            super(view);

            try {
                mChart = (PieChart) view.findViewById(R.id.chart1);
                tvPresent = (TextView) view.findViewById(R.id.present);
                tvHoliday = (TextView) view.findViewById(R.id.holiday);
                tvAbsent = (TextView) view.findViewById(R.id.absent);
                tvPersent = (TextView) view.findViewById(R.id.persent);
                tvtDays = (TextView) view.findViewById(R.id.tDays);
                mChart.setUsePercentValues(false);

                mChart.getDescription().setEnabled(false);
                mChart.setExtraOffsets(5, 10, 5, 5);

                mChart.setDragDecelerationFrictionCoef(0.95f);
                mTfRegular = Typeface.createFromAsset(mContext.getAssets(), "sansR.ttf");
                mChart.setCenterTextTypeface(mTfRegular);
               // mChart.setCenterText(generateCenterSpannableText());

                mChart.setDrawHoleEnabled(true);
                mChart.setHoleColor(Color.WHITE);

                mChart.setTransparentCircleColor(Color.WHITE);
                mChart.setTransparentCircleAlpha(110);

                mChart.setHoleRadius(58f);
                mChart.setTransparentCircleRadius(61f);

                mChart.setDrawCenterText(true);

                mChart.setRotationAngle(0);
                // enable rotation of the chart by touch
                mChart.setRotationEnabled(true);
                mChart.setHighlightPerTapEnabled(true);

                // mChart.setUnit(" €");
                // mChart.setDrawUnitsInChart(true);

                // add a selection listener
                mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                    @Override
                    public void onValueSelected(Entry e, Highlight h) {

                    }

                    @Override
                    public void onNothingSelected() {

                    }
                });

                mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

                Legend l = mChart.getLegend();
                l.setEnabled(false);
         /*       l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                l.setOrientation(Legend.LegendOrientation.VERTICAL);
                l.setDrawInside(false);
                l.setXEntrySpace(7f);
                l.setYEntrySpace(0f);
                l.setYOffset(0f);*/


                // entry label styling
                mChart.setEntryLabelColor(Color.WHITE);
                mChart.setEntryLabelTypeface(mTfRegular);
                mChart.setEntryLabelTextSize(12f);
            } catch (Exception e) {
                e.getMessage();
            }
            llyParent = (LinearLayout) view.findViewById(R.id.llRowBagpack);
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(Constant.screenWidth , LinearLayout.LayoutParams.WRAP_CONTENT);
            llyParent.setLayoutParams(parms);
            FrameLayout.LayoutParams parms1 = new FrameLayout.LayoutParams(Constant.screenWidth , Constant.screenWidth-(Constant.screenWidth/3));
            mChart.setLayoutParams(parms1);

            Typeface type = Typeface.createFromAsset(mContext.getAssets(), "sansR.ttf");
            tvPresent.setTypeface(type);
            tvAbsent.setTypeface(type);
            tvHoliday.setTypeface(type);
            tvPersent.setTypeface(type);
            tvtDays.setTypeface(type);
        }
    }





}
