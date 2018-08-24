package b.laixuantam.myaarlibrary.base;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public abstract class BaseListAdapter<T, V extends BaseViewHolder> extends ArrayAdapter<T>
{
    protected final LayoutInflater inflater;
    private final int resourceId;
    protected List<T> list;

    public BaseListAdapter(Context context, int resourceId, List<T> list)
    {
        super(context, android.R.layout.simple_list_item_1, list);

        this.inflater = LayoutInflater.from(context);
        this.resourceId = resourceId;
        this.list = list;
    }

    protected abstract void fillView(V viewHolder, T item, int position, View rowView);

    protected abstract V getViewHolder();

    @Override
    @SuppressWarnings("unchecked")
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View rowView = convertView;

        if (rowView == null)
        {
            rowView = generatedRowView(position, null, parent);
        }
        else
        {
            updatedRowView(position, convertView, parent);
        }

        V viewHolder = (V) rowView.getTag();

        T item = getItem(position);

        fillView(viewHolder, item, position, rowView);

        return rowView;
    }

    protected int getRowViewResource(int position)
    {
        return resourceId;
    }

    protected View generatedRowView(int position, View convertView, ViewGroup parent)
    {
        View rowView = inflater.inflate(getRowViewResource(position), parent, false);
        V viewHolder = getViewHolder();
        viewHolder.bind(rowView);
        rowView.setTag(viewHolder);

        return rowView;
    }

    protected void updatedRowView(int position, View convertView, ViewGroup parent)
    {
    }

    protected int getColor(@ColorRes int colorId)
    {
        return ContextCompat.getColor(getContext(), colorId);
    }

    public interface OnLoadMoreListener<T>
    {
        void onLoadMore(T data);

        void onLoadMore();
    }
}