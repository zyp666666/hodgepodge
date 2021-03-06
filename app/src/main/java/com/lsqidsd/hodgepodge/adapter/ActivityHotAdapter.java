package com.lsqidsd.hodgepodge.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lsqidsd.hodgepodge.R;
import com.lsqidsd.hodgepodge.ViewHolder.LoadMoreHolder;

import com.lsqidsd.hodgepodge.bean.NewsHot;
import com.lsqidsd.hodgepodge.databinding.Loadbinding;
import com.lsqidsd.hodgepodge.databinding.NewsItemHotBinding;
import com.lsqidsd.hodgepodge.viewmodel.HotViewModule;
import com.lsqidsd.hodgepodge.viewmodel.HttpModel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.List;

public class ActivityHotAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private int page;
    private LayoutInflater layoutInflater;
    private List<NewsHot.DataBean> hotBeans;
    private final int TYPE_NORMAL = 1;
    private RefreshLayout refreshLayout;
    private GridViewImgAdapter gridViewImgAdapter;
    ;
    private final int LOAD_MORE = -1;//上拉加载

    public ActivityHotAdapter(Context context, RefreshLayout refreshLayout) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.refreshLayout = refreshLayout;
        gridViewImgAdapter = new GridViewImgAdapter(context);
    }

    public void addHot(List<NewsHot.DataBean> hotBean) {
        this.hotBeans = hotBean;
        this.page = 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        NewsItemHotBinding binding;
        Loadbinding loadbinding;
        switch (viewType) {
            case TYPE_NORMAL:
                binding = DataBindingUtil.inflate(layoutInflater, R.layout.news_item_hot, parent, false);
                viewHolder = new HotViewHolder(binding);
                break;

            case LOAD_MORE:
                loadbinding = DataBindingUtil.inflate(layoutInflater, R.layout.loadmore, parent, false);
                viewHolder = new LoadMoreHolder(loadbinding);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HotViewHolder) {
            HotViewHolder hotViewHolder = (HotViewHolder) holder;
            hotViewHolder.loadData(hotBeans.get(position));
        }
        if (holder instanceof LoadMoreHolder) {
            refreshLayout.setOnLoadMoreListener(a -> HttpModel.getActivityHotNews(context,page, b -> page++, hotBeans, refreshLayout));
        }
    }

    @Override
    public int getItemCount() {
        return hotBeans.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return LOAD_MORE;
        } else {
            return TYPE_NORMAL;
        }
    }

    public class HotViewHolder extends RecyclerView.ViewHolder {
        NewsItemHotBinding itemHotBinding;

        public HotViewHolder(NewsItemHotBinding itemView) {
            super(itemView.getRoot());
            itemHotBinding = itemView;
        }

        private void loadData(NewsHot.DataBean bean) {
            if (bean.getIrs_imgs().get_$227X148() != null && bean.getIrs_imgs().get_$227X148().size() == 3) {
                gridViewImgAdapter.addImgs(bean.getIrs_imgs().get_$227X148(), bean.getUrl(), itemHotBinding);
                itemHotBinding.gv.setAdapter(gridViewImgAdapter);
            }
            itemHotBinding.setNewsitem(new HotViewModule(context, bean.getIrs_imgs().get_$227X148(), bean, itemHotBinding));
        }

    }
}
