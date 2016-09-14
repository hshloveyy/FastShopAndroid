package com.sea.fastshop.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sea.fastshop.R;
import com.sea.fastshop.entity.Product;

import java.util.List;

/**
 * Created by Administrator on 2016/9/12.
 */
public class ProductListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Product> products;
    private LayoutInflater inflater;

    public ProductListAdapter(Context mContext, List<Product> products) {
        this.mContext = mContext;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.getLong(products.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder)convertView.getTag();
        } else {
            holder = new ViewHolder();
            inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.product_item, null);
            holder.mainImage = (ImageView) convertView.findViewById(R.id.mainImage);
            holder.productNameText = (TextView)convertView.findViewById(R.id.productName);
            holder.productPriceText = (TextView)convertView.findViewById(R.id.productPrice);

            convertView.setTag(holder);
        }

        Product product = products.get(position);
        holder.productNameText.setText(product.getName());
        holder.productPriceText.setText(product.getPrice());
        ImageLoader.getInstance().displayImage(product.getMainImage(), holder.mainImage, DisplayImageOptions.createSimple());
        return convertView;
    }

    class ViewHolder {
        private ImageView mainImage;
        private TextView productNameText;
        private TextView productPriceText;
    }
}
