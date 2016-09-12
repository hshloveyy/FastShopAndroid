package com.sea.fastshop;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 知道碎片界面
 *
 * @author wwj_748
 */
public class ZhidaoFragment extends Fragment {
    private static final String TAG = "ZhidaoFragment";

    private Activity activity;
    private PullToRefreshListView mPullToRefreshListView;

    private String[] dataList = {
            "10", "9", "8", "7", "6", "5", "4", "3", "2", "1"};

    private List<String> datas = null;
    private ListView listView;
    private ArrayAdapter<String> listAdapter;
    private int i = 11;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        View view = inflater.inflate(com.sea.fastshopandroid.R.layout.main_tab1_fragment, container,
                false);
        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(com.sea.fastshopandroid.R.id.pull_to_refresh_listview);

        listView = mPullToRefreshListView.getRefreshableView();
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                for (int index = 0;index < 2;index++) {
                    datas.add(0, (i++)+"");
                }
//                datas.add("11");
                listAdapter.notifyDataSetChanged();
                mPullToRefreshListView.onRefreshComplete();
            }
        });
        datas = new ArrayList<String>();
        for (String s : dataList) {
            datas.add(0, s);
        }
        listAdapter = new ArrayAdapter<String>(activity,android.R.layout.simple_list_item_1, datas);
        listView.setAdapter(listAdapter);
        return view;
    }


}
