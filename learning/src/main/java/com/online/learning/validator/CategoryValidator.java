package com.online.learning.validator;
import com.online.learning.service.CategoryService;
import com.online.learning.model.dto.CategoryDTO;
import com.online.learning.utils.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CategoryValidator implements Validator {

    @Autowired
    private CategoryService categoryService;


    @Override
    public boolean supports(Class<?> clazz) {
        return CategoryDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        try {
            CategoryDTO categoryDTO = (CategoryDTO) target;
            if (categoryDTO != null) {
                // verify title
                if (ValidatorUtil.isEmpty(categoryDTO.getTitle())) {
                    errors.rejectValue("title", "Vui lòng nhập Tiêu đề!",
                            "Vui lòng nhập Tiêu đề!");
                } else {
                    if (categoryDTO.getTitle().length()>255){
                        errors.rejectValue("title","Tên không được vượt quá 255 kí tự","Tên không được vượt quá 255 kí tự");
                    }
                }

                // verify description
                if (ValidatorUtil.isEmpty(categoryDTO.getDescription())) {
                    errors.rejectValue("description", "Vui lòng nhập mô tả!",
                            "Vui lòng nhập mô tả!");
                }
            }


        } catch (Exception e) {
            errors.rejectValue("msg", "Có lỗi xảy ra, vui lòng thử lại!",
                    "Có lỗi xảy ra, vui lòng thử lại!");
        }
    }
}
