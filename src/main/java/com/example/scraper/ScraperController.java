package com.example.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ScraperController {

    // Hiển thị màn hình nhập liệu ban đầu
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // Nhận URL người dùng gửi lên và xử lý cào dữ liệu
    @PostMapping("/scrape")
    public String scrapeUrl(@RequestParam("targetUrl") String targetUrl, Model model) {
        model.addAttribute("inputUrl", targetUrl);

        try {
            // Kết nối đến trang web đích bằng Jsoup
            Document doc = Jsoup.connect(targetUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(10000)
                    .get();

            // Tìm phần tử có id="lst_content"
            Element contentElement = doc.getElementById("lst_content");

            if (contentElement != null) {
                // Lấy nội dung text bên trong
                model.addAttribute("scrapedContent", contentElement.text());
            } else {
                model.addAttribute("error", "Không tìm thấy phần tử có id=\"lst_content\" trên trang này.");
            }

        } catch (Exception e) {
            model.addAttribute("error", "Lỗi kết nối hoặc URL không hợp lệ: " + e.getMessage());
        }

        return "index"; // Trả về giao diện kèm kết quả
    }
}