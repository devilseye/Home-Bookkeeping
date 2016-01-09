package devilseye.android.homebookkeeping.model;

public class OperationType {
    private int _id;
    private String _name;

    public OperationType(){}

    public OperationType(int id, String name){
        this._id=id;
        this._name=name;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public OperationType(String name){
        this._name=name;
    }
}
