package devilseye.android.homebookkeeping.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Operation {
    private long _id;
    private Date _opDate;
    private OperationType _type;
    private double _value;
    private Account _from;
    private Account _to;
    private Category _category;
    private String _description;

    public Operation(int _id, Date _datetime, OperationType _type, double _value, Account _from, Account _to, Category _category, String _description) {
        this._id = _id;
        this._opDate = _datetime;
        this._type = _type;
        if (_value>=0) {
            this._value = _value;
        } else {
            this._value=-_value;
        }
        this._from = _from;
        this._to = _to;
        this._category = _category;
        this._description = _description;
    }

    public Operation(Date _datetime, OperationType _type, double _value, Account _from, Account _to, Category _category, String _description) {
        this._opDate = _datetime;
        this._type = _type;
        if (_value>=0) {
            this._value = _value;
        } else {
            this._value=-_value;
        }
        this._from = _from;
        this._to = _to;
        this._category = _category;
        this._description = _description;
    }

    public Operation(int _id, String _datetime, OperationType _type, double _value, Account _from, Account _to, Category _category, String _description) {
        this._id = _id;
        this._opDate = getDateTime(_datetime);
        this._type = _type;
        if (_value>=0) {
            this._value = _value;
        } else {
            this._value=-_value;
        }
        this._from = _from;
        this._to = _to;
        this._category = _category;
        this._description = _description;
    }

    public Operation(String _datetime, OperationType _type, double _value, Account _from, Account _to, Category _category, String _description) {
        this._opDate = getDateTime(_datetime);
        this._type = _type;
        if (_value>=0) {
            this._value = _value;
        } else {
            this._value=-_value;
        }
        this._from = _from;
        this._to = _to;
        this._category = _category;
        this._description = _description;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public Date get_datetime() {
        return _opDate;
    }

    public void set_datetime(Date _datetime) {
        this._opDate = _datetime;
    }

    public OperationType get_type() {
        return _type;
    }

    public void set_type(OperationType _type) {
        this._type = _type;
    }

    public double get_value() {
        return _value;
    }

    public void set_value(double _value) {
        if (_value>=0) {
            this._value = _value;
        } else {
            this._value=-_value;
        }
    }

    public Account get_from() {
        return _from;
    }

    public void set_from(Account _from) {
        this._from = _from;
    }

    public Account get_to() {
        return _to;
    }

    public void set_to(Account _to) {
        this._to = _to;
    }

    public Category get_category() {
        return _category;
    }

    public void set_category(Category _category) {
        this._category = _category;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public static String getStringDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm", Locale.getDefault());
        return dateFormat.format(date);
    }

    public static Date getDateTime(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm", Locale.getDefault());
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
