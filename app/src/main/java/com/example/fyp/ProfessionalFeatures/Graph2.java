package com.example.fyp.ProfessionalFeatures;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.fyp.Messaging.InboxProfessional;
import com.example.fyp.ObjectClasses.Job;
import com.example.fyp.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.model.GradientColor;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Graph2 extends AppCompatActivity {

    BarDataSet set;
    BarChart barChart;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseUser user;
    private String uid;
    ArrayList<String> xAxisLabels = new ArrayList<>();
    public static ArrayList<Job> january = new ArrayList<>();
    public static ArrayList<Job> february = new ArrayList<>();
    public static ArrayList<Job> march = new ArrayList<>();
    public static ArrayList<Job> april = new ArrayList<>();
    public static ArrayList<Job> may = new ArrayList<>();
    public static ArrayList<Job> june = new ArrayList<>();
    public static ArrayList<Job> july = new ArrayList<>();
    public static ArrayList<Job> august = new ArrayList<>();
    public static ArrayList<Job> september = new ArrayList<>();
    public static ArrayList<Job> october = new ArrayList<>();
    public static ArrayList<Job> november = new ArrayList<>();
    public static ArrayList<Job> december = new ArrayList<>();
    int janTotal = 0;
    int febTotal = 0;
    int marTotal = 0;
    int aprTotal = 0;
    int mayTotal = 0;
    int juneTotal = 0;
    int julTotal = 0;
    int augTotal = 0;
    int sepTotal = 0;
    int octTotal = 0;
    int novTotal = 0;
    int decTotal = 0;
    TextView t1, t2, t3, t4;
    Spinner spinner;
    int total;
    int takeHome;

    int l = january.size() + february.size() + march.size() + april.size() + may.size() + june.size() + july.size() + august.size() + september.size()
            + october.size() + november.size() + december.size();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph2);


        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        getJobs();

        t1 = (TextView) findViewById(R.id.totalEarned);
        t2 = (TextView) findViewById(R.id.totalTakeHome);
        t3 = (TextView) findViewById(R.id.jobsCompleted);
        t4 = (TextView) findViewById(R.id.average);

        spinner = (Spinner) findViewById(R.id.spinner);

        barChart = (BarChart) findViewById(R.id.barchart);

        xAxisLabels.add("Jan");
        xAxisLabels.add("Feb");
        xAxisLabels.add("Mar");
        xAxisLabels.add("Apr");
        xAxisLabels.add("May");
        xAxisLabels.add("Jun");
        xAxisLabels.add("Jul");
        xAxisLabels.add("Aug");
        xAxisLabels.add("Sep");
        xAxisLabels.add("Oct");
        xAxisLabels.add("Nov");
        xAxisLabels.add("Dec");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.inbox:
                        Intent intent = new Intent(getApplicationContext(), InboxProfessional.class);
                        startActivity(intent);
                        return true;

                    case R.id.profile:
                        Intent intent1 = new Intent(getApplicationContext(), ProfileHomePage.class);
                        startActivity(intent1);
                        return true;

                    case R.id.work:
                        Intent intent2 = new Intent(getApplicationContext(), WorkHomepage.class);
                        startActivity(intent2);
                        return true;

                    case R.id.stats:
                        Intent intent3 = new Intent(getApplicationContext(), ViewProfessionalFeedback.class);
                        intent3.putExtra("professionalId", uid);
                        startActivity(intent3);
                        return true;

                    case R.id.home:
                        Intent intent4 = new Intent(getApplicationContext(), WelcomeProfessional.class);
                        startActivity(intent4);
                }

                return false;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int i = spinner.getSelectedItemPosition();
                if (i == 0) {
                    if (total != 0) {
                        t1.setText(String.valueOf(total));
                        t2.setText( String.valueOf(takeHome));
                        t3.setText( String.valueOf(l));
                        t4.setText(String.valueOf(takeHome / l));
                    }
                }
                if (i == 1) {
                    t1.setText(String.valueOf(janTotal));
                    t2.setText( String.valueOf(janTotal * 0.9));
                    t3.setText( String.valueOf(january.size()));
                    t4.setText( String.valueOf((janTotal * 0.9) / january.size()));
                }
                if (i == 2) {
                    t1.setText( String.valueOf(febTotal));
                    t2.setText(String.valueOf(febTotal * 0.9));
                    t3.setText(  String.valueOf(february.size()));
                    t4.setText("Average Earn per Job : " + String.valueOf((febTotal * 0.9) / february.size()));
                }
                if (i == 3) {
                    t1.setText( String.valueOf(marTotal));
                    t2.setText( String.valueOf(marTotal * 0.9));
                    t3.setText( String.valueOf(march.size()));
                    t4.setText( String.valueOf((marTotal * 0.9) / march.size()));
                }
                if (i == 4) {
                    t1.setText(String.valueOf(aprTotal));
                    t2.setText( String.valueOf(aprTotal * 0.9));
                    t3.setText(String.valueOf(april.size()));
                    t4.setText( String.valueOf((aprTotal * 0.9) / april.size()));
                }
                if (i == 5) {
                    t1.setText( String.valueOf(mayTotal));
                    t2.setText(String.valueOf(mayTotal * 0.9));
                    t3.setText( String.valueOf(may.size()));
                    t4.setText(String.valueOf((mayTotal * 0.9) / may.size()));
                }
                if (i == 6) {
                    t1.setText(String.valueOf(juneTotal));
                    t2.setText( String.valueOf(juneTotal * 0.9));
                    t3.setText(String.valueOf(june.size()));
                    t4.setText(String.valueOf((juneTotal * 0.9) / june.size()));
                }
                if (i == 7) {
                    t1.setText( String.valueOf(julTotal));
                    t2.setText(String.valueOf(julTotal * 0.9));
                    t3.setText( String.valueOf(july.size()));
                    t4.setText( String.valueOf((julTotal * 0.9) / july.size()));
                }
                if (i == 8) {
                    t1.setText( String.valueOf(augTotal));
                    t2.setText( String.valueOf(augTotal * 0.9));
                    t3.setText( String.valueOf(august.size()));
                    t4.setText( String.valueOf((augTotal * 0.9) / august.size()));
                }
                if (i == 9) {
                    t1.setText(String.valueOf(sepTotal));
                    t2.setText(String.valueOf(sepTotal * 0.9));
                    t3.setText(String.valueOf(september.size()));
                    t4.setText(String.valueOf((sepTotal * 0.9) / september.size()));
                }
                if (i == 10) {
                    t1.setText( String.valueOf(octTotal));
                    t2.setText(String.valueOf(octTotal * 0.9));
                    t3.setText(String.valueOf(october.size()));
                    t4.setText( String.valueOf((octTotal * 0.9) / october.size()));
                }
                if (i == 11) {
                    t1.setText( String.valueOf(novTotal));
                    t2.setText(String.valueOf(novTotal * 0.9));
                    t3.setText( String.valueOf(november.size()));
                    t4.setText( String.valueOf((novTotal * 0.9) / november.size()));
                }
                if (i == 12) {
                    t1.setText( String.valueOf(decTotal));
                    t2.setText(String.valueOf(decTotal * 0.9));
                    t3.setText(String.valueOf(december.size()));
                    t4.setText( String.valueOf((decTotal * 0.9) / december.size()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }


    public void getJobs() {
        ref.child("Job").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Job job = child.getValue(Job.class);
                    if (job.getProfessionalId().equals(uid) && job.isFinished()) {
                        String date = job.getEndDate();
                        String parts[] = date.split("/");
                        String month = parts[1];
                        if (month.equals("01")) {
                            january.add(job);
                        } else if (month.equals("02")) {
                            february.add(job);
                        } else if (month.equals("03")) {
                            march.add(job);
                        } else if (month.equals("04")) {
                            april.add(job);
                        } else if (month.equals("05")) {
                            may.add(job);
                        } else if (month.equals("06")) {
                            june.add(job);
                        } else if (month.equals("07")) {
                            july.add(job);
                        } else if (month.equals("08")) {
                            august.add(job);
                        } else if (month.equals("09")) {
                            september.add(job);
                        } else if (month.equals("10")) {
                            october.add(job);
                        } else if (month.equals("11")) {
                            november.add(job);
                        } else if (month.equals("12")) {
                            december.add(job);
                        }
                    }
                }
                calculate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }

    public void calculate() {

        for (Job job : january) {
            janTotal = janTotal + job.getPrice();
        }
        for (Job job : february) {
            febTotal = febTotal + job.getPrice();
        }
        for (Job job : march) {
            marTotal = marTotal + job.getPrice();
        }
        for (Job job : april) {
            aprTotal = aprTotal + job.getPrice();
        }
        for (Job job : may) {
            mayTotal = mayTotal + job.getPrice();
        }
        for (Job job : june) {
            juneTotal = juneTotal + job.getPrice();
        }
        for (Job job : july) {
            julTotal = julTotal + job.getPrice();
        }
        for (Job job : august) {
            augTotal = augTotal + job.getPrice();
        }
        for (Job job : september) {
            sepTotal = sepTotal + job.getPrice();
        }
        for (Job job : october) {
            octTotal = octTotal + job.getPrice();
        }
        for (Job job : november) {
            novTotal = novTotal + job.getPrice();
        }
        for (Job job : december) {
            decTotal = decTotal + job.getPrice();
        }
        setDataOnGraph();
    }

    public void setDataOnGraph() {
        barChart.setBackgroundColor(Color.WHITE);
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);

        barChart.setScaleYEnabled(false);
        barChart.setExtraBottomOffset(20);

        // X-Axis Style
        XAxis xAxis = barChart.getXAxis();
        //  xAxis.enableGridDashedLine(10f, 10f, 0f);
        //xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //  xAxis.setAxisMaximum(12);
//        xAxis.setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value) {
//
//                return xAxisLabels.get((int)value);
//            }
//        });
        // Y-Axis Style
        YAxis yAxis = barChart.getAxisLeft();
        yAxis.enableGridDashedLine(10f, 10f, 0f);
        yAxis.setAxisMinimum(0);
        yAxis.setAxisMaximum(2000);
        barChart.getAxisRight().setEnabled(false);

        barChart.setFitBars(true);
        // Create a dataset
        ArrayList<BarEntry> values = new ArrayList<>();
        set = new BarDataSet(values, "Total Earnings per Month");
        set.setColor(R.color.blue);
        set.setDrawValues(true);
        set.setHighLightColor(R.color.red);

        // Customize legend entry
        set.setForm(Legend.LegendForm.LINE);
        set.setFormLineWidth(3f);
        set.setFormSize(30.f);

        BarEntry jan = new BarEntry(1, janTotal);
        BarEntry feb = new BarEntry(2, febTotal);
        BarEntry mar = new BarEntry(3, marTotal);
        BarEntry apr = new BarEntry(4, aprTotal);
        BarEntry m = new BarEntry(5, mayTotal);
        BarEntry jun = new BarEntry(6, juneTotal);
        BarEntry jul = new BarEntry(7, julTotal);
        BarEntry aug = new BarEntry(8, augTotal);
        BarEntry sep = new BarEntry(9, sepTotal);
        BarEntry oct = new BarEntry(10, octTotal);
        BarEntry nov = new BarEntry(11, novTotal);
        BarEntry dec = new BarEntry(12, decTotal);

        set.addEntry(jan);
        set.addEntry(feb);
        set.addEntry(mar);
        set.addEntry(apr);
        set.addEntry(m);
        set.addEntry(jun);
        set.addEntry(jul);
        set.addEntry(aug);
        set.addEntry(sep);
        set.addEntry(oct);
        set.addEntry(nov);
        set.addEntry(dec);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);
        set.setBarBorderWidth(1f);
        BarData data = new BarData(dataSets);
        barChart.setData(data);

        total = janTotal + febTotal + marTotal + aprTotal + mayTotal + juneTotal + julTotal + augTotal + sepTotal + octTotal + novTotal + decTotal;
        takeHome = (int) (total * 0.9);

        l = january.size() + february.size() + march.size() + april.size() + may.size() + june.size() + july.size() + august.size() + september.size()
                + october.size() + november.size() + december.size();


        t1.setText("Total Gross Earnings : " + String.valueOf(total));
        t2.setText("Total Take Home : " + String.valueOf(takeHome));
        t3.setText("Number of Jobs Completed : " + String.valueOf(l));
        t4.setText("Average Earn per Job : " + String.valueOf(total / l));

    }

    public void viewJobs(View view) {

        Intent intent = new Intent(getApplicationContext(), CompletedSelectedJobs.class);
        String selectedMonth = null;
        int i = spinner.getSelectedItemPosition();
        if (i == 0) {
            if (total != 0) {
                selectedMonth = "Whole Year";
            }
        }
        if (i == 1) {
            selectedMonth = "January";
        }
        if (i == 2) {
            selectedMonth = "February";

        }
        if (i == 3) {
            selectedMonth = "March";
        }
        if (i == 4) {
            selectedMonth = "April";
        }
        if (i == 5) {
            selectedMonth = "May";
        }
        if (i == 6) {
            selectedMonth = "June";
        }
        if (i == 7) {
            selectedMonth = "July";
        }
        if (i == 8) {
            selectedMonth = "August";
        }
        if (i == 9) {
            selectedMonth = "September";
        }
        if (i == 10) {
            selectedMonth = "October";
        }
        if (i == 11) {
            selectedMonth = "November";
        }
        if (i == 12) {
            selectedMonth = "December";
        }
        intent.putExtra("month", selectedMonth);
        startActivity(intent);

    }


}