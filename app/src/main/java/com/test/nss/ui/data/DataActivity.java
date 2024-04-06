package com.test.nss.ui.data;

import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.nss.R;

import java.util.ArrayList;
import java.util.List;

public class DataActivity extends AppCompatActivity {

    List<MyListAdapterModel> modelList = new ArrayList<>();
    private boolean isShowingCardShadow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        datModel();

        MaxHeightRecyclerView rv = findViewById(R.id.card_rec_view);
        LinearLayoutManager lm = new LinearLayoutManager(DataActivity.this);
        rv.setLayoutManager(new LinearLayoutManager(DataActivity.this));
        rv.setAdapter(new AdapterModel(modelList, DataActivity.this));
        rv.addItemDecoration(new DividerItemDecoration(DataActivity.this, lm.getOrientation()));

        View cardShadow = findViewById(R.id.toolbar_shadow_view);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                boolean isRecToTop = lm.findFirstVisibleItemPosition() == 0 && lm.findViewByPosition(0).getTop() == 0;
                if (!isRecToTop && !isShowingCardShadow) {
                    isShowingCardShadow = true;
                    showHideView(cardShadow, true);
                } else {
                    isShowingCardShadow = false;
                    showHideView(cardShadow, false);
                }
            }
        });

        AdvancedNestedScrollView adv = findViewById(R.id.nested_scroll_view);
        adv.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        adv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == 0 && oldScrollY > 0) {
                    rv.scrollToPosition(0);
                    cardShadow.setAlpha(0f);
                    isShowingCardShadow = false;
                }
            }
        });
    }

    private void showHideView(View cardShadow, boolean isShow) {
        cardShadow.animate().alpha(isShow ? 1f : 0f).setDuration(100).setInterpolator(new DecelerateInterpolator());
    }

    private void datModel() {
        modelList.add(new MyListAdapterModel(getString(R.string.text1), "Unity alone can make India strong and great National Integration Pledge"));
        modelList.add(new MyListAdapterModel(getString(R.string.text2), "MOTTO OF NSS NOT ME BUT YOU STORY OF N.S.S. EMBLEM"));
        modelList.add(new MyListAdapterModel(getString(R.string.text3), "NSS\nAIMS AND OBJECTIVES"));
        modelList.add(new MyListAdapterModel(getString(R.string.text4), "Introduction: National Service Scheme."));
        modelList.add(new MyListAdapterModel(getString(R.string.text5), "Suggestive list of the activities during Regular as well as Special Camping Programmes."));
        modelList.add(new MyListAdapterModel(getString(R.string.text6), "CODE OF CONDUCT FOR NSS VOLUNTEERS"));
        modelList.add(new MyListAdapterModel(getString(R.string.text7), "Fund Collection"));
    }
}