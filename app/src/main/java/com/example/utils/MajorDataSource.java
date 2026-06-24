package com.example.utils;

import com.example.models.Major;

import java.util.ArrayList;
import java.util.List;

/**
 * Nguồn dữ liệu các ngành học tại trường UEL.
 * Mỗi ngành có tên, mô tả, URL và danh sách từ khóa liên quan
 * để xây dựng TF-IDF vector phục vụ tìm kiếm ngữ nghĩa.
 */
public class MajorDataSource {

    public static List<Major> getMajors() {
        List<Major> list = new ArrayList<>();

        list.add(new Major(
                "Hệ thống thông tin quản lý",
                "Ngành đào tạo chuyên gia về thiết kế, xây dựng và quản lý hệ thống thông tin trong doanh nghiệp. Ứng dụng công nghệ thông tin vào quản lý.",
                "Khoa Hệ thống thông tin",
                "https://myuel.uel.edu.vn/Default.aspx?ModuleId=f92f39b2-dea3-4185-8cbb-56c1c49c5226&OlogyID=406&DepartmentID=05&GraduateLevelID=DH&StudyTypeID=CQ",
                new String[]{"httt", "hệ thống", "thông tin", "quản lý", "information", "management", "system", "MIS", "cơ sở dữ liệu", "database", "phần mềm", "software", "CNTT", "công nghệ thông tin", "IT"}
        ));

        list.add(new Major(
                "Thương mại điện tử",
                "Ngành đào tạo chuyên gia về kinh doanh trực tuyến, marketing số, thanh toán điện tử và logistics thương mại điện tử.",
                "Khoa Hệ thống thông tin",
                "https://myuel.uel.edu.vn/Default.aspx?ModuleId=f92f39b2-dea3-4185-8cbb-56c1c49c5226&OlogyID=411&DepartmentID=05&GraduateLevelID=DH&StudyTypeID=CQ",
                new String[]{"tmdt", "thương mại", "điện tử", "ecommerce", "e-commerce", "kinh doanh trực tuyến", "marketing số", "digital", "bán hàng online", "thanh toán điện tử", "logistics", "shopee", "lazada", "online"}
        ));

        list.add(new Major(
                "Kinh doanh số và trí tuệ nhân tạo",
                "Ngành kết hợp kinh doanh kỹ thuật số với trí tuệ nhân tạo, machine learning và phân tích dữ liệu lớn để tạo ra giá trị kinh doanh.",
                "Khoa Hệ thống thông tin",
                "https://myuel.uel.edu.vn/Default.aspx?ModuleId=f92f39b2-dea3-4185-8cbb-56c1c49c5226&OlogyID=416&DepartmentID=05&GraduateLevelID=DH&StudyTypeID=CQ",
                new String[]{"AI", "trí tuệ nhân tạo", "artificial intelligence", "machine learning", "deep learning", "kinh doanh số", "digital business", "dữ liệu lớn", "big data", "data science", "khoa học dữ liệu", "chatbot", "tự động hóa", "automation"}
        ));

        list.add(new Major(
                "Kinh tế",
                "Ngành đào tạo chuyên gia phân tích kinh tế vi mô, vĩ mô, chính sách kinh tế và dự báo kinh tế.",
                "Khoa Kinh tế",
                "https://myuel.uel.edu.vn/Default.aspx?ModuleId=f92f39b2-dea3-4185-8cbb-56c1c49c5226&OlogyID=401&DepartmentID=01&GraduateLevelID=DH&StudyTypeID=CQ",
                new String[]{"kinh tế", "economics", "kinh tế vi mô", "kinh tế vĩ mô", "micro", "macro", "chính sách", "policy", "phân tích", "tài chính", "đầu tư"}
        ));

        list.add(new Major(
                "Quản trị kinh doanh",
                "Đào tạo kỹ năng quản trị doanh nghiệp, chiến lược kinh doanh, quản lý nhân sự và lãnh đạo tổ chức.",
                "Khoa Quản trị",
                "https://myuel.uel.edu.vn/Default.aspx?ModuleId=f92f39b2-dea3-4185-8cbb-56c1c49c5226&OlogyID=402&DepartmentID=02&GraduateLevelID=DH&StudyTypeID=CQ",
                new String[]{"quản trị", "kinh doanh", "MBA", "business", "administration", "management", "nhân sự", "lãnh đạo", "chiến lược", "strategy", "doanh nghiệp", "tổ chức"}
        ));

        list.add(new Major(
                "Kế toán",
                "Đào tạo kỹ năng lập báo cáo tài chính, kiểm toán, kế toán quản trị và thuế.",
                "Khoa Kế toán",
                "https://myuel.uel.edu.vn/Default.aspx?ModuleId=f92f39b2-dea3-4185-8cbb-56c1c49c5226&OlogyID=403&DepartmentID=03&GraduateLevelID=DH&StudyTypeID=CQ",
                new String[]{"kế toán", "accounting", "kiểm toán", "audit", "tài chính", "finance", "báo cáo tài chính", "thuế", "tax", "ngân hàng", "kế toán quản trị"}
        ));

        list.add(new Major(
                "Marketing",
                "Đào tạo chuyên gia marketing, nghiên cứu thị trường, xây dựng thương hiệu và chiến lược truyền thông.",
                "Khoa Marketing",
                "https://myuel.uel.edu.vn/Default.aspx?ModuleId=f92f39b2-dea3-4185-8cbb-56c1c49c5226&OlogyID=404&DepartmentID=04&GraduateLevelID=DH&StudyTypeID=CQ",
                new String[]{"marketing", "thị trường", "thương hiệu", "brand", "quảng cáo", "advertising", "truyền thông", "media", "digital marketing", "SEO", "social media", "nghiên cứu thị trường"}
        ));

        list.add(new Major(
                "Tài chính - Ngân hàng",
                "Đào tạo chuyên gia phân tích tài chính, đầu tư chứng khoán, quản trị rủi ro và hoạt động ngân hàng.",
                "Khoa Tài chính",
                "https://myuel.uel.edu.vn/Default.aspx?ModuleId=f92f39b2-dea3-4185-8cbb-56c1c49c5226&OlogyID=405&DepartmentID=06&GraduateLevelID=DH&StudyTypeID=CQ",
                new String[]{"tài chính", "ngân hàng", "finance", "banking", "đầu tư", "investment", "chứng khoán", "stock", "rủi ro", "risk", "bảo hiểm", "insurance", "tiền tệ"}
        ));

        return list;
    }
}
