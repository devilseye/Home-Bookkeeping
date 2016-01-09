package devilseye.android.homebookkeeping.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import devilseye.android.homebookkeeping.DBHelper;
import devilseye.android.homebookkeeping.OperationActivity;
import devilseye.android.homebookkeeping.R;
import devilseye.android.homebookkeeping.model.Operation;

public class OperationAdapter extends ArrayAdapter<Operation> {
    private final List<Operation> list;
    private final Activity context;
    private int selectedId;

    public OperationAdapter(Activity context, List<Operation> list) {
        super(context, R.layout.operation_item, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected TextView opDate;
        protected TextView opType;
        protected TextView category;
        protected TextView from;
        protected TextView to;
        protected TextView description;
        protected TextView value;
        protected ImageView deleteButton;
    }

    @Override
    public View getView(int position, final View convertView, final ViewGroup parent) {
        final View view;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.operation_item, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.opDate = (TextView) view.findViewById(R.id.opDate);
            viewHolder.opType=(TextView) view.findViewById(R.id.opType);
            viewHolder.category=(TextView) view.findViewById(R.id.category);
            viewHolder.category.setVisibility(View.VISIBLE);
            viewHolder.from=(TextView) view.findViewById(R.id.from);
            viewHolder.to=(TextView) view.findViewById(R.id.to);
            viewHolder.description=(TextView) view.findViewById(R.id.description);
            viewHolder.value=(TextView) view.findViewById(R.id.value);
            viewHolder.deleteButton=(ImageView) view.findViewById(R.id.deleteButton);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        final ViewHolder holder = (ViewHolder) view.getTag();
        holder.opDate.setText(Operation.getStringDateTime(list.get(position).get_datetime()));
        holder.opType.setText(list.get(position).get_type().get_name() + ":");
        holder.category.setText(list.get(position).get_category().get_name());
        if (list.get(position).get_from()!=null) {
            holder.from.setVisibility(View.VISIBLE);
            holder.from.setText(getContext().getString(R.string.from) + list.get(position).get_from().get_name());
        } else {
            holder.from.setVisibility(View.INVISIBLE);
        }
        if (list.get(position).get_to()!=null) {
            holder.to.setVisibility(View.VISIBLE);
            holder.to.setText(getContext().getString(R.string.to) + list.get(position).get_to().get_name());
        } else {
            holder.to.setVisibility(View.INVISIBLE);
        }
        holder.description.setText(list.get(position).get_description());
        holder.value.setText(getContext().getString(R.string.currency) + list.get(position).get_value());
        holder.deleteButton.setTag(list.get(position).get_id());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedId=Integer.parseInt(v.getTag().toString());
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder
                        .setTitle(context.getString(R.string.are_you_sure))
                        .setMessage(context.getString(R.string.delete_operation))
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DBHelper dbHelper = new DBHelper(context);
                                Operation operation = dbHelper.getOperation(selectedId);
                                dbHelper.deleteOperation(operation);
                                OperationActivity operationActivity=(OperationActivity) context;
                                operationActivity.updateList();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
        return view;
    }
}
