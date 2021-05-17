package ru.zverkov_studio.split;


import android.content.Context;

import android.os.Bundle;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;


import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class PickerDate extends DialogFragment {
    private static Context mContext;
    private static EditText meditText;
    private static ImageButton set_data;
    private static DatePicker datePicker;

    public PickerDate(Context context, EditText editText) {
        mContext = context;
        meditText = editText;
    }

    public static PickerDate newInstance() {
        PickerDate fragment = new PickerDate(mContext, meditText);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getTheme() {
        return R.style.date_picker;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View picker = inflater.inflate(R.layout.picker_date, container, true);

        datePicker = (DatePicker) picker.findViewById(R.id.birthday_picker);
        set_data = (ImageButton) picker.findViewById(R.id.ok_button_date_picker);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                String dateString = sdf.format(calendar.getTime());
                meditText.setText(dateString);
                dismiss();
            }
        };

        set_data.setOnClickListener(listener);
        Window window = getDialog().getWindow();

        // set gravity
        window.setGravity(Gravity.BOTTOM|Gravity.CENTER);

        // then set the values to where you want to position it
        WindowManager.LayoutParams params = window.getAttributes();
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (180 * scale + 0.5f);
        params.y = pixels;
        window.setAttributes(params);

        return picker;
    }
}
