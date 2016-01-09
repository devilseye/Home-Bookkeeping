package devilseye.android.homebookkeeping;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

import devilseye.android.homebookkeeping.model.Account;
import devilseye.android.homebookkeeping.model.Category;
import devilseye.android.homebookkeeping.model.Operation;

@RunWith(AndroidJUnit4.class)
public class DeleteAccountTest extends ActivityInstrumentationTestCase2 {

    final double valueTo=10.0;
    final double valueFrom=20.0;
    Operation operation1;
    Operation operation2;
    Account account;
    Account safeAccount;
    DBHelper dbHelper;
    List<Category> categories;

    public DeleteAccountTest() {
        super(MainActivity.class);
    }

    @Test
    public void testDeleteAccount() {
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        dbHelper=new DBHelper(getActivity());
        account=new Account("TestAccount","[test]",200.0);
        long id=dbHelper.addAccount(account);
        account=dbHelper.getAccount(id);
        safeAccount=dbHelper.getAllAccounts().get(0);
        categories=dbHelper.getAllCategories();
        operation1=new Operation(new Date(),dbHelper.getOpType(1),valueTo,null,account,categories.get(0),"[test]");
        operation2=new Operation(new Date(),dbHelper.getOpType(2),valueFrom,account,null,categories.get(0),"[test]");
        long opId1=dbHelper.addOperation(operation1);
        long opId2=dbHelper.addOperation(operation2);
        operation1=dbHelper.getOperation(opId1);
        operation2=dbHelper.getOperation(opId2);
        double startBalance=safeAccount.get_balance();
        int startCount=dbHelper.getAllOperationsFromAcc(safeAccount.get_id()).size()+dbHelper.getAllOperationsToAcc(safeAccount.get_id()).size();
        dbHelper.deleteAccount(account,safeAccount.get_id());
        safeAccount=dbHelper.getAccount(safeAccount.get_id());
        int endCount=dbHelper.getAllOperationsFromAcc(safeAccount.get_id()).size()+dbHelper.getAllOperationsToAcc(safeAccount.get_id()).size();
        double endBalance=safeAccount.get_balance();

        operation1=dbHelper.getOperation(operation1.get_id());
        operation2=dbHelper.getOperation(operation2.get_id());

        assertEquals(endCount-startCount,2);
        assertEquals(operation1.get_to().get_id(),safeAccount.get_id());
        assertEquals(operation2.get_from().get_id(),safeAccount.get_id());
        assertEquals(endBalance - startBalance, valueTo - valueFrom);

        // cleaning
        operation1=dbHelper.getOperation(operation1.get_id());
        dbHelper.deleteOperation(operation1);
        operation2=dbHelper.getOperation(operation2.get_id());
        dbHelper.deleteOperation(operation2);
        dbHelper.close();
    }
}
