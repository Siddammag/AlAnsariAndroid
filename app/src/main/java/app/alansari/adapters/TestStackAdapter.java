package app.alansari.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.StackAdapter;

/**
 * Created by Parveen Dala on 20 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class TestStackAdapter extends StackAdapter<Integer> {

    public TestStackAdapter(Context context) {
        super(context);
    }

    @Override
    public void bindView(Integer data, int position, CardStackView.ViewHolder holder) {
        if (holder instanceof ColorItemLargeHeaderViewHolder) {
            ColorItemLargeHeaderViewHolder h = (ColorItemLargeHeaderViewHolder) holder;
            h.onBind(data, position);
        }
        if (holder instanceof ColorItemWithNoHeaderViewHolder) {
            ColorItemWithNoHeaderViewHolder h = (ColorItemWithNoHeaderViewHolder) holder;
            h.onBind(data, position);
        }
        if (holder instanceof ColorItemViewHolder) {
            ColorItemViewHolder h = (ColorItemViewHolder) holder;
            h.onBind(data, position);
        }
    }

    @Override
    protected CardStackView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case app.alansari.R.layout.list_card_item_larger_header:
                view = getLayoutInflater().inflate(app.alansari.R.layout.list_card_item_larger_header, parent, false);
                return new ColorItemLargeHeaderViewHolder(view);
            case app.alansari.R.layout.list_card_item_with_no_header:
                view = getLayoutInflater().inflate(app.alansari.R.layout.list_card_item_with_no_header, parent, false);
                return new ColorItemWithNoHeaderViewHolder(view);
            default:
                view = getLayoutInflater().inflate(app.alansari.R.layout.list_card_item, parent, false);
                return new ColorItemViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 6) {//TODO TEST LARGER ITEM
            return app.alansari.R.layout.list_card_item_larger_header;
        } else if (position == 10) {
            return app.alansari.R.layout.list_card_item_with_no_header;
        } else {
            return app.alansari.R.layout.list_card_item;
        }
    }

    static class ColorItemViewHolder extends CardStackView.ViewHolder {
        View mLayout;
        View mContainerContent;
        TextView mTextTitle;

        public ColorItemViewHolder(View view) {
            super(view);
            mLayout = view.findViewById(app.alansari.R.id.frame_list_card_item);
            mContainerContent = view.findViewById(app.alansari.R.id.container_list_content);
            mTextTitle = (TextView) view.findViewById(app.alansari.R.id.text_list_card_title);
        }

        @Override
        public void onItemExpand(boolean b) {
            mContainerContent.setVisibility(b ? View.VISIBLE : View.GONE);
        }

        public void onBind(Integer data, int position) {
            mLayout.getBackground().setColorFilter(ContextCompat.getColor(getContext(), data), PorterDuff.Mode.SRC_IN);
            mTextTitle.setText(String.valueOf(position));
        }

    }

    static class ColorItemWithNoHeaderViewHolder extends CardStackView.ViewHolder {
        View mLayout;
        TextView mTextTitle;

        public ColorItemWithNoHeaderViewHolder(View view) {
            super(view);
            mLayout = view.findViewById(app.alansari.R.id.frame_list_card_item);
            mTextTitle = (TextView) view.findViewById(app.alansari.R.id.text_list_card_title);
        }

        @Override
        public void onItemExpand(boolean b) {
        }

        public void onBind(Integer data, int position) {
            mLayout.getBackground().setColorFilter(ContextCompat.getColor(getContext(), data), PorterDuff.Mode.SRC_IN);
            mTextTitle.setText(String.valueOf(position));
        }

    }

    static class ColorItemLargeHeaderViewHolder extends CardStackView.ViewHolder {
        View mLayout;
        View mContainerContent;
        TextView mTextTitle;

        public ColorItemLargeHeaderViewHolder(View view) {
            super(view);
            mLayout = view.findViewById(app.alansari.R.id.frame_list_card_item);
            mContainerContent = view.findViewById(app.alansari.R.id.container_list_content);
            mTextTitle = (TextView) view.findViewById(app.alansari.R.id.text_list_card_title);
        }

        @Override
        public void onItemExpand(boolean b) {
            mContainerContent.setVisibility(b ? View.VISIBLE : View.GONE);
        }

        @Override
        protected void onAnimationStateChange(int state, boolean willBeSelect) {
            super.onAnimationStateChange(state, willBeSelect);
            if (state == CardStackView.ANIMATION_STATE_START && willBeSelect) {
                onItemExpand(true);
            }
            if (state == CardStackView.ANIMATION_STATE_END && !willBeSelect) {
                onItemExpand(false);
            }
        }

        public void onBind(Integer data, int position) {
            mLayout.getBackground().setColorFilter(ContextCompat.getColor(getContext(), data), PorterDuff.Mode.SRC_IN);
            mTextTitle.setText(String.valueOf(position));

            itemView.findViewById(app.alansari.R.id.text_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((CardStackView) itemView.getParent()).performItemClick(ColorItemLargeHeaderViewHolder.this);
                }
            });
        }

    }

}
