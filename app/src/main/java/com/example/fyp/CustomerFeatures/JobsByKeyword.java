package com.example.fyp.CustomerFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.fyp.Adapters.JobKeywordAdapter;
import com.example.fyp.Adapters.MyAdapterCustomer;
import com.example.fyp.Messaging.InboxCustomer;
import com.example.fyp.ObjectClasses.Job;
import com.example.fyp.ObjectClasses.Listing;
import com.example.fyp.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class JobsByKeyword extends AppCompatActivity {

    ArrayList<Job> jobMatches =new ArrayList<Job>();
    ArrayList<Integer> prices =new ArrayList<Integer>();
    private FirebaseDatabase database;
    private DatabaseReference ref;
    String keyword;
    JobKeywordAdapter myAdapter;
    TextView t1,t2;
    BarChart barChart;
    BarDataSet set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_by_keyword);

        Intent intent = getIntent();
        keyword=intent.getStringExtra("keyword");

        database=FirebaseDatabase.getInstance();
        ref=database.getReference();

        t1=(TextView)findViewById(R.id.title);
        t2=(TextView)findViewById(R.id.priceRange);
        barChart = (BarChart) findViewById(R.id.barchart);

        t1.setText("Jobs Matching Keyword : "+keyword);

        RecyclerView mRecyclerView=(RecyclerView)findViewById(R.id.my_adapter_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        myAdapter= new JobKeywordAdapter(jobMatches);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(myAdapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.inbox:
                        Intent intent = new Intent(JobsByKeyword.this, InboxCustomer.class);
                        startActivity(intent);
                        return true;

                    case R.id.profile:
                        Intent intent1 = new Intent(JobsByKeyword.this, CustomerProfile.class);
                        startActivity(intent1);
                        return true;

                    case R.id.createListing:
                        Intent intent2 = new Intent(JobsByKeyword.this, CreateListing.class);
                        startActivity(intent2);
                        return true;

                    case R.id.myListings:
                        Intent intent3 = new Intent(JobsByKeyword.this, CustomerListingNav.class);
                        startActivity(intent3);
                        return true;


                    case R.id.home:
                        Intent intent4 = new Intent(JobsByKeyword.this, WelcomeCustomer.class);
                        startActivity(intent4);
                }

                return false;
            }
        });
        getJobs();

    }

    public void getJobs(){
        ref.child("Job").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot>children=snapshot.getChildren();
                for(DataSnapshot child:children){

                    Job job=child.getValue(Job.class);

                    assert job != null;
                    if(job.isFinished()){
                       if( (job.getJobTitle().equalsIgnoreCase(keyword)) || (job.getJobTitle().contains(keyword))|| (job.getDescription().contains(keyword))) {
                           jobMatches.add(job);
                    }
//
                    }
                    myAdapter.notifyItemInserted(jobMatches.size()-1);
                }
                getPrices();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });

    }

    private void getPrices() {

        for(Job match:jobMatches){
            prices.add(match.getPrice());
        }

        if(!prices.isEmpty()) {
            Collections.sort(prices);

            int total = 0;
            for (int i : prices) {
                total = total + i;
            }
            int average = (int) total / prices.size();

            String min = String.valueOf(prices.get(0));
            String max = String.valueOf(prices.get(prices.size() - 1));
            t2.setText("Price Range for Matching Jobs : " + "\nLowest Price :"+min + "\nHighest Price : " + max+ "\nAverage Price: "+String.valueOf(average));
            setDataOnGraph();

        }else{
            t2.setText("");
        }
    }

    public void setDataOnGraph() {
        barChart.setBackgroundColor(Color.WHITE);
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);

        barChart.setScaleYEnabled(false);
        barChart.setExtraBottomOffset(20);

        // X-Axis Style
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawLabels(false);

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.enableGridDashedLine(10f, 10f, 0f);
        yAxis.setAxisMinimum(0);
        yAxis.setAxisMaximum(prices.get(prices.size()-1)+100);
        barChart.getAxisRight().setEnabled(false);

        barChart.setFitBars(true);
        // Create a dataset
        ArrayList<BarEntry> values = new ArrayList<>();
        set = new BarDataSet(values, "Matching Job Prices");
        set.setColor(R.color.blue);
        set.setDrawValues(true);
        set.setHighLightColor(R.color.red);

        // Customize legend entry
        set.setForm(Legend.LegendForm.LINE);
        set.setFormLineWidth(3f);
        set.setFormSize(30.f);


        int x=0;
        for(int i:prices){
            x=x+1;
            BarEntry jan = new BarEntry(x, i);
            set.addEntry(jan);

        }



        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);
        set.setBarBorderWidth(1f);
        BarData data = new BarData(dataSets);
        barChart.setData(data);

    }
}