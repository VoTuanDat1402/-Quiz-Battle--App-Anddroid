package com.example.demoquiz;

public class User {
    private String guestId;
    private String username;
    private int score;

    public User() {
        // Bắt buộc phải có hàm khởi tạo trống này để Firebase có thể giải mã dữ liệu sau này
    }

    public User(String guestId, String username, int score) {
        this.guestId = guestId;
        this.username = username;
        this.score = score;
    }

    public String getGuestId() { return guestId; }
    public void setGuestId(String guestId) { this.guestId = guestId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
}
