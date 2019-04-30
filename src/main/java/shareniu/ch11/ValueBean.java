package shareniu.ch11;

import java.io.Serializable;

public class ValueBean implements Serializable {
    private  String value;
    public ValueBean(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
