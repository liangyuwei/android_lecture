package cn.zju.id21832004.liangyuwei;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import cn.iipc.android.tweetlib.SubmitProgram;

public class CalcActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn[] = new Button[16];
    private TextView txtScreen, txtResult;

    private int computationResult = 0;

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
        getMenuInflater().inflate(R.menu.menu_sub, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.upload_assignments:
                new SubmitProgram().doSubmit(this, "C2");//"B1");
                return true;
            case R.id.calculator:
                startActivity(new Intent(this, CalcActivity.class));
                return true;
            case R.id.post_weibo:
                startActivity(new Intent(this, StatusActivity.class));
                return true;
            case R.id.file_test:
                startActivity(new Intent(this, FileWriteActivity.class));
                return true;
            case R.id.activate_service:
                startService(new Intent(this, UpdateService.class)); //
                return true;
            case R.id.deactivate_service:
                stopService(new Intent(this, UpdateService.class));
                return true;
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.get_music_info:
                startActivity(new Intent(this, MusicActivity.class));
                return true;
            case R.id.action_close:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public int computeExpression(String expr){

        // Prep //
        ArrayList<Integer> numList = new ArrayList<Integer>();
        ArrayList<Character> signList = new ArrayList<Character>();
        int tmp = 0;

        // Parse numbers and symbols //
        for (int i=0; i<=expr.length()-1; i++){
            // get the character
            char ch = expr.charAt(i);
            // check if it's a number
            if (Character.isDigit(ch)){ // is a number
                tmp = tmp * 10 + Integer.parseInt(String.valueOf(ch));
            }
            else{  // is a symbol
                numList.add(tmp);
                tmp = 0; // reset
                signList.add(ch);
            }
        }
        numList.add(tmp); // The last one

        // Evaluate the expression //
        int numPlusMinus = 0;
        // get the number of + and - symbols
        for (int i=0; i<signList.size(); i++) {
            if (signList.get(i) == '+' || signList.get(i) == '-'){
                numPlusMinus++;
            }
        }
        int numListNew[] = new int[numPlusMinus + 1];
        char signListNew[] = new char[numPlusMinus];
        // Iterate to perform * and / operations
        tmp = numList.get(0); // get the first number
        int subid = 0;
        for (int i=0; i<signList.size(); i++){
            switch (signList.get(i)){
                case '*':
                    tmp *= numList.get(i+1);
                    break;
                case '/':
                    tmp /= numList.get(i+1);
                    break;
                default: // + or -
                    numListNew[subid] = tmp;
                    signListNew[subid] = signList.get(i); // store the sign
                    tmp = numList.get(i+1); // set to the next number
                    subid++;
                    break;
            }
        }
        numListNew[subid] = tmp; // push the last number
        // Iterate to perform + and - operations
        int result = numListNew[0];
        for (int i=0; i<signListNew.length; i++){
            if(signListNew[i] == '+'){
                result += numListNew[i+1];
            }
            else {
                result -= numListNew[i+1];
            }
        }

        return result;

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
                    //txtScreen.setText("Compute result");
                    // call function to cumpute the expression
                    computationResult = computeExpression(txtScreen.getText().toString());
                    txtResult.setText("=" + Integer.toString(computationResult));
                }
                else{
                    txtResult.setText("The last character should be a number!");
                }
                break;
            default:
                if ( !Character.isDigit(lastChar) && !Character.isDigit(inputChar.charAt(0)) ) { // neither is digit, i.e. both are symbols
                    txtScreen.setText(currentScreen.substring(0, currentScreen.length()-1) + inputChar);
                }
                else {
                    if (lastChar == '/' && inputChar.charAt(0) == '0'){ // cannot divide by 0
                        break;
                    }
                    else {
                        txtScreen.setText(currentScreen + inputChar);
                    }
                }
                break;
        }

    }
}

