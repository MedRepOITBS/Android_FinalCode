package pharma.model;

/**
 * Created by kishore on 7/11/15.
 */
public class DoctorNotificationStats {
    private int notificationId;
    private String notificationName;
    private int totalCount;
    private double ratingAverage;
    private double prescribeYes;
    private double prescribeNo;
    private double favoriteYes;
    private double favoriteNo;
    private double recomendYes;
    private double recomendNo;

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationName() {
        return notificationName;
    }

    public void setNotificationName(String notificationName) {
        this.notificationName = notificationName;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public double getRatingAverage() {
        return ratingAverage;
    }

    public void setRatingAverage(double ratingAverage) {
        this.ratingAverage = ratingAverage;
    }

    public double getPrescribeYes() {
        return prescribeYes;
    }

    public void setPrescribeYes(double prescribeYes) {
        this.prescribeYes = prescribeYes;
    }

    public double getPrescribeNo() {
        return prescribeNo;
    }

    public void setPrescribeNo(double prescribeNo) {
        this.prescribeNo = prescribeNo;
    }

    public double getFavoriteYes() {
        return favoriteYes;
    }

    public void setFavoriteYes(double favoriteYes) {
        this.favoriteYes = favoriteYes;
    }

    public double getFavoriteNo() {
        return favoriteNo;
    }

    public void setFavoriteNo(double favoriteNo) {
        this.favoriteNo = favoriteNo;
    }

    public double getRecomendYes() {
        return recomendYes;
    }

    public void setRecomendYes(double recomendYes) {
        this.recomendYes = recomendYes;
    }

    public double getRecomendNo() {
        return recomendNo;
    }

    public void setRecomendNo(double recomendNo) {
        this.recomendNo = recomendNo;
    }
}
