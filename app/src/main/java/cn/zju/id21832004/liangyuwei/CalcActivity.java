package cn.zju.id21832004.liangyuwei;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cn.iipc.android.tweetlib.SubmitProgram;
import static java.lang.System.*;

public class CalcActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn[] = new Button[16];
    private TextView txtScreen, txtResult;

    private int computationResult = 0;
    private boolean afterNumber = true;

    private int symbols[] = new int[4]; // denotes +, -, *, /
    private boolean localCal = false; // true only when * or / is used


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);

        // Connect Views
        // For figures
        for (int i=0; i<=9; i++) {
            String buttonID = "btn" + i;
            int resID = getResources().getIdentifier(buttonID, "id", this.getPackageName());
            btn[i] = (Button) findViewById(resID);
        }
        // For symbols
        btn[10] = (Button) findViewById(R.id.btnPlus);
        btn[11] = (Button) findViewById(R.id.btnMinus);
        btn[12] = (Button) findViewById(R.id.btnMultiply);
        btn[13] = (Button) findViewById(R.id.btnDivide);
        btn[14] = (Button) findViewById(R.id.btnClear);
        btn[15] = (Button) findViewById(R.id.btnEqual);
        // For TextView
        txtResult = (TextView) findViewById(R.id.txtResult);
        txtScreen = (TextView) findViewById(R.id.txtScreen);

        // Set onClickListener(Instantiate??)
        for (int i=0; i<=15; i++){
            btn[i].setOnClickListener(this);
        }

        // Set text
        txtScreen.setText("0");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hello, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            new SubmitProgram().doSubmit(this, "C2");//"B1");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int getInputNumber(int viewId){
        int id = -1;
        for (int i=0; i<=9; i++) {
            if(viewId == getResources().getIdentifier("btn" + i, "id", this.getPackageName() )){
                id = i;
            }
        }
        return id;
    }

    @Override
    public void onClick(View v) {
        // Get the view id
        int btnID = v.getId();
        String currentScreen = txtScreen.getText().toString();
        String inputChar = ((Button)v).getText().toString();
        char lastChar = currentScreen.charAt(currentScreen.length()-1);

        switch (btnID){
            case R.id.btnClear:
                txtScreen.setText("0");
                txtResult.setText("");
                break;
            case R.id.btnEqual:
                if (Character.isDigit(lastChar)) {
                    txtScreen.setText("Compute result");
                    txtResult.setText("Result");
                }
                else{
                    txtResult.setText("Error");
                }
                break;
            default:
                if ( !Character.isDigit(lastChar) && !Character.isDigit(inputChar.charAt(0)) ) { // neither is digit, i.e. both are symbols
                    txtScreen.setText(currentScreen.substring(0, currentScreen.length()-1) + inputChar);
                }
                else {
                    txtScreen.setText(currentScreen + inputChar);
                }
                break;
        }

        /*
        // Check if it's after a number
        if (afterNumber){
            // Check if the input is a number
            if (isNumber(btnID)){ // a number input

            }
            else { // a symbol input

            }

        }
        else {

        }

        if (btnID == getResources().getIdentifier("btn1", "id", this.getPackageName())){
            txtResult.setText("Clicked 1");
        };
        /*
        switch (btnID){
            case R.id.btn0:
                if (currentResult != 0){
                    currentResult = currentResult * 10;
                    txtToPrint = txtToPrint + "0";
                    txtResult.setText(txtToPrint);
                }
                break;
            case R.id.btn1:

                break;

            default:
        }
        */

    }
}

