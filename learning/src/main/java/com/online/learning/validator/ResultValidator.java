package com.online.learning.validator;

import com.online.learning.model.dto.ResultDTO;
import com.online.learning.model.entity.Result;
import com.online.learning.service.ResultService;
import com.online.learning.utils.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ResultValidator implements Validator {

    @Autowired
    private ResultService resultService;

    @Override
    public boolean supports(Class<?> clazz) {
        return ResultDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        try {
            ResultDTO resultDTO = (ResultDTO) target;
            // verify score
            if (resultDTO.getScore() == 0) {
                errors.rejectValue("score", "Vui lòng nhập kết quả!",
                        "Vui lòng nhập kết quả!");
            }

        } catch (Exception e) {
            errors.rejectValue("msg", "Có lỗi xảy ra, vui lòng thử lại!",
                    "Có lỗi xảy ra, vui lòng thử lại!");
        }
    }
}
