package com.dukeai.manageloads.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import com.dukeai.manageloads.Duke;
import com.dukeai.manageloads.R;
import com.dukeai.manageloads.interfaces.UpdateTabStylesListener;
import com.dukeai.manageloads.model.ChangeThemeModel;
import com.dukeai.manageloads.utils.Utilities;

import java.util.ArrayList;
import java.util.List;

public class DocumentsViewPagerAdapter extends FragmentPagerAdapter {
    UpdateTabStylesListener updateTabStylesListener;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private Context context;

    public DocumentsViewPagerAdapter(Context context, FragmentManager fm, UpdateTabStylesListener listener) {
        super(fm);
        this.context = context;
        this.updateTabStylesListener = listener;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        titleList.add(title);

    }

    public void setTitleList(int position, String title) {
        titleList.set(position, title);
        ((TextView) (getTabView(position).findViewById(R.id.document_tab_item))).setText(title);

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (updateTabStylesListener != null) {
            updateTabStylesListener.UpdateTabStyles();
        }
    }

    public View getTabView(int position) {
        View line;
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        String inProcessString = Utilities.getStrings(context, R.string.in_process);
        String rejectedString = Utilities.getStrings(context, R.string.rejected);
        if (Duke.fileStatusModel != null) {
            if (Duke.fileStatusModel.getInProcessDocumentsModels() != null && Duke.fileStatusModel.getInProcessDocumentsModels().size() > 0) {
                inProcessString = Utilities.getStrings(context, R.string.in_process) + "(" + Duke.fileStatusModel.getInProcessDocumentsModels().size() + ")";
            }
            if (Duke.fileStatusModel.getInProcessDocumentsModels() != null && Duke.fileStatusModel.getRejectedDocumentsModels().size() > 0) {
                rejectedString = Utilities.getStrings(context, R.string.rejected) + "(" + Duke.fileStatusModel.getRejectedDocumentsModels().size() + ")";
            }
        }

        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tv = v.findViewById(R.id.document_tab_item);
        if (position == 0) {
            tv.setText(inProcessString);
        } else if (position == 1) {
            tv.setText(getPageTitle(position));
        } else {
            tv.setText(rejectedString);
        }
        setCurrentTheme(tv);

        line = v.findViewById(R.id.item_indicator);
        return v;
    }

    private void setCurrentTheme(TextView textView) {
        ChangeThemeModel changeThemeModel = new ChangeThemeModel();
        textView.setTextColor(Color.parseColor(changeThemeModel.getTabTextColor()));
    }
}
