package com.online.learning.validator;

import com.online.learning.model.dto.ChapterDTO;
import com.online.learning.utils.ConstantUtil;
import com.online.learning.utils.ValidatorUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ChapterValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        try {
            ChapterDTO chapterDTO = (ChapterDTO) target;

            if (ValidatorUtil.isEmpty(chapterDTO.getTitle())) {
                errors.rejectValue("title", "Vui lòng nhập Tiêu đề!",
                        "Vui lòng nhập Tiêu đề!");
            } else {
                if (chapterDTO.getTitle().length() > ConstantUtil.TITLE_LENGTH) {
                    errors.rejectValue("title",
                            "Tên không được vượt quá 255 kí tự",
                            "Tên không được vượt quá 255 kí tự");
                }
            }

            if (ValidatorUtil.isEmpty(chapterDTO.getDescription())) {
                errors.rejectValue("content", "Vui lòng nhập Mô Tả!",
                        "Vui lòng nhập Mô Tả!");
            }

            if (chapterDTO.getCourseId() == 0) {
                errors.rejectValue("courseId", "Vui lòng chọn Khóa Học!",
                        "Vui lòng chọn Khóa Học!");
            }

            if (!ValidatorUtil.isEmpty(chapterDTO.getSortOrder()) &&
                    !ValidatorUtil.isNumeric(chapterDTO.getSortOrder())) {
                errors.rejectValue("sortOrder", "Thứ tự phải là kiểu số!",
                        "Thứ tự phải là kiểu số!");
            }

        } catch (Exception e) {
            errors.rejectValue("msg", "Có lỗi xảy ra, vui lòng thử lại!",
                    "Có lỗi xảy ra, vui lòng thử lại!");
        }
    }
}
