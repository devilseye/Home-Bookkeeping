package devilseye.android.homebookkeeping;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import devilseye.android.homebookkeeping.model.Account;
import devilseye.android.homebookkeeping.model.Category;
import devilseye.android.homebookkeeping.model.Operation;
import devilseye.android.homebookkeeping.model.OperationType;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "homebookkeeping";
    public static final int DATABASE_VERSION = 1;
    public static final String COLUMN_ID = "id";
    // table account
    public static final String TABLE_ACCOUNT="account";
    public static final String COLUMN_ACCOUNT_NAME="name";
    public static final String COLUMN_ACCOUNT_DESC="description";
    public static final String COLUMN_ACCOUNT_BALANCE="balance";
    // table category
    public static final String TABLE_CATEGORY="category";
    public static final String COLUMN_CATEGORY_INCOME="income";
    public static final String COLUMN_CATEGORY_NAME="name";
    // table operationType
    public static final String TABLE_OPTYPE="optype";
    public static final String COLUMN_OPTYPE_NAME="name";
    // table operation
    public static final String TABLE_OPERATION="operation";
    public static final String COLUMN_OPERATION_OPDATE="opDate";
    public static final String COLUMN_OPERATION_TYPE_ID="type_id";
    public static final String COLUMN_OPERATION_VALUE="value";
    public static final String COLUMN_OPERATION_FROM_ID="from_id";
    public static final String COLUMN_OPERATION_TO_ID="to_id";
    public static final String COLUMN_OPERATION_CATEGORY_ID="category_id";
    public static final String COLUMN_OPERATION_DESC="description";

    //create table category
    public static final String DATABASE_CREATE_CATEGORY = "create table "
            + TABLE_CATEGORY + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_CATEGORY_INCOME + " bool, "
            + COLUMN_CATEGORY_NAME + " string );";
    // create table account
    public static final String DATABASE_CREATE_ACCOUNT= "create table "
            + TABLE_ACCOUNT + "("+COLUMN_ID+" integer primary key autoincrement, "
            + COLUMN_ACCOUNT_NAME + " string, "
            + COLUMN_ACCOUNT_DESC + " string, "
            +COLUMN_ACCOUNT_BALANCE + " double );";
    // create table optype
    public static final String DATABASE_CREATE_OPTYPE= "create table "
            + TABLE_OPTYPE +"("+COLUMN_ID+" integer primary key autoincrement, "
            + COLUMN_OPTYPE_NAME + " string );";
    // create table operation
    public static final String DATABASE_CREATE_OPERATION="create table "
            + TABLE_OPERATION +"("+COLUMN_ID+" integer primary key autoincrement, "
            + COLUMN_OPERATION_OPDATE + " datetime, "
            + COLUMN_OPERATION_TYPE_ID + " integer, "
            + COLUMN_OPERATION_VALUE + " double, "
            + COLUMN_OPERATION_FROM_ID + " integer, "
            + COLUMN_OPERATION_TO_ID+" integer, "
            + COLUMN_OPERATION_CATEGORY_ID +" integer, "
            + COLUMN_OPERATION_DESC + " string, "
            + "FOREIGN KEY ("+COLUMN_OPERATION_TYPE_ID+") REFERENCES "+TABLE_OPTYPE+"("+COLUMN_ID+"), "
            + "FOREIGN KEY ("+COLUMN_OPERATION_FROM_ID+") REFERENCES "+TABLE_ACCOUNT+"("+COLUMN_ID+"), "
            + "FOREIGN KEY ("+COLUMN_OPERATION_TO_ID+") REFERENCES "+TABLE_ACCOUNT+"("+COLUMN_ID+"), "
            + "FOREIGN KEY ("+COLUMN_OPERATION_CATEGORY_ID+") REFERENCES "+TABLE_CATEGORY+"("+COLUMN_ID+")); ";

    //delete tables
    public static final String DATABASE_DROP_ACCOUNT = "DROP TABLE IF EXISTS " + TABLE_ACCOUNT;
    public static final String DATABASE_DROP_CATEGORY = "DROP TABLE IF EXISTS " + TABLE_CATEGORY;
    public static final String DATABASE_DROP_OPTYPE = "DROP TABLE IF EXISTS " + TABLE_OPTYPE;
    public static final String DATABASE_DROP_OPERATION = "DROP TABLE IF EXISTS " + TABLE_OPERATION;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_OPTYPE);
        db.execSQL(DATABASE_CREATE_ACCOUNT);
        db.execSQL(DATABASE_CREATE_CATEGORY);
        db.execSQL(DATABASE_CREATE_OPERATION);
        ContentValues values=new ContentValues();
        values.put(COLUMN_CATEGORY_INCOME, true);
        values.put(COLUMN_CATEGORY_NAME, "Income");
        db.insert(TABLE_CATEGORY, null, values);
        values=new ContentValues();
        values.put(COLUMN_CATEGORY_INCOME, false);
        values.put(COLUMN_CATEGORY_NAME, "Expenses");
        db.insert(TABLE_CATEGORY, null, values);
        values=new ContentValues();
        values.put(COLUMN_OPTYPE_NAME, "Income");
        db.insert(TABLE_OPTYPE, null, values);
        values=new ContentValues();
        values.put(COLUMN_OPTYPE_NAME, "Outcome");
        db.insert(TABLE_OPTYPE, null, values);
        values=new ContentValues();
        values.put(COLUMN_OPTYPE_NAME, "Transfer");
        db.insert(TABLE_OPTYPE, null, values);
    }

    @Override
     public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DATABASE_DROP_ACCOUNT);
        db.execSQL(DATABASE_DROP_OPTYPE);
        db.execSQL(DATABASE_DROP_CATEGORY);
        db.execSQL(DATABASE_DROP_OPERATION);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DATABASE_DROP_ACCOUNT);
        db.execSQL(DATABASE_DROP_OPTYPE);
        db.execSQL(DATABASE_DROP_CATEGORY);
        db.execSQL(DATABASE_DROP_OPERATION);
        onCreate(db);
    }

    public void addCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_INCOME, category.is_income());
        values.put(COLUMN_CATEGORY_NAME, category.get_name());

        db.insert(TABLE_CATEGORY, null, values);

    }

    public Category getCategory(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CATEGORY, new String[] { COLUMN_ID, COLUMN_CATEGORY_INCOME,
                        COLUMN_CATEGORY_NAME}, COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
            if (cursor.getCount()!=0) {
                Category category = new Category(Integer.parseInt(cursor.getString(0)), cursor.getInt(1) > 0,
                        cursor.getString(2));
                cursor.close();
                return category;
            }
        }
        return null;
    }

    public void deleteCategory(Category category) {
        if (category!=null) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_CATEGORY, COLUMN_ID + " = ?",
                    new String[]{String.valueOf(category.get_id())});
        }
    }

    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<Category>();
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.set_id(Integer.parseInt(cursor.getString(0)));

                category.set_income(cursor.getInt(1)>0);
                category.set_name(cursor.getString(2));

                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categoryList;
    }

    public List<Category> getAllCategoriesOfType(boolean income) {
        List<Category> categoryList = new ArrayList<Category>();
        int incomeInt=0;
        if (income)
            incomeInt=1;
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY + " WHERE "+COLUMN_CATEGORY_INCOME+" = "+incomeInt;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.set_id(Integer.parseInt(cursor.getString(0)));

                category.set_income(cursor.getInt(1)>0);
                category.set_name(cursor.getString(2));

                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categoryList;
    }

    public OperationType getOpType(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_OPTYPE, new String[]{COLUMN_ID, COLUMN_OPTYPE_NAME}, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
            if (cursor.getCount()!=0) {
                OperationType operationType = new OperationType(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
                cursor.close();
                return operationType;
            }
        }
        return null;
    }

    public List<OperationType> getAllOpTypes() {
        List<OperationType> operationTypeList = new ArrayList<OperationType>();
        String selectQuery = "SELECT  * FROM " + TABLE_OPTYPE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                OperationType operationType = new OperationType();
                operationType.set_id(Integer.parseInt(cursor.getString(0)));
                operationType.set_name(cursor.getString(1));

                operationTypeList.add(operationType);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return operationTypeList;
    }

    public long addAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ACCOUNT_NAME, account.get_name());
        values.put(COLUMN_ACCOUNT_DESC, account.get_description());
        values.put(COLUMN_ACCOUNT_BALANCE, account.get_balance());

        return db.insert(TABLE_ACCOUNT, null, values);

    }

    public void updateAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, account.get_id());
        values.put(COLUMN_ACCOUNT_NAME, account.get_name());
        values.put(COLUMN_ACCOUNT_DESC, account.get_description());
        values.put(COLUMN_ACCOUNT_BALANCE, account.get_balance());

        db.update(TABLE_ACCOUNT, values, COLUMN_ID + " = ?", new String[]{account.get_id() + ""});

    }

    public Account getAccount(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ACCOUNT, new String[]{COLUMN_ID, COLUMN_ACCOUNT_NAME,
                        COLUMN_ACCOUNT_DESC, COLUMN_ACCOUNT_BALANCE}, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
            if (cursor.getCount()!=0) {
                Account account = new Account(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                        cursor.getString(2), cursor.getDouble(3));
                cursor.close();
                return account;
            }
        }
        return null;
    }

    public void deleteAccount(Account account, long accountToId) {
        Account accountTo = getAccount(accountToId);
        List<Operation> operationsFromAcc = getAllOperationsFromAcc(account.get_id());
        for (Operation operation : operationsFromAcc) {
            operation.set_from(accountTo);
            accountTo.decrease_balance(operation.get_value());
            updateOperation(operation);
        }
        List<Operation> operationsToAcc = getAllOperationsToAcc(account.get_id());
        for (Operation operation : operationsToAcc) {
            operation.set_to(accountTo);
            accountTo.increase_balance(operation.get_value());
            updateOperation(operation);
        }
        updateAccount(accountTo);
        if (account!=null) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_ACCOUNT, COLUMN_ID + " = ?",
                    new String[]{String.valueOf(account.get_id())});
        }
    }

    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<Account>();
        String selectQuery = "SELECT  * FROM " + TABLE_ACCOUNT;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Account account = new Account();
                account.set_id(Integer.parseInt(cursor.getString(0)));

                account.set_name(cursor.getString(1));
                account.set_description(cursor.getString(2));
                account.set_balance(cursor.getDouble(3));

                accounts.add(account);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return accounts;
    }

    public List<Account> getAllAccountsExcept(long exceptId) {
        List<Account> accounts = new ArrayList<Account>();
        String selectQuery = "SELECT  * FROM " + TABLE_ACCOUNT + " WHERE "+ COLUMN_ID + " != "+exceptId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Account account = new Account();
                account.set_id(Integer.parseInt(cursor.getString(0)));

                account.set_name(cursor.getString(1));
                account.set_description(cursor.getString(2));
                account.set_balance(cursor.getDouble(3));

                accounts.add(account);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return accounts;
    }

    public long addOperation(Operation operation) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_OPERATION_DESC, operation.get_description());
        values.put(COLUMN_OPERATION_OPDATE, Operation.getStringDateTime(operation.get_datetime()));
        values.put(COLUMN_OPERATION_VALUE, operation.get_value());
        if (operation.get_category()!=null) {
            values.put(COLUMN_OPERATION_CATEGORY_ID, operation.get_category().get_id());
        } else {
            values.putNull(COLUMN_OPERATION_CATEGORY_ID);
        }
        if (operation.get_from()!=null) {
            values.put(COLUMN_OPERATION_FROM_ID, operation.get_from().get_id());
        } else {
            values.putNull(COLUMN_OPERATION_FROM_ID);
        }
        if (operation.get_to()!=null){
            values.put(COLUMN_OPERATION_TO_ID, operation.get_to().get_id());
        } else {
            values.putNull(COLUMN_OPERATION_TO_ID);
        }
        if (operation.get_type()!=null) {
            values.put(COLUMN_OPERATION_TYPE_ID, operation.get_type().get_id());
        } else {
            values.putNull(COLUMN_OPERATION_TYPE_ID);
        }

        long id = db.insert(TABLE_OPERATION, null, values);
        if (operation.get_from() != null) {
            operation.get_from().decrease_balance(operation.get_value());
            updateAccount(operation.get_from());
        }
        if (operation.get_to() != null) {
            operation.get_to().increase_balance(operation.get_value());
            updateAccount(operation.get_to());
        }
        return id;
    }

    public void updateOperation(Operation operation) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, operation.get_id());
        values.put(COLUMN_OPERATION_DESC, operation.get_description());
        values.put(COLUMN_OPERATION_OPDATE, Operation.getStringDateTime(operation.get_datetime()));
        values.put(COLUMN_OPERATION_VALUE, operation.get_value());
        if (operation.get_category()!=null) {
            values.put(COLUMN_OPERATION_CATEGORY_ID, operation.get_category().get_id());
        } else {
            values.putNull(COLUMN_OPERATION_CATEGORY_ID);
        }
        if (operation.get_from()!=null) {
            values.put(COLUMN_OPERATION_FROM_ID, operation.get_from().get_id());
        } else {
            values.putNull(COLUMN_OPERATION_FROM_ID);
        }
        if (operation.get_to()!=null){
            values.put(COLUMN_OPERATION_TO_ID, operation.get_to().get_id());
        } else {
            values.putNull(COLUMN_OPERATION_TO_ID);
        }
        if (operation.get_type()!=null) {
            values.put(COLUMN_OPERATION_TYPE_ID, operation.get_type().get_id());
        } else {
            values.putNull(COLUMN_OPERATION_TYPE_ID);
        }

        db.update(TABLE_OPERATION, values, COLUMN_ID + " = ?",
                new String[]{operation.get_id() + ""});
    }

    public Operation getOperation(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_OPERATION, new String[]{COLUMN_ID, COLUMN_OPERATION_OPDATE,
                        COLUMN_OPERATION_TYPE_ID, COLUMN_OPERATION_VALUE, COLUMN_OPERATION_FROM_ID,
                        COLUMN_OPERATION_TO_ID, COLUMN_OPERATION_CATEGORY_ID, COLUMN_OPERATION_DESC}, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
            if (cursor.getCount()!=0) {
                Operation operation = new Operation(Integer.parseInt(cursor.getString(0)), Operation.getDateTime(cursor.getString(1)),
                        getOpType(cursor.getInt(2)), cursor.getDouble(3),
                        getAccount(cursor.getInt(4)),getAccount(cursor.getInt(5)),
                        getCategory(cursor.getInt(6)), cursor.getString(7));
                cursor.close();
                return operation;
            }
        }
        return null;
    }

    public void deleteOperation(Operation operation) {
        if (operation.get_from() != null) {
            operation.get_from().increase_balance(operation.get_value());
            updateAccount(operation.get_from());
        }
        if (operation.get_to() != null) {
            operation.get_to().decrease_balance(operation.get_value());
            updateAccount(operation.get_to());
        }
        if (operation!=null) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_OPERATION, COLUMN_ID + " = ?",
                    new String[]{String.valueOf(operation.get_id())});
        }
    }

    public List<Operation> getAllOperations() {
        List<Operation>operations = new ArrayList<Operation>();
        String selectQuery = "SELECT  * FROM " + TABLE_OPERATION;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Operation operation = new Operation(Integer.parseInt(cursor.getString(0)), Operation.getDateTime(cursor.getString(1)),
                        getOpType(cursor.getInt(2)), cursor.getDouble(3),
                        getAccount(cursor.getInt(4)),getAccount(cursor.getInt(5)),
                        getCategory(cursor.getInt(6)),cursor.getString(7));

                operations.add(operation);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return operations;
    }

    public List<Operation> getOperationsByDesc(String searchText) {
        List<Operation>operations = new ArrayList<Operation>();
        String selectQuery = "SELECT  * FROM " + TABLE_OPERATION + " WHERE "+COLUMN_OPERATION_DESC+" LIKE \'%"+searchText+"%\'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Operation operation = new Operation(Integer.parseInt(cursor.getString(0)), Operation.getDateTime(cursor.getString(1)),
                        getOpType(cursor.getInt(2)), cursor.getDouble(3),
                        getAccount(cursor.getInt(4)),getAccount(cursor.getInt(5)),
                        getCategory(cursor.getInt(6)),cursor.getString(7));

                operations.add(operation);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return operations;
    }

    public List<Operation> getAllOperationsFromAcc(long accountId) {
        List<Operation>operations = new ArrayList<Operation>();
        String selectQuery = "SELECT  * FROM " + TABLE_OPERATION + " WHERE "+COLUMN_OPERATION_FROM_ID +" = "+accountId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Operation operation = new Operation(Integer.parseInt(cursor.getString(0)), Operation.getDateTime(cursor.getString(1)),
                        getOpType(cursor.getInt(2)), cursor.getDouble(3),
                        getAccount(cursor.getInt(4)),getAccount(cursor.getInt(5)),
                        getCategory(cursor.getInt(6)),cursor.getString(7));

                operations.add(operation);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return operations;
    }

    public List<Operation> getAllOperationsToAcc(long accountId) {
        List<Operation>operations = new ArrayList<Operation>();
        String selectQuery = "SELECT  * FROM " + TABLE_OPERATION + " WHERE "+COLUMN_OPERATION_TO_ID +" = "+accountId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Operation operation = new Operation(Integer.parseInt(cursor.getString(0)), Operation.getDateTime(cursor.getString(1)),
                        getOpType(cursor.getInt(2)), cursor.getDouble(3),
                        getAccount(cursor.getInt(4)),getAccount(cursor.getInt(5)),
                        getCategory(cursor.getInt(6)),cursor.getString(7));

                operations.add(operation);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return operations;
    }
}
