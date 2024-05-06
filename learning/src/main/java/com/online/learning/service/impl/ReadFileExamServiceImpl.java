package com.online.learning.service.impl;

import com.online.learning.model.dto.ExamDTO;
import com.online.learning.model.dto.QuestionDTO;
import com.online.learning.service.ReadFileExamService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ReadFileExamServiceImpl implements ReadFileExamService {

    private static final int COLUMN_INDEX_EXAM = 1;

    private static final int ROW_EXAM = 7;

    private static final int COLUMN_INDEX_QUESTION = 0;

    private static final int COLUMN_INDEX_OPTION_1 = 1;

    private static final int COLUMN_INDEX_OPTION_2 = 2;

    private static final int COLUMN_INDEX_OPTION_3 = 3;

    private static final int COLUMN_INDEX_OPTION_4 = 4;

    private static final int COLUMN_INDEX_ANSWER = 5;

    @Override
    public ExamDTO readQuiz(ExamDTO examDTO) throws IOException {
        String pathFileExcel = examDTO.getPathFileExcel();
        // Get file
        InputStream inputStream = new FileInputStream(pathFileExcel);

        // Get workbook
        Workbook workbook = getWorkbook(inputStream, pathFileExcel);

        // Get sheet
        Sheet sheet = workbook.getSheetAt(0);
        // Get all rows
        for (Row nextRow : sheet) {
            // Get all cells
            Iterator<Cell> cellIterator = nextRow.cellIterator();

            // Read cells and set value for book object
            while (cellIterator.hasNext()) {
                //Read cell
                Cell cell = cellIterator.next();
                Object cellValue = getCellValue(cell);
                if (cellValue == null || cellValue.toString().isEmpty()) {
                    continue;
                }
                // Set value for book object
                int columnIndex = cell.getColumnIndex();
                if (columnIndex == COLUMN_INDEX_EXAM) {
                    switch (nextRow.getRowNum()) {
                        case 0:
                            examDTO.setTitle((String) getCellValue(cell));
                            break;
                        case 1:
                            examDTO.setDescription((String) getCellValue(cell));
                            break;
                        case 2:
                            examDTO.setType((String) getCellValue(cell));
                            break;
                        case 3:
                            examDTO.setKindExam((String) getCellValue(cell));
                            break;
                        case 4:
                            examDTO.setTotalTime((String) getCellValue(cell));
                            break;
                        case 5:
                            examDTO.setNumberOfDay((String) getCellValue(cell));
                            break;
                        default:
                            break;
                    }
                }

            }
        }

        List<QuestionDTO> questionDTOList = readListQuestion(pathFileExcel);
        examDTO.setQuestionDTOList(questionDTOList);

        workbook.close();
        inputStream.close();

        examDTO.setStatus(true);
        return examDTO;
    }

    private List<QuestionDTO> readListQuestion(String pathFileExcel) throws IOException {
        List<QuestionDTO> result = new ArrayList<>();

        // Get file
        InputStream inputStream = new FileInputStream(pathFileExcel);

        // Get workbook
        Workbook workbook = getWorkbook(inputStream, pathFileExcel);

        // Get sheet
        Sheet sheet = workbook.getSheetAt(0);

        // Get all rows
        for (Row nextRow : sheet) {
            // Ignore header
            if (nextRow.getRowNum() < ROW_EXAM) {
                continue;
            }

            // Get all cells
            Iterator<Cell> cellIterator = nextRow.cellIterator();

            // Read cells and set value for book object
            QuestionDTO questionDTO = new QuestionDTO();
            while (cellIterator.hasNext()) {
                //Read cell
                Cell cell = cellIterator.next();
                Object cellValue = getCellValue(cell);
                if (cellValue == null || cellValue.toString().isEmpty()) {
                    continue;
                }
                // Set value for book object
                int columnIndex = cell.getColumnIndex();
                switch (columnIndex) {
                    case COLUMN_INDEX_QUESTION:
                        questionDTO.setTitle((String) getCellValue(cell));
                        break;
                    case COLUMN_INDEX_OPTION_1:
                        questionDTO.setOption1((String) getCellValue(cell));
                        break;
                    case COLUMN_INDEX_OPTION_2:
                        questionDTO.setOption2((String) getCellValue(cell));
                        break;
                    case COLUMN_INDEX_OPTION_3:
                        questionDTO.setOption3((String) getCellValue(cell));
                        break;
                    case COLUMN_INDEX_OPTION_4:
                        questionDTO.setOption4((String) getCellValue(cell));
                        break;
                    case COLUMN_INDEX_ANSWER:
                        String answer = (String) getCellValue(cell);
                        switch (answer.trim()) {
                            case "Đáp Án 1":
                                questionDTO.setAnswer(questionDTO.getOption1());
                                break;
                            case "Đáp Án 2":
                                questionDTO.setAnswer(questionDTO.getOption2());
                                break;
                            case "Đáp Án 3":
                                questionDTO.setAnswer(questionDTO.getOption3());
                                break;
                            case "Đáp Án 4":
                                questionDTO.setAnswer(questionDTO.getOption4());
                                break;
                            default:
                                break;
                        }
                    default:
                        break;
                }
            }
            result.add(questionDTO);
        }

        workbook.close();
        inputStream.close();

        return result;
    }

    // Get Workbook
    private Workbook getWorkbook(InputStream inputStream, String excelFilePath) throws IOException {
        Workbook workbook;
        if (excelFilePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } else if (excelFilePath.endsWith("xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }
        return workbook;
    }

    // Get cell value
    private Object getCellValue(Cell cell) {
        CellType cellType = cell.getCellType();
        Object cellValue = null;
        switch (cellType) {
            case BOOLEAN:
                cellValue = cell.getBooleanCellValue();
                break;
            case FORMULA:
                Workbook workbook = cell.getSheet().getWorkbook();
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                cellValue = evaluator.evaluate(cell).getNumberValue();
                break;
            case NUMERIC:
                cellValue = cell.getNumericCellValue();
                break;
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            default:
                break;
        }
        return cellValue;
    }

}
