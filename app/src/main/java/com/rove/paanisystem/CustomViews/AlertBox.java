package com.rove.paanisystem.CustomViews;
/*Created by Talha Siddiqui on 19/03/2020.
 Copyright (c) 2020 Rove. All rights reserved.
*/


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.rove.paanisystem.R;


public class AlertBox extends RelativeLayout {
    private CardView alertBoxCardView;
    private View rootView;
    private ObjectAnimator animation;
    private float centreScreenY;
    private float moveDownOffset;
    private float moveUpOffset;
    private RelativeLayout cancelButtonLeft;
    private RelativeLayout okButtonRight;
    private RelativeLayout okButtonCentre;
    private LinearLayout twoButtonLayout;
    private TextView cancelButtonLeftTextView;
    private TextView okButtonCentreTextView;
    private TextView okButtonRightTextView;
    private TextView messageTextView;
    public AlertBoxButtonListener alertBoxButtonListener;

    public AlertBox(Context context) {
        super(context);
        init(context);
    }

    public AlertBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //alertBoxCardView.setY(-alertBoxCardView.getHeight());
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centreScreenY = h / 2.0f;
        moveDownOffset = (centreScreenY - (float) alertBoxCardView.getMeasuredHeight() / 2);
        moveUpOffset = -alertBoxCardView.getMeasuredHeight();
        if (centreScreenY != 0) {
            rootView.setVisibility(INVISIBLE);
        }

    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        rootView = inflater.inflate(R.layout.alert_box_layout, this);
        alertBoxCardView = rootView.findViewById(R.id.alert_box_cardview);
        messageTextView = rootView.findViewById(R.id.main_dialog_message);
        twoButtonLayout = rootView.findViewById(R.id.two_button_layout);
        cancelButtonLeft = rootView.findViewById(R.id.cencel_button_left_rl);
        okButtonRight = rootView.findViewById(R.id.ok_button_right_rl);
        okButtonCentre = rootView.findViewById(R.id.ok_button_centre_rl);
        //alertBoxCardView.setY(-200);
        //rootView.setVisibility(INVISIBLE);
    }

    public void setDialogType(boolean singleButton) {
        if (singleButton) {
            okButtonCentre.setVisibility(VISIBLE);
            twoButtonLayout.setVisibility(INVISIBLE);
            okButtonCentre.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertBoxButtonListener.okButtonCentreClick();
                }
            });
        } else {
            twoButtonLayout.setVisibility(VISIBLE);
            okButtonCentre.setVisibility(INVISIBLE);
            cancelButtonLeft.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertBoxButtonListener.cancelButtonLeftClick();
                }
            });
            okButtonRight.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertBoxButtonListener.okButtonRightClick();
                }
            });
        }
    }


    public void setDialogMessage(String message, String leftButtonText, String centreButtonText, String rightButtonText) {
        cancelButtonLeftTextView = rootView.findViewById(R.id.cancel_button_left_textview);
        okButtonRightTextView = rootView.findViewById(R.id.ok_button_right_textview);
        cancelButtonLeftTextView.setText(leftButtonText);
        okButtonRightTextView.setText(rightButtonText);
        messageTextView.setText(message);
    }

    public void setDialogMessage(String message, String centreButtonText) {
        okButtonCentreTextView = rootView.findViewById(R.id.ok_button_centre_textview);
        okButtonCentreTextView.setText(centreButtonText);
        messageTextView.setText(message);
    }

    public void setDialogMessage(String message) {
        messageTextView.setText(message);
    }


    public void showDialog() {
        if (animation != null) {
            animation.end();
        }
        alertBoxCardView.setY(-alertBoxCardView.getMeasuredHeight());
        rootView.setVisibility(VISIBLE);
        rootView.setElevation(45);
        animation = ObjectAnimator.ofFloat(alertBoxCardView, "Y", moveDownOffset);
        animation.setDuration(300);
        animation.start();

    }

  /*  public void showDialogUsingAlternativeMoveOffset(int offset) {
        alertBoxCardView.setY(-alertBoxCardView.getHeight());
        rootView.setVisibility(VISIBLE);
        ObjectAnimator animation = ObjectAnimator.ofFloat(alertBoxCardView, "Y", alternativeMoveDownOffset+offset);
        animation.setDuration(270);
        animation.start();

    }*/

    public void hideDialog() {
        animation = ObjectAnimator.ofFloat(alertBoxCardView, "Y", moveUpOffset);
        animation.setDuration(300);
        animation.start();
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                rootView.setVisibility(INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


}
