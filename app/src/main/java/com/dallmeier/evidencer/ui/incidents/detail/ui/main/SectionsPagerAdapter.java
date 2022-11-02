package com.dallmeier.evidencer.ui.incidents.detail.ui.main;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.dallmeier.evidencer.R;
import com.dallmeier.evidencer.dao.AppDatabase;
import com.dallmeier.evidencer.ui.incidents.detail.ui.main.comment.CommentFragment;
import com.dallmeier.evidencer.ui.incidents.detail.ui.main.media.MediaFragment;
import com.dallmeier.evidencer.ui.incidents.detail.ui.main.timeline.TimelineFragment;
import com.dallmeier.evidencer.utils.Utils;
import com.dallmeier.evidencer.utils.VerticalImageSpan;


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_2};
    private final Context mContext;
    private long incidentId;
    private Drawable myDrawable;
    private final AppDatabase appDatabase;

    public SectionsPagerAdapter(Context context, FragmentManager fm, long incidentId, AppDatabase appDatabase) {
        super(fm);
        mContext = context;
        this.incidentId = incidentId;
        this.appDatabase = appDatabase;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return TimelineFragment.newInstance(position, incidentId);
            case 1:
                return CommentFragment.newInstance(position, incidentId);
            case 2:
                return MediaFragment.newInstance(position, incidentId);
        }
        return TimelineFragment.newInstance(position, incidentId);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return customTitle(position);
    }

    public SpannableStringBuilder customTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                myDrawable = Utils.getDrawableAttr(mContext, new int[]{R.attr.ic_clock});
                break;
            case 1:
                myDrawable = Utils.getDrawableAttr(mContext, new int[]{R.attr.ic_comment});
                title = "(" + appDatabase.incidentDao().getIncidentById(incidentId).getNumberComment() + ")";
                break;
            case 2:
                myDrawable = Utils.getDrawableAttr(mContext, new int[]{R.attr.ic_key});
                title = "(" + appDatabase.incidentDao().getIncidentById(incidentId).getNumberAllEvidences() + ")";
                break;
        }

        SpannableStringBuilder sb = new SpannableStringBuilder("   " + title); // space added before text for convenience
        try {
            myDrawable.setBounds(5, 5, myDrawable.getIntrinsicWidth(), myDrawable.getIntrinsicHeight());
            VerticalImageSpan span = new VerticalImageSpan(myDrawable);
            sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return sb;
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return TAB_TITLES.length;
    }
}