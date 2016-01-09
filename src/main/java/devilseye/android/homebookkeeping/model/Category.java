package devilseye.android.homebookkeeping.model;

public class Category {
    private int _id;
    private boolean _income;
    private String _name;

    public Category(){}

    public Category(int id, boolean income, String name){
        this._id=id;
        this._income=income;
        this._name=name;
    }

    public Category(boolean income, String name){
        this._income=income;
        this._name=name;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public boolean is_income() {
        return _income;
    }

    public void set_income(boolean _income) {
        this._income = _income;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }
}
