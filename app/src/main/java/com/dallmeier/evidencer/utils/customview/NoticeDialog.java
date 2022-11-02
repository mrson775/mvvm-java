package com.dallmeier.evidencer.utils.customview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dallmeier.evidencer.R;

public abstract class NoticeDialog extends AlertDialog implements
		OnClickListener {
	protected Context mContext;
	protected String message;
	protected String title;
	protected TextView mMessageTv;

	protected NoticeDialog(Context context, String message, String title) {
		this(context);
		this.message = message;
		this.title = title;
	}

	protected NoticeDialog(Context context) {
		super(context);
		mContext = context;
	}

	private View createParentView() {
		/*View view = LayoutInflater.from(mContext).cloneInContext(
				new ContextThemeWrapper(mContext, R.style.DialogWindowTitle_Holo)
		).inflate(R.layout.notice_dialog, null);*/
		View view = View.inflate(mContext, R.layout.notice_dialog, null);
		mMessageTv = (TextView) view.findViewById(R.id.notice_message);
		mMessageTv.setText(message);

		return view;
	}

	protected void createDialogView() {
		View view = createParentView();
		setView(view);
		setTitle(title);
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

	protected abstract void onPositiveBtnPress();

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		// Cancel button
		case BUTTON_NEGATIVE:
			super.dismiss();
			break;
		// Save button
		case BUTTON_POSITIVE:
			onPositiveBtnPress();
			break;

		default:
			break;
		}
	}

}
