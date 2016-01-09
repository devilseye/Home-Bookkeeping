package devilseye.android.homebookkeeping;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

import devilseye.android.homebookkeeping.model.Account;
import devilseye.android.homebookkeeping.model.Category;
import devilseye.android.homebookkeeping.model.Operation;

@RunWith(AndroidJUnit4.class)
public class DeleteOperationTest extends ActivityInstrumentationTestCase2 {

    final double value=10.0;
    Operation operation;
    Account accountTo;
    Account accountFrom;
    DBHelper dbHelper;

    public DeleteOperationTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        dbHelper=new DBHelper(getActivity());
        List<Account> accounts=dbHelper.getAllAccounts();
        List<Category> categories=dbHelper.getAllCategories();
        accountTo=accounts.get(0);
        accountFrom=accounts.get(accounts.size()-1);
        operation=new Operation(new Date(),dbHelper.getOpType(3),value,accountFrom,accountTo,categories.get(0),"description");
    }

    @Test
    public void testDeleteOperation() {
        assertNotNull(accountTo);
        assertNotNull(accountFrom);
        assertNotNull(operation);
        long id=dbHelper.addOperation(operation);
        accountTo=dbHelper.getAccount(accountTo.get_id());
        accountFrom=dbHelper.getAccount(accountFrom.get_id());
        double startBalanceTo=accountTo.get_balance();
        double startBalanceFrom=accountFrom.get_balance();
        dbHelper.deleteOperation(dbHelper.getOperation(id));
        accountTo=dbHelper.getAccount(accountTo.get_id());
        accountFrom=dbHelper.getAccount(accountFrom.get_id());
        double endBalanceTo=accountTo.get_balance();
        double endBalanceFrom=accountFrom.get_balance();
        assertEquals("accountTo:", startBalanceTo-endBalanceTo, value, 0.01);
        assertEquals("accountFrom:", endBalanceFrom - startBalanceFrom, value, 0.01);
    }
}
