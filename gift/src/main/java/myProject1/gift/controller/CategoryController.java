package myProject1.gift.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myProject1.gift.dto.CategoryDto;
import myProject1.gift.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/admin/category/create")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    //==카테고리 등록 페이지 조회==//
    @GetMapping
    public String dispCreateCategoryForm(Model model){
        model.addAttribute("categoryDto", new CategoryDto());
        return "category/createCategoryForm";
    }

    //==카테고리 등록==//
    @PostMapping
    public String createCategory(@Valid @ModelAttribute CategoryDto categoryDto, BindingResult result){
        if(result.hasErrors()){
            return "category/createCategoryForm";
        }
        categoryService.createCategory(categoryDto);

        return "redirect:/admin/items/create";
    }
}
