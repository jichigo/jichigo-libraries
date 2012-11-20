package org.jichigo.sample;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeAjaxController {

    private static final Logger logger = LoggerFactory.getLogger(HomeAjaxController.class);

    private AtomicLong counter = new AtomicLong(1);

    /**
     * Simply selects the home view to render by returning its name.
     */
    @RequestMapping(value = "/ajax/{id}", method = RequestMethod.GET)
    @ResponseBody
    public GenericJsonBean home(Locale locale, Model model, HttpSession session, HttpServletRequest request,
            @PathVariable Integer id) {
        logger.info("Welcome home! the client locale is " + locale.toString());
System.out.println(request.getHeader("Accept"));
        session.setAttribute("hoge", "test");
        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

        String formattedDate = dateFormat.format(date);

        model.addAttribute("serverTime", formattedDate);

        if (counter.getAndIncrement() % 2 == 0) {
             throw new OutOfMemoryError("test");
//            throw new IllegalArgumentException("hoge");
        }

        GenericJsonBean bean = new GenericJsonBean();
        bean.setResultCode("0");
        bean.setMessage(formattedDate + "/" + id);

        return bean;
    }
    //
    // @ExceptionHandler({ IllegalArgumentException.class, NullPointerException.class })
    // @ResponseBody
    // @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    // public GenericJsonBean handleRuntimeException(RuntimeException e, HttpServletRequest req, HttpServletResponse
    // res,
    // HttpSession session) {
    // session.invalidate();
    // GenericJsonBean bean = new GenericJsonBean();
    // bean.setResultCode("1");
    // bean.setMessage(e.getMessage());
    // return bean;
    // }

}
