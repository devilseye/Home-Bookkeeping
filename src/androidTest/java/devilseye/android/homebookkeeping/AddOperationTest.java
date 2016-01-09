package devilseye.android.homebookkeeping;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import devilseye.android.homebookkeeping.model.*;

@RunWith(AndroidJUnit4.class)
public class AddOperationTest extends ActivityInstrumentationTestCase2 {

    final double value=10.0;
    Operation operationTo;
    Operation operationFrom;
    Operation operationTransfer;
    Account accountTo;
    Account accountFrom;
    DBHelper dbHelper;

    public AddOperationTest() {
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
        operationTo=new Operation(new Date(),dbHelper.getOpType(1),value,null,accountTo,categories.get(0),"description");
        operationFrom=new Operation(new Date(),dbHelper.getOpType(2),value,accountFrom,null,categories.get(0),"description");
        operationTransfer=new Operation(new Date(),dbHelper.getOpType(3),value,accountFrom,accountTo,categories.get(0),"description");
    }

    @Test
    public void testAddOperation() {
        // test operationTo
        assertNotNull(accountTo);
        double startBalance=accountTo.get_balance();
        assertNotNull(operationTo);
        long idTo=dbHelper.addOperation(operationTo);
        accountTo=dbHelper.getAccount(accountTo.get_id());
        double endBalance=accountTo.get_balance();
        assertEquals("operationTo:", endBalance - startBalance, value, 0.01);

        // test operationFrom
        assertNotNull(accountFrom);
        startBalance=accountFrom.get_balance();
        assertNotNull(operationFrom);
        long idFrom=dbHelper.addOperation(operationFrom);
        accountFrom=dbHelper.getAccount(accountFrom.get_id());
        endBalance=accountFrom.get_balance();
        assertEquals("operationFrom:", startBalance - endBalance, value, 0.01);

        // test operationTransfer
        accountTo=dbHelper.getAccount(accountTo.get_id());
        accountFrom=dbHelper.getAccount(accountFrom.get_id());
        assertNotNull(accountTo);
        assertNotNull(accountFrom);
        double startBalanceTo=accountTo.get_balance();
        startBalance=accountFrom.get_balance();
        assertNotNull(operationTransfer);
        long idTransfer=dbHelper.addOperation(operationTransfer);
        accountTo=dbHelper.getAccount(accountTo.get_id());
        accountFrom=dbHelper.getAccount(accountFrom.get_id());
        double endBalanceTo=accountTo.get_balance();
        endBalance=accountFrom.get_balance();
        assertEquals(endBalanceTo - startBalanceTo, value, 0.01);
        assertEquals(startBalance - endBalance, value, 0.01);

        dbHelper.deleteOperation(dbHelper.getOperation(idTo));
        dbHelper.deleteOperation(dbHelper.getOperation(idFrom));
        dbHelper.deleteOperation(dbHelper.getOperation(idTransfer));
        dbHelper.close();
    }
}
