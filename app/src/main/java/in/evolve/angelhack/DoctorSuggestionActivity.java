package in.evolve.angelhack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DoctorSuggestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggestions_activity);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }
}
