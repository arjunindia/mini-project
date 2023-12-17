package io.bootify.mini_project.user;

import io.bootify.mini_project.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("users", userService.findAll());
        return "user/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("user") final UserDTO userDTO) {
        return "user/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("user") @Valid final UserDTO userDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("username") && userDTO.getUsername() == null) {
            bindingResult.rejectValue("username", "NotNull");
        }
        if (!bindingResult.hasFieldErrors("username") && userService.usernameExists(userDTO.getUsername())) {
            bindingResult.rejectValue("username", "Exists.user.username");
        }
        if (bindingResult.hasErrors()) {
            return "user/add";
        }
        userService.create(userDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("user.create.success"));
        return "redirect:/users";
    }

    @GetMapping("/edit/{username}")
    public String edit(@PathVariable(name = "username") final String username, final Model model) {
        model.addAttribute("user", userService.get(username));
        return "user/edit";
    }

    @PostMapping("/edit/{username}")
    public String edit(@PathVariable(name = "username") final String username,
            @ModelAttribute("user") @Valid final UserDTO userDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "user/edit";
        }
        userService.update(username, userDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("user.update.success"));
        return "redirect:/users";
    }

    @PostMapping("/delete/{username}")
    public String delete(@PathVariable(name = "username") final String username,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = userService.getReferencedWarning(username);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            userService.delete(username);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("user.delete.success"));
        }
        return "redirect:/users";
    }

}
