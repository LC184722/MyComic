package com.qc.mycomic.other;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import the.one.base.ui.fragment.BaseDataFragment;
import the.one.base.widge.decoration.SpacesItemDecoration;

/**
 * @author The one
 * @date 2019/10/9 0009
 * @describe 分割线
 * @email 625805189@qq.com
 * @remark
 */
public class MySpacesItemDecoration extends SpacesItemDecoration {
    private static final String TAG = "SpacesItemDecoration";

    private int column;
    private int headerNum;
    private int left;
    private int right;
    private int top;
    private int bottom;

    public MySpacesItemDecoration(int column, int space) {
        this(column, 0, space);
    }

    public MySpacesItemDecoration(int column, int headerNum, int space) {
        this(column, headerNum, space, space, space, space);
    }

    public MySpacesItemDecoration(int column, int headerNum, int left, int right, int top, int bottom) {
        super(column, headerNum, left, right, top, bottom);
        this.column = column;
        this.headerNum = headerNum;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        // 当前View列位置,
        int columnIndex = 0;
        // 当前View为第几个
        int position;
        int type;
        if (parent.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager.LayoutParams params =
                    (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
            columnIndex = params.getSpanIndex();
            position = params.getViewAdapterPosition();
            type = BaseDataFragment.TYPE_STAGGERED;
        } else if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) view.getLayoutParams();
            columnIndex = params.getSpanIndex();
            position = params.getViewAdapterPosition();
            type = BaseDataFragment.TYPE_GRID;
        } else {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
            position = params.getViewAdapterPosition();
            type = BaseDataFragment.TYPE_LIST;
        }
        // 有的时候适配器会加上头部，如果有就不加间距，让头部自行处理 （如果有尾部，同理也可以加上）

        if (position >= headerNum) {
            // 全都加上bottom,left,right间距
            outRect.left = left;
            outRect.right = right;
            outRect.bottom = bottom;
            // 但是只给第一个加上top间距
            outRect.top = position == headerNum ? top : 0;
//            outRect.top = top / 2;
            if (column > 1) {
                // 只要不为一列，只考虑最左和最右项的差别
                int X = column - columnIndex;
                //Log.i(TAG, "getItemOffsets: position X = " + position + " " + X);
                // 如果为多列时，给第一行的加上top.
                // X != column 这个判断
                // 当遇到SectionAdapter时，有两列，如果第一行是标题占据一整行，
                // position=1时已经是第二行的内容层了，这个时候就不能加了，案例见aqtour首页布局
                if (type == BaseDataFragment.TYPE_STAGGERED && position < column && X != column) {
                    outRect.top = top;
                }
                if (X == column) {
                    // 最左边的只需要在右边设置一半的间距
                    outRect.right = right / 2;
                } else if (X == 1) {
                    // 最右边的只需要在左边设置一半的间距
                    outRect.left = left / 2;
                } else {
                    // 其余的一律在左右都设置一般的间距
                    outRect.left = left / 2;
                    outRect.right = right / 2;
                }
            }
            //Log.i(TAG, "getItemOffsets: top " + outRect.top);
            //Log.i(TAG, "getItemOffsets: bottom " + outRect.bottom);
            //Log.i(TAG, "getItemOffsets: left " + outRect.left);
            //Log.i(TAG, "getItemOffsets: right " + outRect.right);
        }
    }

}