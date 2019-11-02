package entity;

public class UserStatus {

    private int statusId;
    private String statusName;

    public UserStatus(int statusId, String statusName) {
        this.statusId = statusId;
        this.statusName = statusName;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    @Override
    public String toString() {
        return "UserStatus{" +
                "statusId=" + statusId +
                ", statusName='" + statusName + '\'' +
                '}';
    }
}