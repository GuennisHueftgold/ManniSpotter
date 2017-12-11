package com.semeshky.kvgspotter.viewholder;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

public class DataboundViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
    private final T mBinding;

    public DataboundViewHolder(T binding) {
        super(binding.getRoot());
        this.mBinding = binding;
    }

    public T getBinding() {
        return mBinding;
    }
}
