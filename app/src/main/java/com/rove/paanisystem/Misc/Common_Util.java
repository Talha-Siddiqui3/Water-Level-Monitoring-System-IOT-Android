package com.rove.paanisystem.Misc;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class Common_Util {

    public void ToasterLong(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        /*View view=toast.getView();
        TextView text = (TextView) view.findViewById(android.R.id.message);
        text.setTextAppearance(R.style.TextRegularSmall);*/
        toast.show();

    }

    public void ToasterShort(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void saveUserDataLocally(Context context, String key, String data) {
        String sharedPrefName = "MySharedPref";
        SharedPreferences pref = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, data); // Storing data
        editor.apply();

    }

    public String getUserDataLocally(Context context, String key) {
        String result;
        String sharedPrefName = "MySharedPref";
        SharedPreferences pref = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        result = pref.getString(key, null);
        return result;
    }

    public ObjectAnimator getLoadingScaleAnim(View loadingView) {
        ObjectAnimator scaleAnim = scaleAnim = ObjectAnimator.ofPropertyValuesHolder(
                loadingView,
                PropertyValuesHolder.ofFloat("scaleX", 1f),
                PropertyValuesHolder.ofFloat("scaleY", 1f)
        );
        scaleAnim.setDuration(1000);
        scaleAnim.setRepeatMode(ValueAnimator.REVERSE);
        scaleAnim.setRepeatCount(ValueAnimator.INFINITE);
        return scaleAnim;
    }


    public void openBrowserLink(String url, Activity context) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }

}
