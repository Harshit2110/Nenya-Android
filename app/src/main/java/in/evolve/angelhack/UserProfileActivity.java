package in.evolve.angelhack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private Button hospitalVisitButton;
    private Button scheduleButton;
    private Button suggestionButton;
    private Button medicineButton;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);

        hospitalVisitButton= (Button) findViewById(R.id.hospital_visits_button);
        scheduleButton= (Button) findViewById(R.id.schedule_button);
        suggestionButton= (Button) findViewById(R.id.suggestions_button);
        medicineButton= (Button) findViewById(R.id.medicines_button);
        toolbar= (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setTitle("Your Profile");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);

        hospitalVisitButton.setOnClickListener(this);
        scheduleButton.setOnClickListener(this);
        suggestionButton.setOnClickListener(this);
        medicineButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        int id=view.getId();

        switch (id)
        {
            case R.id.hospital_visits_button:
                Intent intent1=new Intent(UserProfileActivity.this,HospitalVisitActivity.class);
                startActivity(intent1);
                break;
            case R.id.schedule_button:
                Intent intent2=new Intent(UserProfileActivity.this,DailyScheduleActivity.class);
                startActivity(intent2);
                break;
            case R.id.medicines_button:
                Intent intent3=new Intent(UserProfileActivity.this,MedicinesActivity.class);
                startActivity(intent3);
                break;
            case R.id.suggestions_button:
                Intent intent4=new Intent(UserProfileActivity.this,DoctorSuggestionActivity.class);
                startActivity(intent4);
                break;
            case android.R.id.home:
                 UserProfileActivity.this.finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_profile,menu);
        MenuItem item=menu.getItem(0);
        item.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.my_account:
                Intent intent=new Intent(UserProfileActivity.this,MyAccountActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }
}
