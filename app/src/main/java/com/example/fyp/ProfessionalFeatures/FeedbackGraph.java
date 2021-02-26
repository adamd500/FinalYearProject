package com.example.fyp.ProfessionalFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.fyp.ObjectClasses.Job;
import com.example.fyp.ObjectClasses.Listing;
import com.example.fyp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FeedbackGraph extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener,OnChartValueSelectedListener {
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseUser user;
    private String uid;
    LineChart chart;
    ArrayList<Job>allJobs=new ArrayList<Job>();
    ArrayList<Job>january = new ArrayList<>();
    ArrayList<Job>february = new ArrayList<>();
    ArrayList<Job>march = new ArrayList<>();
    ArrayList<Job>april = new ArrayList<>();
    ArrayList<Job>may = new ArrayList<>();
    ArrayList<Job>june  = new ArrayList<>();
    ArrayList<Job>july = new ArrayList<>();
    ArrayList<Job>august = new ArrayList<>();
    ArrayList<Job>september = new ArrayList<>();
    ArrayList<Job>october = new ArrayList<>();
    ArrayList<Job>november = new ArrayList<>();
    ArrayList<Job>december = new ArrayList<>();

    private SeekBar seekBarX, seekBarY;
    private TextView tvX, tvY;

    int janEarnings,febEarnings,marEarnings,aprilEarnings,mayEarnings,juneEarnings,julyEarnings,augEarnings,sepEarnings,octEarnings,novEarnings,decEarnings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_graph);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        chart=(LineChart)findViewById(R.id.chart1);
        setTitle("LineChartActivity1");

        tvX = findViewById(R.id.tvXMax);
        tvY = findViewById(R.id.tvYMax);

        getJobs();
      //  filterJobs();

        seekBarX = findViewById(R.id.seekBar1);
        seekBarX.setOnSeekBarChangeListener(this);

        seekBarY = findViewById(R.id.seekBar2);
        seekBarY.setMax(180);
        seekBarY.setOnSeekBarChangeListener( this);


        {   // // Chart Style // //
            chart = findViewById(R.id.chart1);

            // background color
            chart.setBackgroundColor(Color.WHITE);

            // disable description text
            chart.getDescription().setEnabled(false);

            // enable touch gestures
            chart.setTouchEnabled(true);

            // set listeners
            chart.setOnChartValueSelectedListener(this);
            chart.setDrawGridBackground(false);

            // create marker to display box when values are selected
            //MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

            IMarker marker = new IMarker() {
                @Override
                public MPPointF getOffset() {
                    return null;
                }

                @Override
                public MPPointF getOffsetForDrawingAtPoint(float posX, float posY) {
                    return null;
                }

                @Override
                public void refreshContent(Entry e, Highlight highlight) {

                }

                @Override
                public void draw(Canvas canvas, float posX, float posY) {

                }
            };
          //  mv.setChartView(chart);

            chart.setMarker(marker);

            // enable scaling and dragging
            chart.setDragEnabled(true);
            chart.setScaleEnabled(true);
            // chart.setScaleXEnabled(true);
            // chart.setScaleYEnabled(true);

            // force pinch zoom along both axis
            chart.setPinchZoom(true);
        }

        XAxis xAxis;
        {   // // X-Axis Style // //
            xAxis = chart.getXAxis();

            // vertical grid lines
            xAxis.enableGridDashedLine(10f, 0f, 0f);
            xAxis.setAxisMaximum(1);
            xAxis.setAxisMaximum(12);
            xAxis.setLabelCount(12);
        }

        YAxis yAxis;
        {   // // Y-Axis Style // //
            yAxis = chart.getAxisLeft();

            // disable dual axis (only use LEFT axis)
            chart.getAxisRight().setEnabled(false);
            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f);

            // axis range
            yAxis.setAxisMaximum(2000f);
            yAxis.setAxisMinimum(0f);
        }
        {   // // Create Limit Lines // //
            LimitLine llXAxis = new LimitLine(9f, "Index 10");
            llXAxis.setLineWidth(4f);
            llXAxis.enableDashedLine(10f, 10f, 0f);
            llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            llXAxis.setTextSize(10f);
       //     llXAxis.setTypeface(tfRegular);

            LimitLine ll1 = new LimitLine(150f, "Upper Limit");
            ll1.setLineWidth(4f);
            ll1.enableDashedLine(10f, 10f, 0f);
            ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
         //   ll1.setTextSize(10f);
           // ll1.setTypeface(tfRegular);

            LimitLine ll2 = new LimitLine(-30f, "Lower Limit");
            ll2.setLineWidth(4f);
            ll2.enableDashedLine(10f, 10f, 0f);
            ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            ll2.setTextSize(10f);
       //     ll2.setTypeface(tfRegular);

            // draw limit lines behind data instead of on top
        //    yAxis.setDrawLimitLinesBehindData(true);
          //  xAxis.setDrawLimitLinesBehindData(true);

            // add limit lines
          //  yAxis.addLimitLine(ll1);
         //   yAxis.addLimitLine(ll2);
            //xAxis.addLimitLine(llXAxis);
        }

        // add data
       // seekBarX.setProgress(45);
       // seekBarY.setProgress(180);
        setData(45, 180);

        // draw points over time
        chart.animateX(1500);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // draw legend entries as lines
        l.setForm(Legend.LegendForm.LINE);
    }

    public void getJobs(){
        ref.child("Job").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Job job = child.getValue(Job.class);
                    if (job.getProfessionalId().equals(uid)) {
                        String date=job.getEndDate();
                        String parts[]=date.split("/");
                        String month=parts[1];
                       // allJobs.add(job);
                        if(month.equals("01")){
                            january.add(job);
                        }else if(month.equals("02")){
                            february.add(job);
                        }else if(month.equals("03")){
                            march.add(job);
                        }else if(month.equals("04")){
                            april.add(job);
                        }else if(month.equals("05")){
                            may.add(job);
                        }else if(month.equals("06")){
                            june.add(job);
                        }else if(month.equals("07")){
                            july.add(job);
                        }else if(month.equals("08")){
                            august.add(job);
                        }else if(month.equals("09")){
                            september.add(job);
                        }else if(month.equals("10")){
                            october.add(job);
                        }else if(month.equals("11")){
                            november.add(job);
                        }else if(month.equals("12")){
                            december.add(job);
                        }
                    }
               //     calculate();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }
//    public void filterJobs(){
//
//        for(Job job: allJobs){
//            String date=job.getEndDate();
//            String parts[]=date.split("/");
//            String month=parts[1];
//
//            if(month.equals("01")){
//                january.add(job);
//            }else if(month.equals("02")){
//                february.add(job);
//            }else if(month.equals("03")){
//                march.add(job);
//            }else if(month.equals("04")){
//                april.add(job);
//            }else if(month.equals("05")){
//                may.add(job);
//            }else if(month.equals("06")){
//                june.add(job);
//            }else if(month.equals("07")){
//                july.add(job);
//            }else if(month.equals("08")){
//                august.add(job);
//            }else if(month.equals("09")){
//                september.add(job);
//            }else if(month.equals("10")){
//                october.add(job);
//            }else if(month.equals("11")){
//                november.add(job);
//            }else if(month.equals("12")){
//                december.add(job);
//            }
//        }
//
//        calculate();
//
//    }
    public void calculate(){

        int janTotal=0;
        for(Job job:january){
            janTotal=janTotal+job.getPrice();
        }
   //     janEarnings=janTotal/january.size();



        int febTotal=0;
        for(Job job:february){
            febTotal=febTotal+job.getPrice();
        }
   //     febEarnings=febTotal/february.size();



        int marTotal=0;
        for(Job job:march){
            marTotal=marTotal+job.getPrice();
        }
     //   marEarnings=marTotal/march.size();



        int aprTotal=0;
        for(Job job:april){
            aprTotal=aprTotal+job.getPrice();
        }
     //   aprilEarnings=aprTotal/april.size();


        int mayTotal=0;
        for(Job job:may){
            mayTotal=mayTotal+job.getPrice();
        }
     //   mayEarnings=mayTotal/may.size();


        int juneTotal=0;
        for(Job job:june){
            juneTotal=juneTotal+job.getPrice();
        }
    //    juneEarnings=juneTotal/june.size();

        int julTotal=0;
        for(Job job:july){
            julTotal=julTotal+job.getPrice();
        }
      //  julyEarnings=janTotal/july.size();

        int augTotal=0;
        for(Job job:august){
            augTotal=augTotal+job.getPrice();
        }
     //   augEarnings=janTotal/august.size();

//        int janTotal=0;
//        for(Job job:january){
//            janTotal=janTotal+job.getPrice();
//        }
//        janEarnings=janTotal/january.size();
//
//
//        int janTotal=0;
//        for(Job job:january){
//            janTotal=janTotal+job.getPrice();
//        }
//        janEarnings=janTotal/january.size();
//
//
//        int janTotal=0;
//        for(Job job:january){
//            janTotal=janTotal+job.getPrice();
//        }
//        janEarnings=janTotal/january.size();
//
//
//
//        int janTotal=0;
//        for(Job job:january){
//            janTotal=janTotal+job.getPrice();
//        }
//        janEarnings=janTotal/january.size();


    }
    private void setData(int count, float range) {

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            float val = (float) (Math.random() * range) - 30;
            values.add(new Entry(i, val, getResources().getDrawable(R.drawable.ic_launcher_background)));
        }

        LineDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet 1");

            set1.setDrawIcons(false);

            // draw dashed line
            set1.enableDashedLine(10f, 5f, 0f);

            // black lines and points
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);

            // line thickness and point size
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);

            // draw points as solid circles
            set1.setDrawCircleHole(false);

            // customize legend entry
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            // text size of values
            set1.setValueTextSize(9f);

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f);

            // set the filled area
            set1.setDrawFilled(true);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });

            // set color of filled area
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_launcher_foreground);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);

            // set data
            chart.setData(data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.line, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.viewGithub: {
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/LineChartActivity1.java"));
//                startActivity(i);
//                break;
//            }
//            case R.id.actionToggleValues: {
//                List<ILineDataSet> sets = chart.getData()
//                        .getDataSets();
//
//                for (ILineDataSet iSet : sets) {
//
//                    LineDataSet set = (LineDataSet) iSet;
//                    set.setDrawValues(!set.isDrawValuesEnabled());
//                }
//
//                chart.invalidate();
//                break;
//            }
//            case R.id.actionToggleIcons: {
//                List<ILineDataSet> sets = chart.getData()
//                        .getDataSets();
//
//                for (ILineDataSet iSet : sets) {
//
//                    LineDataSet set = (LineDataSet) iSet;
//                    set.setDrawIcons(!set.isDrawIconsEnabled());
//                }
//
//                chart.invalidate();
//                break;
//            }
//            case R.id.actionToggleHighlight: {
//                if(chart.getData() != null) {
//                    chart.getData().setHighlightEnabled(!chart.getData().isHighlightEnabled());
//                    chart.invalidate();
//                }
//                break;
//            }
//            case R.id.actionToggleFilled: {
//
//                List<ILineDataSet> sets = chart.getData()
//                        .getDataSets();
//
//                for (ILineDataSet iSet : sets) {
//
//                    LineDataSet set = (LineDataSet) iSet;
//                    if (set.isDrawFilledEnabled())
//                        set.setDrawFilled(false);
//                    else
//                        set.setDrawFilled(true);
//                }
//                chart.invalidate();
//                break;
//            }
//            case R.id.actionToggleCircles: {
//                List<ILineDataSet> sets = chart.getData()
//                        .getDataSets();
//
//                for (ILineDataSet iSet : sets) {
//
//                    LineDataSet set = (LineDataSet) iSet;
//                    if (set.isDrawCirclesEnabled())
//                        set.setDrawCircles(false);
//                    else
//                        set.setDrawCircles(true);
//                }
//                chart.invalidate();
//                break;
//            }
//            case R.id.actionToggleCubic: {
//                List<ILineDataSet> sets = chart.getData()
//                        .getDataSets();
//
//                for (ILineDataSet iSet : sets) {
//
//                    LineDataSet set = (LineDataSet) iSet;
//                    set.setMode(set.getMode() == LineDataSet.Mode.CUBIC_BEZIER
//                            ? LineDataSet.Mode.LINEAR
//                            :  LineDataSet.Mode.CUBIC_BEZIER);
//                }
//                chart.invalidate();
//                break;
//            }
//            case R.id.actionToggleStepped: {
//                List<ILineDataSet> sets = chart.getData()
//                        .getDataSets();
//
//                for (ILineDataSet iSet : sets) {
//
//                    LineDataSet set = (LineDataSet) iSet;
//                    set.setMode(set.getMode() == LineDataSet.Mode.STEPPED
//                            ? LineDataSet.Mode.LINEAR
//                            :  LineDataSet.Mode.STEPPED);
//                }
//                chart.invalidate();
//                break;
//            }
//            case R.id.actionToggleHorizontalCubic: {
//                List<ILineDataSet> sets = chart.getData()
//                        .getDataSets();
//
//                for (ILineDataSet iSet : sets) {
//
//                    LineDataSet set = (LineDataSet) iSet;
//                    set.setMode(set.getMode() == LineDataSet.Mode.HORIZONTAL_BEZIER
//                            ? LineDataSet.Mode.LINEAR
//                            :  LineDataSet.Mode.HORIZONTAL_BEZIER);
//                }
//                chart.invalidate();
//                break;
//            }
//            case R.id.actionTogglePinch: {
//                if (chart.isPinchZoomEnabled())
//                    chart.setPinchZoom(false);
//                else
//                    chart.setPinchZoom(true);
//
//                chart.invalidate();
//                break;
//            }
//            case R.id.actionToggleAutoScaleMinMax: {
//                chart.setAutoScaleMinMaxEnabled(!chart.isAutoScaleMinMaxEnabled());
//                chart.notifyDataSetChanged();
//                break;
//            }
//            case R.id.animateX: {
//                chart.animateX(2000);
//                break;
//            }
//            case R.id.animateY: {
//                chart.animateY(2000, Easing.EaseInCubic);
//                break;
//            }
//            case R.id.animateXY: {
//                chart.animateXY(2000, 2000);
//                break;
//            }
//            case R.id.actionSave: {
//                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    saveToGallery();
//                } else {
//                    requestStoragePermission(chart);
//                }
//                break;
//            }
//        }
//        return true;
//    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        tvX.setText(String.valueOf(seekBarX.getProgress()));
        tvY.setText(String.valueOf(seekBarY.getProgress()));

        setData(seekBarX.getProgress(), seekBarY.getProgress());

        // redraw
        chart.invalidate();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

//    @Override
//    protected void saveToGallery() {
//        saveToGallery(chart, "LineChartActivity1");
//    }


}
