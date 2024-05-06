package com.online.learning.validator;

import com.online.learning.model.dto.ExamDTO;
import com.online.learning.model.dto.QuestionDTO;
import com.online.learning.utils.ValidatorUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class ExamValidator implements Validator {

    private static final String TYPE_EXAM = "Trắc Nghiệm";

    @Override
    public boolean supports(Class<?> clazz) {
        return ExamDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ExamDTO examDTO = (ExamDTO) target;
        // verify title
        if (ValidatorUtil.isEmpty(examDTO.getTitle())) {
            errors.rejectValue("title", "Vui lòng nhập tiêu đề!",
                    "Vui lòng nhập tiêu đề!");
        }

        // verify description
        if (ValidatorUtil.isEmpty(examDTO.getDescription())) {
            errors.rejectValue("description", "Vui lòng nhập mô tả!",
                    "Vui lòng nhập mô tả!");
        }

        if (ValidatorUtil.isEmpty(examDTO.getType())) {
            errors.rejectValue("type", "Vui lòng nhập hình thức kiểm tra!",
                    "Vui lòng nhập hình thức kiểm tra!");
        }

        if (ValidatorUtil.isEmpty(examDTO.getKindExam())) {
            errors.rejectValue("kindExam", "Vui lòng nhập loại kiểm tra!",
                    "Vui lòng nhập loại kiểm tra!");
        } else {
            switch (examDTO.getKindExam()) {
                case "Khóa Học":
                    if (examDTO.getCourseId() == 0) {
                        errors.rejectValue("kindExam", "Loại Kiểm Tra không phải Khóa Học! Vui lòng kiểm tra lại tài liệu",
                                "Loại Kiểm Tra không phải Khóa Học! Vui lòng kiểm tra lại tài liệu");
                    }
                    break;
                case "Chương":
                    if (examDTO.getChapterId() == 0) {
                        errors.rejectValue("kindExam", "Loại Kiểm Tra không phải Chương! Vui lòng kiểm tra lại tài liệu",
                                "Loại Kiểm Tra không phải Chương! Vui lòng kiểm tra lại tài liệu");
                    }
                    break;
                default:
                    if (examDTO.getLectureId() == 0) {
                        errors.rejectValue("kindExam", "Loại Kiểm Tra không phải Bài Học! Vui lòng kiểm tra lại tài liệu",
                                "Loại Kiểm Tra không phải Bài Học! Vui lòng kiểm tra lại tài liệu");
                    }
                    break;
            }
        }

        if (ValidatorUtil.isEmpty(examDTO.getTotalTime())) {
            errors.rejectValue("totalTime", "Thời gian làm bài không được để trống!!!",
                    "Thời gian làm bài không được để trống!!!");
        }

        if (ValidatorUtil.isEmpty(examDTO.getNumberOfDay())) {
            errors.rejectValue("numberOfDay", "Số lần thi không được để trống!!!",
                    "Số lần thi không được để trống!!!");
        }

        List<QuestionDTO> questionDTOList = examDTO.getQuestionDTOList();
        if (questionDTOList.isEmpty()) {
            errors.rejectValue("questionFailed", "Vui lòng kiểm tra lại tài liệu!",
                    "Vui lòng kiểm tra lại tài liệu!");
        } else {
            for (QuestionDTO questionDTO : questionDTOList) {
                if (ValidatorUtil.isEmpty(questionDTO.getTitle())) {
                    errors.rejectValue("questionFailed", "Câu hỏi hoặc câu trả lời bị sai cấu trúc! Vui lòng kiểm tra tài liệu!",
                            "Câu hỏi hoặc câu trả lời bị sai cấu trúc! Vui lòng kiểm tra tài liệu!");
                    break;
                }

                if (examDTO.getType().equalsIgnoreCase(TYPE_EXAM)) {
                    if (ValidatorUtil.isEmpty(questionDTO.getAnswer()) || ValidatorUtil.isEmpty(questionDTO.getOption1()) ||
                            ValidatorUtil.isEmpty(questionDTO.getOption2()) || ValidatorUtil.isEmpty(questionDTO.getOption3()) ||
                            ValidatorUtil.isEmpty(questionDTO.getOption4())
                    ) {
                        errors.rejectValue("questionFailed", "Câu hỏi hoặc câu trả lời bị sai cấu trúc! Vui lòng kiểm tra tài liệu!",
                                "Câu hỏi hoặc câu trả lời bị sai cấu trúc! Vui lòng kiểm tra tài liệu!");
                        break;
                    }
                }
            }
        }
    }
}
