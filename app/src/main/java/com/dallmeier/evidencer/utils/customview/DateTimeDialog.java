package com.dallmeier.evidencer.utils.customview;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.dallmeier.evidencer.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateTimeDialog extends AlertDialog {
    protected Context mContext;
    protected String message;
    protected String title;
    protected TextView mMessageTv;
    private View view;

    protected DateTimeDialog(Context context, String message, String title) {
        this(context);
        this.message = message;
        this.title = title;
    }

    public DateTimeDialog(Context context) {
        super(context);
        mContext = context;
    }

    private View createParentView() {
		/*View view = LayoutInflater.from(mContext).cloneInContext(
				new ContextThemeWrapper(mContext, R.style.DialogWindowTitle_Holo)
		).inflate(R.layout.notice_dialog, null);*/
        View view = View.inflate(mContext, R.layout.date_time_picker, null);
		/*mMessageTv = (TextView) view.findViewById(R.id.notice_message);
		mMessageTv.setText(message);
*/
        this.view = view;
        return view;
    }

    protected void createDialogView() {
        View view = createParentView();
        setView(view);
        setTitle(title);
    }

    public long getTime() {
        DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
        TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);

        Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                datePicker.getMonth(),
                datePicker.getDayOfMonth(),
                timePicker.getCurrentHour(),
                timePicker.getCurrentMinute());
        return calendar.getTimeInMillis();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        createDialogView();
        // setButton(BUTTON_NEGATIVE, mContext.getString(R.string.ignore),
        // this);
        // setButton(BUTTON_POSITIVE, mContext.getString(R.string.settings),
        // this);
        super.onCreate(savedInstanceState);
    }

}
