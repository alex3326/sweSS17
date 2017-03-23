package at.sw2017.calculator;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Calculator extends Activity implements View.OnClickListener{


    private Button buttonZero;
    private Button buttonOne;
    private Button buttonTwo;
    private Button buttonThree;
    private Button buttonFour;
    private Button buttonFive;
    private Button buttonSix;
    private Button buttonSeven;
    private Button buttonEight;
    private Button buttonNine;
    private Button buttonPlus;
    private Button buttonMinus;
    private Button buttonMulti;
    private Button buttonDiv;
    private Button buttonC;
    private TextView TVoutput;


    @Override
    public void onClick(View v) {


        Button clickedButton = (Button) v;

        switch (clickedButton.getId())
        {
            case R.id.buttonPlus:
                break;
            case R.id.buttonMinus:
                break;
            case R.id.buttonMulti:
                break;
            case R.id.buttonDiv:
                break;
            case R.id.buttonEqual:
                break;
            case R.id.buttonC:
                clearTexView();
                break;
            default:
                String recentNumber = TVoutput.getText().toString();
                if(recentNumber.equals("0")){
                    recentNumber = "";
                }
                recentNumber += clickedButton.getText().toString();
                TVoutput.setText(recentNumber);

        }





    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        buttonZero = (Button) findViewById(R.id.buttonZero);
        buttonOne = (Button) findViewById(R.id.buttonOne);
        buttonTwo = (Button) findViewById(R.id.buttonTwo);
        buttonThree = (Button) findViewById(R.id.buttonThree);
        buttonFour = (Button) findViewById(R.id.buttonFour);
        buttonFive = (Button) findViewById(R.id.buttonFive);
        buttonSix = (Button) findViewById(R.id.buttonSix);
        buttonSeven = (Button) findViewById(R.id.buttonSeven);
        buttonEight = (Button) findViewById(R.id.buttonEight);
        buttonNine = (Button) findViewById(R.id.buttonNine);
        buttonPlus = (Button) findViewById(R.id.buttonPlus);
        buttonMinus = (Button) findViewById(R.id.buttonMinus);
        buttonMulti = (Button) findViewById(R.id.buttonMulti);
        buttonDiv = (Button) findViewById(R.id.buttonDiv);
        buttonC = (Button) findViewById(R.id.buttonC);
        TVoutput = (TextView) findViewById(R.id.textViewOutput);

        buttonC.setOnClickListener(this);
        buttonZero.setOnClickListener(this);
        buttonOne.setOnClickListener(this);
        buttonThree.setOnClickListener(this);
        buttonTwo.setOnClickListener(this);
        buttonFour.setOnClickListener(this);
        buttonFive.setOnClickListener(this);
        buttonSeven.setOnClickListener(this);
        buttonSix.setOnClickListener(this);
        buttonEight.setOnClickListener(this);
        buttonNine.setOnClickListener(this);
        buttonPlus.setOnClickListener(this);
        buttonMinus.setOnClickListener(this);
        buttonMulti.setOnClickListener(this);
        buttonDiv.setOnClickListener(this);

        clearTexView();

    }



    private void clearTexView(){
        TVoutput.setText("0");
    }

    public int doAddition(int a, int b){
        return a + b;
    }
}
