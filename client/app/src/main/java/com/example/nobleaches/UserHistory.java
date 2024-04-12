import java.util.ArrayList;
import java.util.List;

public class UserHistory {
    private String machineName;
    private String date;
    private String time;

    public UserHistory(String machineName, String date, String time) {
        this.machineName = machineName;
        this.date = date;
        this.time = time;
    }

    // Getters and setters
    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
