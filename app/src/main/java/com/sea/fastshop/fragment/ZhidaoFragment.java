package com.sea.fastshop.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sea.fastshop.R;
import com.sea.fastshop.adapter.ProductListAdapter;
import com.sea.fastshop.entity.Product;
import com.sea.fastshop.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * 知道碎片界面
 *
 * @author wwj_748
 */
public class ZhidaoFragment extends Fragment {
    private static final String TAG = "ZhidaoFragment";

    private Activity activity;
    private PullToRefreshListView mPullToRefreshListView;

    private List<Product> products = new ArrayList<Product>();
    private ListView listView;
    private ProductListAdapter productListAdapter;
    private int pageNum = 1;
    private int pageSize = 10;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        View view = inflater.inflate(R.layout.main_tab1_fragment, container,
                false);
        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_to_refresh_listview);

        listView = mPullToRefreshListView.getRefreshableView();
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                mPullToRefreshListView.onRefreshComplete();
                getProductList();
            }
        });
        productListAdapter = new ProductListAdapter(activity, products);
        listView.setAdapter(productListAdapter);
        getProductList();
        return view;
    }

    private void getProductList() {
        final  ProgressDialog pd = new ProgressDialog(activity);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNum", pageNum + "");
        params.put("pageSize", pageSize+ "");
        HttpUtils.get("product/queryProduct.do", new RequestParams(params), new JsonHttpResponseHandler("utf-8"){
            @Override
            public void onStart() {
                super.onStart();
                pd.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if(HttpUtils.SUCCESS_CODE.equals(response.getString("code"))){
                        Type objectType = new TypeToken<List<Product>>() {}.getType();
                        products.addAll(0, (Collection<? extends Product>) new Gson().fromJson(response.getString("data"), objectType));
                        productListAdapter.notifyDataSetChanged();
                        pageNum++;
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "onSuccess: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(activity, "请求失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                pd.dismiss();
                mPullToRefreshListView.onRefreshComplete();
            }
        });
    }


}
