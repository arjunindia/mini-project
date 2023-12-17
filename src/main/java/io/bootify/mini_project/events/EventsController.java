package io.bootify.mini_project.events;

import io.bootify.mini_project.user.User;
import io.bootify.mini_project.user.UserRepository;
import io.bootify.mini_project.util.CustomCollectors;
import io.bootify.mini_project.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
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
@RequestMapping("/eventss")
public class EventsController {

    private final EventsService eventsService;
    private final UserRepository userRepository;

    public EventsController(final EventsService eventsService,
            final UserRepository userRepository) {
        this.eventsService = eventsService;
        this.userRepository = userRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("usernameValues", userRepository.findAll(Sort.by("username"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getUsername, User::getUsername)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("eventses", eventsService.findAll());
        return "events/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("events") final EventsDTO eventsDTO) {
        return "events/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("events") @Valid final EventsDTO eventsDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "events/add";
        }
        eventsService.create(eventsDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("events.create.success"));
        return "redirect:/eventss";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Integer id, final Model model) {
        model.addAttribute("events", eventsService.get(id));
        return "events/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Integer id,
            @ModelAttribute("events") @Valid final EventsDTO eventsDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "events/edit";
        }
        eventsService.update(id, eventsDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("events.update.success"));
        return "redirect:/eventss";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Integer id,
            final RedirectAttributes redirectAttributes) {
        eventsService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("events.delete.success"));
        return "redirect:/eventss";
    }

}
