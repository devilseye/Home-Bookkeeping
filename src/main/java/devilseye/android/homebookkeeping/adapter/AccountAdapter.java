package devilseye.android.homebookkeeping.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import devilseye.android.homebookkeeping.DBHelper;
import devilseye.android.homebookkeeping.MainActivity;
import devilseye.android.homebookkeeping.R;
import devilseye.android.homebookkeeping.model.Account;

public class AccountAdapter extends ArrayAdapter<Account> {
    private final List<Account> list;
    private final Activity context;

    public AccountAdapter(Activity context, List<Account> list) {
        super(context, R.layout.account_item, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected TextView account;
        protected TextView description;
        protected TextView balance;
        protected ImageView deleteButton;
        protected int selectedId;
    }

    @Override
    public View getView(int position, final View convertView, final ViewGroup parent) {
        final View view;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.account_item, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.account = (TextView) view.findViewById(R.id.account);
            viewHolder.description=(TextView) view.findViewById(R.id.description);
            viewHolder.balance=(TextView) view.findViewById(R.id.balance);
            viewHolder.deleteButton=(ImageView) view.findViewById(R.id.deleteButton);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        final ViewHolder holder = (ViewHolder) view.getTag();
        holder.account.setText(list.get(position).get_name());
        holder.description.setText(list.get(position).get_description());
        holder.balance.setText(context.getString(R.string.currency) + list.get(position).get_balance());
        holder.deleteButton.setTag(list.get(position).get_id());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DBHelper dbHelper = new DBHelper(context);
                ImageView button = (ImageView) v;
                final int accountId = Integer.parseInt(button.getTag().toString());
                List<Account> otherAccs = dbHelper.getAllAccountsExcept(accountId);
                if (otherAccs.size() > 0) {
                    String[] accounts = new String[otherAccs.size()];
                    for (int i = 0; i < otherAccs.size(); i++) {
                        accounts[i] = otherAccs.get(i).get_name() + ":" + otherAccs.get(i).get_id();
                    }
                    final AlertDialog.Builder viewDialog = new AlertDialog.Builder(context);
                    viewDialog.setTitle(context.getString(R.string.are_you_sure));
                    final View dialogView = LayoutInflater.from(context).inflate(R.layout.delete_acc_dialog, null);
                    viewDialog.setView(dialogView);
                    final Dialog dialog = viewDialog.create();
                    final Spinner spinner = (Spinner) dialogView.findViewById(R.id.other_accs);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, accounts);
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {
                            String selectedItem = spinner.getSelectedItem().toString();
                            holder.selectedId = Integer.parseInt(selectedItem.substring(selectedItem.lastIndexOf(":") + 1, selectedItem.length()));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    Button deleteButton = (Button) dialogView.findViewById(R.id.delete);
                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dbHelper.deleteAccount(dbHelper.getAccount(accountId),holder.selectedId);
                            MainActivity activity = (MainActivity) getContext();
                            activity.updateList();
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else {
                    Toast.makeText(context,context.getString(R.string.nothing_to_delete),Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}
