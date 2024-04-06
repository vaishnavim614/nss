package com.test.nss.ui.data;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;

import com.test.nss.R;

public class CustomBehaviour extends CoordinatorLayout.Behavior<NestedScrollView> {
    public CustomBehaviour(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull NestedScrollView child, int layoutDirection) {
        parent.onLayoutChild(child, layoutDirection);
        setTopMargin(child.findViewById(R.id.card_view), 0);
        int maxH = child.getHeight() - child.findViewById(R.id.card_title).getHeight()
                - child.findViewById(R.id.card_sub_title).getHeight();

        MaxHeightRecyclerView rv = child.findViewById(R.id.card_rec_view);
        rv.setMaxHeight(maxH);

        View cardCon = child.findViewById(R.id.card_container);
        int toolbH = 10;
        setPaddTop(cardCon, maxH - toolbH);
        ViewCompat.offsetTopAndBottom(child, toolbH);
        setPaddBot(rv, toolbH);

        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout parent, @NonNull NestedScrollView child, @NonNull MotionEvent ev) {
        return ev.getActionMasked() == MotionEvent.ACTION_DOWN &&
                !isTouchInChild(parent, child.findViewById(R.id.card_view), ev);
    }

    private boolean isTouchInChild(ViewGroup parent, View child, MotionEvent ev) {
        return MyViewGroupUtils.isPointInChildBounds(parent, child, (int) ev.getX(), (int) ev.getY());
    }

    private void setPaddBot(View view, int bottom) {
        if (view.getPaddingTop() != bottom)
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), bottom);
    }

    private void setPaddTop(View view, int top) {
        if (view.getPaddingTop() != top)
            view.setPadding(view.getPaddingLeft(), top, view.getPaddingRight(), view.getPaddingBottom());
    }

    private void setTopMargin(View a, int b) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) a.getLayoutParams();
        if (layoutParams.topMargin != b) {
            layoutParams.topMargin = b;
            a.setLayoutParams(layoutParams);
        }

    }
}
