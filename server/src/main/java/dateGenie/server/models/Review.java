package dateGenie.server.models;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Review {
    private String authorName;
    private Double rating;
    private String text;
    private String date;

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("authorName", authorName)
                .add("rating", rating)
                .add("text", text)
                .add("time", date)
                .build();
    }
    
    public static Review createReviewFromSql(SqlRowSet rs) {

        final Review review = new Review();

        review.setAuthorName(rs.getString("author_name"));
        review.setRating(rs.getDouble("rating"));
        review.setText(rs.getString("review"));
        review.setDate(rs.getString("review_date"));

        return review;
    }

    public static Review createReview(JsonObject doc) {

        final Review review = new Review();

        review.setAuthorName(doc.getString("authorName"));
        review.setRating(doc.getJsonNumber("rating").doubleValue());
        review.setText(doc.getString("text"));
        review.setDate(doc.getString("time"));

        return review;
    }

    @Override
    public String toString() {
        return "Restaurant {authorName=%s, rating=%f, text=%s, date=%s}"
                .formatted(authorName, rating, text, date);
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
