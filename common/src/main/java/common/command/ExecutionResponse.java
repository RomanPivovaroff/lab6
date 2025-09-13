package common.command;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/** Класс для обратной связи команд */
@XmlRootElement
public class ExecutionResponse {
    private boolean isSucceeded;
    private String massage;

    public ExecutionResponse(boolean code, String s) {
        isSucceeded = code;
        massage = s;
    }

    public ExecutionResponse(String s) {
        this(true, s);
    }

    @XmlElement
    public boolean getIsSucceeded() {
        return isSucceeded;
    }

    @XmlElement
    public String getMassage() {
        return massage;
    }

    public String toString() {
        return String.valueOf(isSucceeded) + ";" + massage;
    }
}
