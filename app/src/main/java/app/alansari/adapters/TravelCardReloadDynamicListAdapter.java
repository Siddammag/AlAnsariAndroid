package app.alansari.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import app.alansari.R;
import app.alansari.TravelCardReloadCurrencyActivity;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.Validation;
import app.alansari.customviews.CurrencyEditText;
import app.alansari.customviews.progressbar.CircleProgressBar;
import app.alansari.models.getCharges.ResultItem;
import app.alansari.models.travalcardvalidateflag.TravelCardAdapterItem;
import app.alansari.models.travalcardvalidateflag.TravelCardFlag;


public class TravelCardReloadDynamicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<TravelCardAdapterItem> itemList;

    TextWatcher textWatcher;
    int currentDirection = -1;
    int showCurrencyPosition = -1;

    public TravelCardReloadDynamicListAdapter(Context context, ArrayList<TravelCardAdapterItem> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(app.alansari.R.layout.travel_card_currency_field_adapter, parent, false);

        TravelCardCurrencyItemViewHolder vh = new TravelCardCurrencyItemViewHolder(v);
        return vh;
    }

    public TravelCardAdapterItem getItemAt(int position) {
        return itemList.get(position);
    }

    public void delete(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final TravelCardCurrencyItemViewHolder curent = (TravelCardCurrencyItemViewHolder) holder;
        if (itemList.get(position).getTravelCardFlag() == null) {
            curent.tvAdd.setVisibility(View.VISIBLE);
            curent.regCurrencyLayout.setVisibility(View.GONE);
            curent.regLabel.setVisibility(View.GONE);
            curent.etTo.setText("");
            curent.etFrom.setText("");
            curent.btnSwap.setVisibility(View.GONE);

            // add currency/add another currency
            curent.tvAdd.setText(getItemCount() == 1 ? "(+)Add Currency" : "(+)Add Another Currency");
        }

        if (itemList.get(position).getResultItem() != null) {
            curent.tvRate.setVisibility(View.VISIBLE);
            curent.tvAdd.setVisibility(View.GONE);
            curent.regCurrencyLayout.setVisibility(View.VISIBLE);
            curent.regLabel.setVisibility(View.GONE);
            ((TravelCardReloadCurrencyActivity) context).isCalculatingCurrency = false;
            curent.progressBar.setVisibility(View.GONE);
            curent.btnSwap.setVisibility(View.VISIBLE);
            curent.name.setText(itemList.get(position).getTravelCardFlag().getISOCCYCODE());

            Picasso.get()
                    .load(Constants.COUNTRY_FLAG_IMAGE_BASE_URL + itemList.get(position).getTravelCardFlag().getFLAG())
                    .placeholder(app.alansari.R.drawable.flag_placeholder)
                    .error(app.alansari.R.drawable.flag_placeholder)
                    .into(curent.imageViewFlag);
            String amount = String.valueOf((itemList.get(position).getResultItem().getAEDAMOUNT()));
            String fcym = String.valueOf((itemList.get(position).getResultItem().getFCYAMOUNT()));
            curent.rate.setText(String.valueOf((itemList.get(position).getResultItem().getFCYAMOUNT())));
//            if (currentDirection == 1) {
//                curent.etTo.setText(amount);
//                curent.etFrom.setText(fcym);
//            } else if (currentDirection == 2) {
//                curent.etTo.setText(amount);
//                curent.etFrom.setText(fcym);
//            }
            curent.etTo.setText(amount);
            curent.etFrom.setText(fcym);
            itemList.get(position).setFromCurrency(CommonUtils.getTextFromEditText(curent.etFrom));
            itemList.get(position).setToCurrency(CommonUtils.getTextFromEditText(curent.etTo));


            try {
                Double rateDouble = Double.valueOf(String.valueOf(itemList.get(position).getResultItem().getRATE()));

                NumberFormat nf = NumberFormat.getInstance(Locale.US);
                nf.setMinimumFractionDigits(10);
                String rate = String.valueOf(nf.format(rateDouble));

                curent.tvRate.setText("Exchange Rate " + itemList.get(position).getTravelCardFlag().getISOCCYCODE() + " = AED " + rate);
            } catch (Exception ex) {
                curent.tvRate.setVisibility(View.INVISIBLE);
            }

            if (position != 0) {
                itemList.get(position - 1).setShowSingleLine(true);
            }
        } else {
            curent.tvRate.setVisibility(View.INVISIBLE);
        }

        if (itemList.get(position).getTravelCardFlag() != null && itemList.get(position).getTravelCardFlag().getCCYDESC() != null) {
            curent.tvAdd.setVisibility(View.GONE);
            curent.regCurrencyLayout.setVisibility(View.VISIBLE);
            curent.regLabel.setVisibility(View.GONE);
            curent.progressBar.setVisibility(View.GONE);
            curent.tvSelectCurrency.setText(itemList.get(position).getTravelCardFlag().getCCYDESC());
            CommonUtils.setCountryFlagImage(context, curent.ivFromFlag, itemList.get(position).getTravelCardFlag().getFLAG());
            curent.ivToFlag.setImageResource(R.drawable.svg_flag_aed);
            curent.tvFromCode.setText(itemList.get(position).getTravelCardFlag().getISOCCYCODE());
            curent.tvToCode.setText("AED");
            curent.ivFromFlag.setVisibility(View.VISIBLE);
            curent.ivToFlag.setVisibility(View.VISIBLE);
            curent.tvToCode.setVisibility(View.VISIBLE);
            curent.tvFromCode.setVisibility(View.VISIBLE);
            //curent.tvRate.setVisibility(View.GONE);


        }
        if (itemList.get(position).isShowSingleLine()) {
            curent.regCurrencyLayout.setVisibility(View.GONE);
            curent.regLabel.setVisibility(View.VISIBLE);
            curent.regLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCurrencyPosition = position;
                    // curent.tvAdd.setVisibility(View.VISIBLE);
                    notifyDataSetChanged();
                }
            });
        }

        if (position == showCurrencyPosition && itemList.get(position).getTravelCardFlag() != null) {
            curent.regCurrencyLayout.setVisibility(View.VISIBLE);
            curent.regLabel.setVisibility(View.GONE);
        } else {
            curent.regCurrencyLayout.setVisibility(View.GONE);
            if (itemList.get(position).getResultItem() != null) {
                curent.regLabel.setVisibility(View.VISIBLE);
                //curent.tvAdd.setVisibility(View.VISIBLE);
            }
        }

        if (itemList.get(position).getTravelCardFlag() == null && itemList.get(position).getResultItem() == null) {
            curent.tvAdd.setVisibility(View.VISIBLE);
        }

       /* if(itemList.size()>1){
            curent.tvAdd.setVisibility(View.GONE);
        }*/

        curent.tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position != 0) {
                    itemList.get(position - 1).setShowSingleLine(true);
                }
                ((TravelCardReloadCurrencyActivity) context).openTravelCardCurrencyList(position);
            }
        });

        curent.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((TravelCardReloadCurrencyActivity) context).removeItem(position);

                curent.tvAdd.setVisibility(View.VISIBLE);
                //notifyDataSetChanged();

            }
        });


        curent.etFrom.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (Validation.isValidEditTextValue(curent.etFrom)) {
                        CommonUtils.hideKeyboard(context);
                        curent.progressBar.setVisibility(View.VISIBLE);
                        currentDirection = 1;
                        itemList.get(position).setFromCurrency(CommonUtils.getTextFromEditText(curent.etFrom));
                        itemList.get(position).setDirection(currentDirection);
                        ((TravelCardReloadCurrencyActivity) context).calculateCurrency(1, curent.etFrom);
                    }
                }
                return false;
            }
        });

        curent.etTo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (Validation.isValidEditTextValue(curent.etTo)) {
                        CommonUtils.hideKeyboard(context);
                        curent.progressBar.setVisibility(View.VISIBLE);
                        currentDirection = 2;
                        itemList.get(position).setToCurrency(CommonUtils.getTextFromEditText(curent.etTo));
                        itemList.get(position).setDirection(currentDirection);
                        ((TravelCardReloadCurrencyActivity) context).calculateCurrency(2, curent.etTo);
                    }
                }
                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void addArrayList(int clickedPosition, TravelCardFlag item) {
        this.itemList.get(clickedPosition).setTravelCardFlag(item);
        notifyDataSetChanged();
    }

    public void setResult(int clickedPosition, ResultItem resultItem) {
        this.itemList.get(clickedPosition).setResultItem(resultItem);
        notifyDataSetChanged();
    }

    public void setShowCurrencyPosition(int position) {
        showCurrencyPosition = position;
    }

    private class TravelCardCurrencyItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvFromCode, tvToCode, tvRate, tvSelectCurrency, tvCurrency, tvBuy, tvSell;
        ImageView imageViewFlag;
        AppCompatImageView ivFromFlag, ivToFlag, btnSwap;
        TextView name, rate, tvAdd, tvDelete;
        RelativeLayout regCurrencyLayout, regLabel;
        private CurrencyEditText etFrom, etTo;
        private CircleProgressBar progressBar;

        public TravelCardCurrencyItemViewHolder(View inflate) {
            super(inflate);
            imageViewFlag = (ImageView) inflate.findViewById(R.id.flag_image);
            name = (TextView) inflate.findViewById(R.id.title);
            rate = (TextView) inflate.findViewById(R.id.amount);
            tvAdd = (TextView) inflate.findViewById(R.id.tv_add);
            tvDelete = (TextView) inflate.findViewById(R.id.tv_delete);
            regCurrencyLayout = (RelativeLayout) inflate.findViewById(R.id.reg_currency_layout);
            regLabel = (RelativeLayout) inflate.findViewById(R.id.reg_label);
            tvFromCode = (TextView) inflate.findViewById(app.alansari.R.id.from_code);
            tvToCode = (TextView) inflate.findViewById(app.alansari.R.id.to_code);
            ivFromFlag = (AppCompatImageView) inflate.findViewById(app.alansari.R.id.from_flag);
            ivToFlag = (AppCompatImageView) inflate.findViewById(app.alansari.R.id.to_flag);
            tvRate = (TextView) inflate.findViewById(app.alansari.R.id.rate);
            tvSelectCurrency = (TextView) inflate.findViewById(app.alansari.R.id.select_currency);
            tvCurrency = (TextView) inflate.findViewById(app.alansari.R.id.currency);
            tvBuy = (TextView) inflate.findViewById(app.alansari.R.id.buy);
            tvSell = (TextView) inflate.findViewById(app.alansari.R.id.sell);

            etFrom = (CurrencyEditText) inflate.findViewById(app.alansari.R.id.from);
            etTo = (CurrencyEditText) inflate.findViewById(app.alansari.R.id.to);

            progressBar = (CircleProgressBar) inflate.findViewById(app.alansari.R.id.progress_bar);
            btnSwap = (AppCompatImageView) inflate.findViewById(app.alansari.R.id.swap_btn);

            /*textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s == etFrom.getEditableText()) {
                        if (s.length() > 5) {
                            etFrom.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(app.alansari.R.dimen.dimens_22sp));
                        } else {
                            etFrom.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(app.alansari.R.dimen.dimens_28sp));
                        }
                        if (!((TravelCardReloadCurrencyActivity) context).isCalculatingCurrency && Validation.isValidEditTextValue(etFrom)) {
                            etTo.setText("");
                            progressBar.setVisibility(View.GONE);
                            btnSwap.setVisibility(View.GONE);
                        }
                        if (Validation.isValidString(s.toString()))
                            CommonUtils.addCommaAfterThousand(etFrom, textWatcher, s.toString());
                    } else {
                        if (s.length() > 5) {
                            etTo.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(app.alansari.R.dimen.dimens_22sp));
                        } else {
                            etTo.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(app.alansari.R.dimen.dimens_28sp));
                        }
                        if (!((TravelCardReloadCurrencyActivity) context).isCalculatingCurrency && Validation.isValidEditTextValue(etTo)) {
                            etFrom.setText("");
                            progressBar.setVisibility(View.GONE);
                            btnSwap.setVisibility(View.GONE);
                        }
                        if (Validation.isValidString(s.toString()))
                            CommonUtils.addCommaAfterThousand(etTo, textWatcher, s.toString());
                    }

                }
            };
            etTo.addTextChangedListener(textWatcher);
            etFrom.addTextChangedListener(textWatcher);*/
        }
    }
}

