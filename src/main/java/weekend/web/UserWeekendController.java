package weekend.web;

import weekend.model.DateReq;
import weekend.model.User;
import weekend.model.Weekend;
import weekend.repository.WeekendRepository;
import weekend.service.SecurityService;
import weekend.service.UserService;
import weekend.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Controller
public class UserWeekendController {
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;
   @Autowired
    private WeekendRepository weekendRepository;

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.save(userForm);

        securityService.autologin(userForm.getUsername(), userForm.getPasswordConfirm());

        return "redirect:/welcome";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    @RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
    public String welcome(Model model) {
        return "welcome";
    }


    @RequestMapping(value = "/welcome", method = RequestMethod.POST)
    public String weekend(@ModelAttribute("dateReq") DateReq dateReq, BindingResult bindingResult, Model model) {
        if (dateReq == null) {
            return "welcome";
        }

        String message = "Number of days off: ";

        if(dateReq.getFromDate()==null || dateReq.getBeforeDate()==null) {
            if (message != null) {
                message = "Enter the correct date.";
            }
            return "welcome";
        }
        else {
            if (dateReq.getFromDate().equals(dateReq.getBeforeDate())) {
                if (message != null) {
                    message += 0;
                }
                return "welcome";
            }
        }
        //расчет количества выходных дней(суббота и воскресенье)
        LocalDate startDate = LocalDate.of(Integer.valueOf(dateReq.getFromDate().substring(0,4)),
                Integer.valueOf(dateReq.getFromDate().substring(5,7)),
                Integer.valueOf(dateReq.getFromDate().substring(8,10))); //год, мес, день
        LocalDate endDate = LocalDate.of(Integer.valueOf(dateReq.getBeforeDate().substring(0,4)),
                Integer.valueOf(dateReq.getBeforeDate().substring(5,7)),
                Integer.valueOf(dateReq.getBeforeDate().substring(8,10)));

        int weekdays = 0;
        while (startDate.isBefore(endDate)) {
            if (DayOfWeek.SATURDAY.equals(startDate.getDayOfWeek())
                    || DayOfWeek.SUNDAY.equals(startDate.getDayOfWeek())) {
                weekdays++;
            }
            startDate = startDate.plusDays(1);
        }

        //запрос к таблице holidays где поле weekend >=dateReq.getFromDate()) and weekend <=dateReq.getBeforeDate()
        List<Weekend> list = weekendRepository.findAllByWeekendBetween(Date.valueOf(dateReq.getFromDate()), Date.valueOf(dateReq.getBeforeDate()));
        //добавление количества дней с бд
        if (list!=null){
            weekdays+=list.size();
        }

        message+=weekdays;

        model.addAttribute("message", message);
        return "welcome";
    }
}

