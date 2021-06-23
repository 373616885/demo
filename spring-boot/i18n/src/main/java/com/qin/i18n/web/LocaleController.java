package com.qin.i18n.web;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;
import java.util.ResourceBundle;


/**
 * @author qinjp
 */
@RestController
@AllArgsConstructor
public class LocaleController {

    private final MessageSource messageSource;

    @RequestMapping("/locale")
    public String locale() {
        //locale是获取http请求的header中的Accept-Language的值
        Locale locale = LocaleContextHolder.getLocale();
        String language = locale.getLanguage();
        if (locale.getLanguage().equals("en")) {
            locale = new Locale("en", "US");
        }
        if (locale.getLanguage().equals("zh")) {
            locale = new Locale("zh", "CN");
        }

        System.out.println("language: " + language);
        return messageSource.getMessage("mess.user.name", null, locale);
    }

    @RequestMapping("/bundle")
    public String bundle() {
        Locale locale = LocaleContextHolder.getLocale();
        String language = locale.getLanguage();
        if (locale.getLanguage().equals("en")) {
            locale = new Locale("en", "US");
        }
        if (locale.getLanguage().equals("zh")) {
            locale = new Locale("zh", "CN");
        }
        //locale是获取http请求的header中的Accept-Language的值
        ResourceBundle bundle = ResourceBundle.getBundle("messages/messages", locale);
        return bundle.getString("mess.user.name");
    }
}
