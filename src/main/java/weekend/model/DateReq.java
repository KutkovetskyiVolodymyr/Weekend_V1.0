package weekend.model;

public class DateReq {
    private String fromDate;
    private String beforeDate;


    public DateReq() {

    }

    public DateReq(String firstDate, String beforeDate) {
        this.fromDate = firstDate;
        this.beforeDate = beforeDate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String firstDate) {
        this.fromDate = firstDate;
    }

    public String getBeforeDate() {
        return beforeDate;
    }

    public void setBeforeDate(String beforeDate) {
        this.beforeDate = beforeDate;
    }
}
