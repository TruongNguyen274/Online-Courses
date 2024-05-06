package com.online.learning.validator;

import com.online.learning.model.dto.CourseDTO;
import com.online.learning.utils.ConstantUtil;
import com.online.learning.utils.ValidatorUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CourseValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return CourseDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        try {
            CourseDTO courseDTO = (CourseDTO) target;

            if (ValidatorUtil.isEmpty(courseDTO.getTitle())) {
                errors.rejectValue("title", "Tiêu Đề không được để trống!",
                        "Tiêu Đề không được để trống!");
            } else {
                if (courseDTO.getTitle().length() > ConstantUtil.TITLE_LENGTH) {
                    errors.rejectValue("title", "Tiêu Đề không được vượt quá 255 kí tự!",
                            "Tiêu Đề không được vượt quá 255 kí tự!");
                }
            }

            if (ValidatorUtil.isEmpty(courseDTO.getDescription())) {
                errors.rejectValue("description", "Mô Tả không được để trống!",
                        "Mô Tả không được để trống!");
            }

            if (ValidatorUtil.isEmpty(courseDTO.getPrice())) {
                errors.rejectValue("price", "Giá Tiền không được để trống!",
                        "Giá Tiền không được để trống!");
            } else {
                if (!ValidatorUtil.isNumeric(courseDTO.getPrice())) {
                    errors.rejectValue("price", "Giá Tiền phải là kiểu số!",
                            "Giá Tiền phải là kiểu số!");
                }
            }

            if (!ValidatorUtil.isEmpty(courseDTO.getDiscount()) &&
                    !ValidatorUtil.isNumeric(courseDTO.getDiscount())) {
                errors.rejectValue("discount", "Giảm Giá phải là kiểu số!",
                        "Giảm Giá phải là kiểu số!");
            }

            if (courseDTO.getCategoryId() == 0) {
                errors.rejectValue("categoryId", "Vui lòng chọn Danh Mục!",
                        "Vui lòng chọn Danh Mục!");
            }

            if (ValidatorUtil.isEmpty(courseDTO.getSummary())) {
                errors.rejectValue("summary", "Giới Thiệu không được để trống!",
                        "Giới Thiệu không được để trống!");
            }

            if (ValidatorUtil.isEmpty(courseDTO.getDuration())) {
                errors.rejectValue("duration", "Thời gian khóa học không được để trống!",
                        "Thời gian khóa học không được để trống!");
            }
        } catch (Exception e) {
            errors.rejectValue("msg", "Có lỗi xảy ra, vui lòng thử lại!",
                    "Có lỗi xảy ra, vui lòng thử lại!");
        }
    }

}
