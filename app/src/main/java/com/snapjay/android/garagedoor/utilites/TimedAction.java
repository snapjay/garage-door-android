package com.snapjay.android.garagedoor.utilites;

import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import com.snapjay.android.garagedoor.MainActivity;

public class TimedAction {


    private int openFor = 30000;
    private TextView txtTimeOpen;
    private Button cmdCancelButton;
    private CountDownTimer _timer;
    private boolean _inProgress;

    public TimedAction(){

        _inProgress = false;
        _timer = new CountDownTimer(openFor, 200) {


            public void onTick(long millisUntilFinished) {
                _inProgress = true;
                cmdCancelButton.setEnabled(true);
                txtTimeOpen.setText(millisUntilFinished / 1000 + "  seconds");
            }

            public void onFinish() {
                MainActivity z = new MainActivity();

                z.actionDoor();
                _viewReset();
            }
        };
    }

    public void setUpdateText(TextView timeOpen){
        txtTimeOpen = timeOpen;
    }
   public void setCancelButton(Button cancelButton){
        cmdCancelButton = cancelButton;
    }

    public void start (){
        _timer.start();
    }

    public void cancel (){
        _timer.cancel();
        _viewReset();
    }

    private void _viewReset(){
        cmdCancelButton.setEnabled(false);
        _inProgress = false;
        txtTimeOpen.setText(openFor / 1000 + " seconds");
    }





}
