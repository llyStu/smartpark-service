package com.vibe.service.system;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.vibe.controller.system.LoginController;
import com.vibe.pojo.MenuBean;
import com.vibe.pojo.user.User;
import com.vibe.service.global.MenuService;

@Component
//@Aspect
@Order(1)
public class CheckPermission {
    private static final String KEY_REENTRY = CheckPermission.class.getName() + ".FLAG_REENTRY";
    private static final String KEY_MODULE_PATH = "ATTACH_MODULE_PATH";
    public static final String READ = ":READ";
    public static final String WRITE = ":WRITE";

    @Autowired
    private MenuService ms;

    private MenuBean modules;

    //@PostConstruct
    private void init(HttpServletRequest request) {
        modules = ms.getMenus(request);
    }

    @Pointcut("execution(* com.vibe.controller..*.*(..))")
    public void doController() {
    }

    private Pattern urlTest = Pattern.compile("save|create|insert|delete|update");

    private static final Set<Class<?>> skipController = new HashSet<>(Arrays.asList(LoginController.class));

    //@Around("doController()")
    public Object check(ProceedingJoinPoint point) throws Throwable {
        do {
            if (skipController.contains(point.getTarget().getClass())) break;

            ServletRequestAttributes ctx = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            User user = (User) ctx.getAttribute("loginUser", RequestAttributes.SCOPE_SESSION);
            if (user == null || "admin".equals(user.getLogin_id())
                    || KEY_REENTRY.equals(ctx.getAttribute(KEY_REENTRY, RequestAttributes.SCOPE_REQUEST))) {
                break;
            }

            Method method = ((MethodSignature) point.getSignature()).getMethod();
            if (method.getAnnotation(RequestMapping.class) == null) break;

            ctx.setAttribute(KEY_REENTRY, KEY_REENTRY, RequestAttributes.SCOPE_REQUEST);
            if (existAuth(method)) break;


            HttpServletRequest req = ctx.getRequest();
            MenuBean module = toModule(req.getParameter(KEY_MODULE_PATH));
            if (module == null) break;

            String moduleName = module.getCaption();
            String url = req.getRequestURI();
            String operation = urlTest.matcher(url).find() ? WRITE : READ;
            if (SecurityUtils.getSubject().isPermitted(moduleName + operation)) break;

            HttpServletResponse resp = ctx.getResponse();
            resp.setStatus(401);
            req.getRequestDispatcher("/html/unauthorized.html").forward(req, resp);
            return null;
        } while (false);
        return point.proceed();
    }

    @SuppressWarnings("rawtypes")
    private static final Class[] shiroAnno = {
            RequiresRoles.class, RequiresPermissions.class, RequiresAuthentication.class, RequiresGuest.class, RequiresUser.class
    };

    @SuppressWarnings("rawtypes")
    private boolean existAuth(Method method) {
        for (Class it : shiroAnno) {
            if (method.isAnnotationPresent(it)) return true;
        }
        return false;
    }

    private MenuBean toModule(String path) {
        if (path == null || (path = path.trim()).length() == 0) return null;

        for (MenuBean it : modules.getChildren()) {
            if (path.endsWith(it.getUrl())) return it;
        }
        return null;
    }
}
