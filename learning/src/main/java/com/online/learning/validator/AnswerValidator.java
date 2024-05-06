package com.online.learning.validator;

import com.online.learning.model.dto.AnswerDTO;
import com.online.learning.model.entity.Answer;
import com.online.learning.service.AnswerService;
import com.online.learning.utils.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AnswerValidator implements Validator {

    @Autowired
    private AnswerService answerService;


    @Override
    public boolean supports(Class<?> clazz) {
        return AnswerDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        try {
            AnswerDTO answerDTO = (AnswerDTO) target;
            Answer answer = null;
            if (answerDTO != null) {
                // verify option
                if (ValidatorUtil.isEmpty(answerDTO.getOption())) {
                    errors.rejectValue("option", "Vui lòng nhập lựa chọn!",
                            "Vui lòng nhập lựa chọn!");
                }

            }


        } catch (Exception e) {
            errors.rejectValue("msg", "Có lỗi xảy ra, vui lòng thử lại!",
                    "Có lỗi xảy ra, vui lòng thử lại!");
        }
    }
}
