package devilseye.android.homebookkeeping;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.Date;
import java.util.List;
import devilseye.android.homebookkeeping.adapter.OperationAdapter;
import devilseye.android.homebookkeeping.model.Account;
import devilseye.android.homebookkeeping.model.Category;
import devilseye.android.homebookkeeping.model.Operation;

public class OperationActivity extends Activity {

    DBHelper dbHelper;
    long account_id;
    List<Category> categoryList;
    int categoryId;
    int selectedAccId;

    public void updateList(){
        account_id=getIntent().getLongExtra("acc_id",0);
        if (account_id!=0) {
            List<Operation> operations = dbHelper.getAllOperationsFromAcc(account_id);
            operations.addAll(dbHelper.getAllOperationsToAcc(account_id));
            final OperationAdapter operationAdapter = new OperationAdapter(this, operations);
            final ListView listView = (ListView) findViewById(R.id.operations);
            listView.setAdapter(operationAdapter);
        } else {
            String searchText=getIntent().getStringExtra("searchText");
            if (searchText==null){
                searchText="";
            }
            List<Operation> operations = dbHelper.getOperationsByDesc(searchText);
            operations.addAll(dbHelper.getAllOperationsToAcc(account_id));
            final OperationAdapter operationAdapter = new OperationAdapter(this, operations);
            final ListView listView = (ListView) findViewById(R.id.operations);
            listView.setAdapter(operationAdapter);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);
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
        if (account_id!=0) {
            getMenuInflater().inflate(R.menu.menu_operation, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_add_operation:
                final AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setTitle(getString(R.string.action_add_operation));
                final View dialogView = LayoutInflater.from(this).inflate(R.layout.add_op_dialog, null);
                CheckBox income = (CheckBox) dialogView.findViewById(R.id.toLabel);
                income.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        final Spinner categories = (Spinner) dialogView.findViewById(R.id.categories);
                        categoryList = dbHelper.getAllCategoriesOfType(isChecked);
                        String[] categoriesStr = new String[categoryList.size()];
                        for (int i = 0; i < categoryList.size(); i++) {
                            categoriesStr[i] = categoryList.get(i).get_name() + ":" + categoryList.get(i).get_id();
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, categoriesStr);
                        categories.setAdapter(adapter);
                    }
                });
                final Spinner categories = (Spinner) dialogView.findViewById(R.id.categories);
                categoryList = dbHelper.getAllCategoriesOfType(income.isChecked());
                String[] categoriesStr = new String[categoryList.size()];
                for (int i = 0; i < categoryList.size(); i++) {
                    categoriesStr[i] = categoryList.get(i).get_name() + ":" + categoryList.get(i).get_id();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, categoriesStr);
                categories.setAdapter(adapter);
                categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {
                        String selectedItem = categories.getSelectedItem().toString();
                        categoryId = Integer.parseInt(selectedItem.substring(selectedItem.lastIndexOf(":") + 1, selectedItem.length()));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                adb.setView(dialogView);
                adb.setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        EditText value = (EditText) dialogView.findViewById(R.id.op_value);
                        EditText description = (EditText) dialogView.findViewById(R.id.op_desc);
                        CheckBox income = (CheckBox) dialogView.findViewById(R.id.toLabel);
                        Operation operation = new Operation(new Date(), ((income.isChecked()) ? dbHelper.getOpType(1) : dbHelper.getOpType(2)), Double.parseDouble(value.getText().toString()),
                                ((!income.isChecked()) ? dbHelper.getAccount(account_id) : null), ((income.isChecked()) ? dbHelper.getAccount(account_id) : null), dbHelper.getCategory(categoryId), description.getText().toString());
                        dbHelper.addOperation(operation);
                        updateList();
                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        updateList();
                    }
                });

                adb.create().show();
                break;
            case R.id.action_add_transfer:
                final AlertDialog.Builder adbTr = new AlertDialog.Builder(this);
                adbTr.setTitle(getString(R.string.action_add_operation));
                final View dialogViewTr = LayoutInflater.from(this).inflate(R.layout.add_op_dialog_tr, null);
                List<Account> otherAccounts = dbHelper.getAllAccountsExcept(account_id);
                if (otherAccounts.size() > 0) {
                    String[] accounts = new String[otherAccounts.size()];
                    for (int i = 0; i < otherAccounts.size(); i++) {
                        accounts[i] = otherAccounts.get(i).get_name() + ":" + otherAccounts.get(i).get_id();
                    }
                    final Spinner spinner = (Spinner) dialogViewTr.findViewById(R.id.to_accs);
                    ArrayAdapter<String> adapterTr = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, accounts);
                    spinner.setAdapter(adapterTr);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {
                            String selectedItem = spinner.getSelectedItem().toString();
                            selectedAccId = Integer.parseInt(selectedItem.substring(selectedItem.lastIndexOf(":") + 1, selectedItem.length()));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    final Spinner categoriesTr = (Spinner) dialogViewTr.findViewById(R.id.categories_tr);
                    categoryList = dbHelper.getAllCategories();
                    String[] categoriesStrTr = new String[categoryList.size()];
                    for (int i = 0; i < categoryList.size(); i++) {
                        categoriesStrTr[i] = categoryList.get(i).get_name() + ":" + categoryList.get(i).get_id();
                    }
                    ArrayAdapter<String> catAdapterTr = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, categoriesStrTr);
                    categoriesTr.setAdapter(catAdapterTr);
                    categoriesTr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {
                            String selectedItem = categoriesTr.getSelectedItem().toString();
                            categoryId = Integer.parseInt(selectedItem.substring(selectedItem.lastIndexOf(":") + 1, selectedItem.length()));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                    adbTr.setView(dialogViewTr);
                    adbTr.setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            EditText value = (EditText) dialogViewTr.findViewById(R.id.op_value);
                            EditText description = (EditText) dialogViewTr.findViewById(R.id.op_desc);
                            Operation operation = new Operation(new Date(), dbHelper.getOpType(3), Double.parseDouble(value.getText().toString()),
                                    dbHelper.getAccount(account_id), dbHelper.getAccount(selectedAccId), dbHelper.getCategory(categoryId), description.getText().toString());
                            dbHelper.addOperation(operation);
                            updateList();
                        }
                    }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            updateList();
                        }
                    });

                    adbTr.create().show();
                    break;
                }
        }

        return super.onOptionsItemSelected(item);
    }
}
