package devilseye.android.homebookkeeping.model;

public class Account {
    private long _id;
    private String _name;
    private String _description;
    private double _balance;

    public Account(){}

    public Account(int id, String name, String description, double balance){
        this._id=id;
        this._name=name;
        this._description=description;
        this._balance=balance;
    }

    public Account(String name, String description, double balance){
        this._name=name;
        this._description=description;
        this._balance=balance;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public double get_balance() {
        return _balance;
    }

    public void set_balance(double _balance) {
        this._balance = _balance;
    }

    public void increase_balance(double inBalance) {
        this._balance += inBalance;
    }

    public void decrease_balance(double deBalance) {
        this._balance -= deBalance;
    }
}
