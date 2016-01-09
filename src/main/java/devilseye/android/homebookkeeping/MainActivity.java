package devilseye.android.homebookkeeping;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import devilseye.android.homebookkeeping.adapter.AccountAdapter;
import devilseye.android.homebookkeeping.model.Account;

public class MainActivity extends Activity {

    DBHelper dbHelper;
    public void updateList(){
        List<Account> accounts=dbHelper.getAllAccounts();
        final AccountAdapter accountAdapter=new AccountAdapter(this,accounts);
        final ListView listView=(ListView) findViewById(R.id.accounts);
        listView.setAdapter(accountAdapter);
        final Context context=getBaseContext();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Account account = (Account) listView.getAdapter().getItem(position);
                Intent operations=new Intent(context,OperationActivity.class);
                operations.putExtra("acc_id",account.get_id());
                startActivity(operations);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper=new DBHelper(this);
        updateList();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add_acc:
                final AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setTitle(getString(R.string.add_acc));
                final View dialogView = LayoutInflater.from(this).inflate(R.layout.add_acc_dialog, null);
                adb.setView(dialogView);
                adb.setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        EditText name = (EditText) dialogView.findViewById(R.id.name);
                        EditText description = (EditText) dialogView.findViewById(R.id.description);
                        EditText balance = (EditText) dialogView.findViewById(R.id.balance);
                        Account account = new Account(name.getText().toString(), description.getText().toString(),
                                Double.parseDouble(balance.getText().toString()));
                        dbHelper.addAccount(account);
                        updateList();
                    }
                });

                adb.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        updateList();
                    }
                });
                adb.create().show();
                break;
            case R.id.action_categories:
                Intent categories=new Intent(this,CategoryActivity.class);
                startActivity(categories);
                break;
            case R.id.action_ops_search:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.action_ops_search));

                final EditText input = new EditText(this);
                input.setId(R.id.value);
                builder.setView(input);

                builder.setPositiveButton(getString(R.string.search), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent operations=new Intent(getBaseContext(),OperationActivity.class);
                        operations.putExtra("searchText",input.getText().toString());
                        startActivity(operations);
                    }
                });
                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
        }

        return super.onOptionsItemSelected(item);
    }
}
