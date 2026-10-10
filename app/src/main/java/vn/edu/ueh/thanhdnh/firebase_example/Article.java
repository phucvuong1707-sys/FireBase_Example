
package vn.edu.ueh.thanhdnh.firebase_example;

public class Article {

    private String id;
    private String title;
    private String content;
    private String image_url;

    // Constructor không tham số để Firestore đọc dữ liệu
    public Article() {
    }

    // Constructor đầy đủ thông tin
    public Article(String id, String title, String content, String image_url) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.image_url = image_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}