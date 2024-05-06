package com.online.learning.validator;

import com.online.learning.model.dto.QuestionDTO;
import com.online.learning.utils.ValidatorUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class QuestionValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return QuestionDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        QuestionDTO questionDTO = (QuestionDTO) target;

        // verify title
        if (ValidatorUtil.isEmpty(questionDTO.getTitle())) {
            errors.rejectValue("title", "Vui lòng nhập câu hỏi!",
                    "Vui lòng nhập câu hỏi!");
        } else {
            if (questionDTO.getTitle().length() > 255) {
                errors.rejectValue("title", "Câu hỏi không được vượt quá 255 kí tự",
                        "Câu hỏi không được vượt quá 255 kí tự");
            }
        }

        if (questionDTO.getExamType().equalsIgnoreCase("Trắc Nghiệm")) {
            if (ValidatorUtil.isEmpty(questionDTO.getAnswer())) {
                errors.rejectValue("answer", "Vui lòng nhập câu trả lời!",
                        "Vui lòng nhập câu trả lời!");
            }

            if (ValidatorUtil.isEmpty(questionDTO.getOption1())) {
                errors.rejectValue("option1", "Vui lòng nhập đáp án 1!",
                        "Vui lòng nhập đáp án 1!");
            }

            if (ValidatorUtil.isEmpty(questionDTO.getOption2())) {
                errors.rejectValue("option2", "Vui lòng nhập đáp án 2!",
                        "Vui lòng nhập đáp án 2!");
            }

            if (ValidatorUtil.isEmpty(questionDTO.getOption3())) {
                errors.rejectValue("option3", "Vui lòng nhập đáp án 3!",
                        "Vui lòng nhập đáp án 3!");
            }

            if (ValidatorUtil.isEmpty(questionDTO.getOption4())) {
                errors.rejectValue("option4", "Vui lòng nhập đáp án 4!",
                        "Vui lòng nhập đáp án 4!");
            }
        }
    }
}
