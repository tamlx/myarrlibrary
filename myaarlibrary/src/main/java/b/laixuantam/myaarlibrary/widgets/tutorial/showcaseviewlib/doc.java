package b.laixuantam.myaarlibrary.widgets.tutorial.showcaseviewlib;

public class doc {
    /**

     package ir.smartdevelop.eram.showcaseview;

     import android.os.Bundle;
     import android.support.v7.app.AppCompatActivity;
     import android.view.View;
     import android.widget.EditText;

     import smartdevelop.ir.eram.showcaseviewlib.GuideView;
     import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
     import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;
     import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

     public class MainActivity extends AppCompatActivity {

     View view1;
     View view2;
     View view3;
     View view4;
     View view5;
     private GuideView mGuideView;
     private GuideView.Builder builder;

     @Override protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_main);

     view1 = findViewById(R.id.view1);
     view2 = findViewById(R.id.view2);
     view3 = findViewById(R.id.view3);
     view4 = findViewById(R.id.view4);
     view5 = findViewById(R.id.view5);

     builder = new GuideView.Builder(this)
     .setTitle("Guide Title Text")
     .setContentText("Guide Description Text\n .....Guide Description Text\n .....Guide Description Text .....")
     .setGravity(Gravity.center)
     .setDismissType(DismissType.outside)
     .setTargetView(view1)
     .setGuideListener(new GuideListener() {
     @Override public void onDismiss(View view) {
     switch (view.getId()) {
     case R.id.view1:
     builder.setTitle("view2 Title Text");
     builder.setContentText("Guide view2 Text\n .....Guide view2 Text\n .....Guide view2 Text .....");
     builder.setTargetView(view2).build();
     break;
     case R.id.view2:
     builder.setTitle("view3 Title Text");
     builder.setContentText("Guide view3 Text\n .....Guide view3 Text\n .....Guide view3 Text .....");
     builder.setTargetView(view3).build();
     break;
     case R.id.view3:
     builder.setTitle("view4 Title Text");
     builder.setContentText("Guide view4 Text\n .....Guide view4 Text\n .....Guide view4 Text .....");
     builder.setTargetView(view4).build();
     break;
     case R.id.view4:
     builder.setTargetView(view5).build();
     break;
     case R.id.view5:
     return;
     }
     mGuideView = builder.build();
     mGuideView.show();
     }
     });

     mGuideView = builder.build();
     mGuideView.show();

     updatingForDynamicLocationViews();

     view3.setOnClickListener(new View.OnClickListener() {
     @Override public void onClick(View v) {
     builder.setTargetView(view1).build();
     mGuideView = builder.build();
     mGuideView.show();
     }
     });
     }

     private void updatingForDynamicLocationViews() {
     view4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
     @Override public void onFocusChange(View view, boolean b) {
     mGuideView.updateGuideViewLocation();
     }
     });
     }

     }

     */
}
