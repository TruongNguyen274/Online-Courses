package com.online.learning.controller.back;

import com.online.learning.service.CategoryService;
import com.online.learning.model.dto.CategoryDTO;
import com.online.learning.model.dto.MessageDTO;
import com.online.learning.model.entity.Category;
import com.online.learning.model.mapper.CategoryMapper;
import com.online.learning.utils.ValidatorUtil;
import com.online.learning.validator.CategoryValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/back/category")
public class CategoryController {

    private String redirectUrl = "/back/category";

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryValidator categoryValidator;

    @Autowired
    private ValidatorUtil validatorUtil;

    @GetMapping(value = {"", "/"})
    public String findAll(Model model) {
        try {
            List<Category> categoryList = categoryService.findAll();
            model.addAttribute("categoryList", categoryMapper.toListDTO(categoryList));

            return "back/category_list";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "redirect:" + redirectUrl;
        }
    }

    @GetMapping(value = {"/form"})
    public String create(Model model) {
        try {
            List<Category> categoryList = categoryService.findAll();
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setStatus(true);
            model.addAttribute("categoryDTO", categoryDTO);
            model.addAttribute("categoryList", categoryMapper.toListDTO(categoryList));
            return "back/category_form";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "redirect:" + redirectUrl;
        }
    }

    @GetMapping(value = {"/form/{id}"})
    public String edit(Model model, @PathVariable long id, @RequestParam(required = false) String action,
                       @RequestParam(required = false) String status) {
        try {
            Category category = categoryService.findById(id);
            if (category == null) {
                return "redirect:" + redirectUrl;
            }

            model.addAttribute("categoryDTO", categoryMapper.toDTO(category));
            model.addAttribute("errorList", new HashMap<>());

            if (action == null) {
                model.addAttribute("messageDTO", null);
            } else {
                model.addAttribute("messageDTO", new MessageDTO(action, status,
                        status.equalsIgnoreCase("success") ? "Cập nhật dữ liệu thành công!" : "Vui lòng kiểm tra lại thông tin!"));
            }

            return "back/category_form";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "redirect:" + redirectUrl;
        }
    }

    @PostMapping(value = "/form")
    public String save(Model model, CategoryDTO categoryDTO, BindingResult bindingResult) {
        String redirectUrl = "/category/";
        try {
            // verify value
            categoryValidator.validate(categoryDTO, bindingResult);

            if (bindingResult.hasErrors()) {
                model.addAttribute("errorList", validatorUtil.toErrors(bindingResult.getFieldErrors()));
                model.addAttribute("messageDTO", new MessageDTO("save", "warning",
                        "Vui lòng kiểm tra lại thông tin!"));
                return "/back/category_form";
            } else {
                // save
                Category category = categoryService.save(categoryDTO);
                redirectUrl = "/back/category/form/" + category.getId() + "?action=save&status=success";
                return "redirect:" + redirectUrl;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "redirect:" + redirectUrl;
        }
    }

    @GetMapping(value = {"/delete/{id}"})
    public String delete(Model model, @PathVariable long id) {
        try {
            categoryService.delete(id);
            redirectUrl = "/back/category" + "?action=save&status=success";

            return "redirect:" + redirectUrl;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "redirect:" + redirectUrl;
        }
    }

}
