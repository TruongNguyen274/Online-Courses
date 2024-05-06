package com.online.learning.validator;

import com.online.learning.model.dto.LectureDTO;
import com.online.learning.utils.ConstantUtil;
import com.online.learning.utils.ValidatorUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class LectureValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return LectureDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        try {
            LectureDTO lectureDTO = (LectureDTO) target;

            if (ValidatorUtil.isEmpty(lectureDTO.getTitle())) {
                errors.rejectValue("title", "Vui lòng nhập Tiêu đề!",
                        "Vui lòng nhập Tiêu đề!");
            } else {
                if (lectureDTO.getTitle().length() > ConstantUtil.TITLE_LENGTH) {
                    errors.rejectValue("title",
                            "Tên không được vượt quá 255 kí tự",
                            "Tên không được vượt quá 255 kí tự");
                }
            }

            if (ValidatorUtil.isEmpty(lectureDTO.getContent())) {
                errors.rejectValue("content", "Vui lòng nhập Nội dung!",
                        "Vui lòng nhập Nội dung!");
            }

            if (lectureDTO.getChapterId() == 0) {
                errors.rejectValue("chapterId", "Vui lòng chọn Chương!",
                        "Vui lòng chọn Chương!");
            }

            if (ValidatorUtil.isEmpty(lectureDTO.getTimeLesson())) {
                errors.rejectValue("timeLesson", "Vui lòng nhập Thời lượng bài học!",
                        "Vui lòng nhập Thời lượng bài học!");
            }
        } catch (Exception e) {
            errors.rejectValue("msg", "Có lỗi xảy ra, vui lòng thử lại!",
                    "Có lỗi xảy ra, vui lòng thử lại!");
        }
    }
}
