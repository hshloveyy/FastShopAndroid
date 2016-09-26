package com.sea.fastshop.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.constants.AlibcConstants;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.model.TradeResult;
import com.alibaba.baichuan.android.trade.page.AlibcAddCartPage;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcDetailPage;
import com.alibaba.baichuan.android.trade.page.AlibcMyCartsPage;
import com.alibaba.baichuan.android.trade.page.AlibcMyOrdersPage;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sea.fastshop.R;
import com.sea.fastshop.adapter.ProductListAdapter;
import com.sea.fastshop.entity.Product;
import com.sea.fastshop.utils.HttpUtils;

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

//    private Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            mPullToRefreshListView.onRefreshComplete();
//        }
//    };

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
//                getProductList();
            }
        });
//        productListAdapter = new ProductListAdapter(activity, products);
//        listView.setAdapter(productListAdapter);
//        getProductList();

        //提供给三方传递配置参数
        Map<String, String> exParams = new HashMap<>();
        exParams.put(AlibcConstants.ISV_CODE, "appisvcode");

        //商品详情page
        AlibcBasePage detailPage = new AlibcDetailPage("1111111");

        //实例化店铺打开page
//        AlibcBasePage shopPage = new AlibcShopPage(shopId);

        //实例化添加购物车打开page
//        AlibcBasePage addCardPage = new AlibcAddCartPage(itemId)

        //实例化我的订单打开page
//        AlibcBasePage ordersPage = new AlibcMyOrdersPage(status, allOrder);

        //实例化我的购物车打开page
//        AlibcBasePage myCartsPage = new AlibcMyCartsPage();

        //实例化URL打开page
//        AlibcBasePage page = new AlibcPage(taokeUrl);

        //设置页面打开方式
        AlibcShowParams showParams = new AlibcShowParams(OpenType.Native, false);

        //使用百川sdk提供默认的Activity打开detail
        AlibcTrade.show(activity, detailPage, showParams, null, exParams,
                new AlibcTradeCallback() {
                    @Override
                    public void onTradeSuccess(TradeResult tradeResult) {
                        //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
                        Log.d(TAG, "onTradeSuccess: " + tradeResult.toString());
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        //打开电商组件，用户操作中错误信息回调。code：错误码；msg：错误信息
                        Log.e(TAG, "onFailure: " + msg);
                    }
                });

        //使用自己的Activity & webview打开detail
//        AlibcTrade.show(activity, webView, webViewClient, webChromeClien, tdetailPage, showParams, null, exParams,
//                new AlibcTradeCallback() {
//                    @Override
//                    public void onTradeSuccess(TradeResult tradeResult) {
//                        //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
//                    }
//
//                    @Override
//                    public void onFailure(int code, String msg) {
//                        //打开电商组件，用户操作中错误信息回调。code：错误码；msg：错误信息
//                    }
//                });

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
                Log.d(TAG, "onStart: 下拉开始");
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
//                    mHandler.sendEmptyMessage(0);
                } catch (JSONException e) {
                    Log.e(TAG, "onSuccess: " + e.getMessage());
                } finally {
                    pd.dismiss();
                    mPullToRefreshListView.onRefreshComplete();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(activity, "请求失败", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: 请求失败");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                Log.d(TAG, "onFinish: 下拉结束");
            }
        });
    }


}
