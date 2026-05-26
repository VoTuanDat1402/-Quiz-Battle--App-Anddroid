package com.example.demoquiz;
import com.example.demoquiz.Question;
import java.util.ArrayList;
import java.util.Collections;

public class QuestionBank {

    public static ArrayList<Question> getQuestions() {
        ArrayList<Question> list = new ArrayList<>();

        // Thêm các câu hỏi mẫu vào đây
        list.add(new Question(
                "Hành tinh nào trong hệ Mặt Trời được gọi là Hành tinh Đỏ?",
                "Sao Mộc", "Sao Kim", "Sao Hỏa", "Sao Thổ", "C"
        ));

        list.add(new Question(
                "Ngôn ngữ lập trình chính thức được Google khuyến khích cho Android hiện tại là gì?",
                "Java", "Kotlin", "Swift", "C++", "B"
        ));

        list.add(new Question(
                "Nước Việt Nam nằm ở châu lục nào?",
                "Châu Âu", "Châu Á", "Châu Mỹ", "Châu Phi", "B"
        ));

        list.add(new Question(
                "Số nào sau đây là số nguyên tố?",
                "4", "6", "9", "7", "D"
        ));

        list.add(new Question(
                "Đâu là tên một tác phẩm văn học nổi tiếng của Nguyễn Du?",
                "Truyện Kiều", "Lục Vân Tiên", "Tắt Đèn", "Chữ người tử tù", "A"
        ));

        // Trộn ngẫu nhiên thứ tự các câu hỏi mỗi lần chơi
        Collections.shuffle(list);
        return list;
    }
}
