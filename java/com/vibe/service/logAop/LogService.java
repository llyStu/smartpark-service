package com.vibe.service.logAop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vibe.mapper.global.LogMapper;
import com.vibe.pojo.user.User;
import com.vibe.utils.EasyUIJson;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

@Aspect
@Service
public class LogService {

    private String OUT = "out";//实际外部用户

    @Autowired
    private LogMapper logMapper;

    public LogService() {
        System.out.println("Aop");
    }

    /**
     * 切点
     */
    @Pointcut(value = "execution (* com.vibe.controller..*.*(..))")
    public void methodCachePointcut() {
    }


    /**
     * 切面
     *
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("methodCachePointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long startTimeMillis = System.currentTimeMillis(); //记录方法开始执行的时间
        MethodValue mthodRemark = getMthodRemark(point);
        String option = mthodRemark.getOption();
        String remark = mthodRemark.getRemark();
        User userout = getLoginUser();

        Object object = null;
        try {
            object = point.proceed();
            mthodRemark.setReturnValue(object);

        } catch (Exception e) {
            // 异常处理记录日志..log.error(e);
            throw e;
        }
        long endTimeMillis = System.currentTimeMillis();
        if (option != null) {
            if (object != null && object.toString().equals("/system/login/login") && option.equals("用户登录")) {
                return object;
            }
            Syslog syslog = new Syslog();
            if (remark.equals("logout")) {
                syslog.setLoginId(userout.getId());
                syslog.setLoginName(userout.getLogin_id());

            }

            setSyslog(mthodRemark, option, startTimeMillis, endTimeMillis, syslog);
            syslog.setOperatingContent(mthodRemark.getOption());

            System.out.println(syslog.toString());
            logMapper.insertLog(syslog);
        }
        return object;
    }

    private void setSyslog(MethodValue mthodRemark, String option, long startTimeMillis, long endTimeMillis, Syslog syslog) {
        String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTimeMillis);

        long ss = endTimeMillis - startTimeMillis;
        syslog.setIpAddress(getIp(getRequest()));
        syslog.setMethodName(mthodRemark.getName());
        syslog.setMethodRemark(mthodRemark.getRemark());
        syslog.setOptDate(startTime);
        syslog.setResponseTime(ss + "毫秒");
        syslog.setResult(mthodRemark.result() == true ? "成功" : "失败");
        User user = getLoginUser();
        if (user != null) {
            syslog.setLoginId(user.getId());
            syslog.setLoginName(user.getLogin_id());

        }
    }

    @After("methodCachePointcut()")
    public void afterAdvice(JoinPoint point) {

        // System.out.println("最后-----");

    }

    /**
     * 方法异常时调用
     *
     * @param ex
     */
    public void afterThrowing(Exception ex) {
        System.out.println("afterThrowing");
        System.out.println(ex);
    }

    /**
     * 获取方法中的中文备注
     *
     * @return
     * @throws Exception
     */
    private User getLoginUser() {

        Object user = getRequest().getSession().getAttribute("loginUser");
        if (user != null) {
            return (User) user;
        }
        return null;
    }

    public HttpServletRequest getRequest() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        return sra.getRequest();
    }

    public static MethodValue getMthodRemark(ProceedingJoinPoint joinPoint) throws Exception {
    	 /* String packages = joinPoint.getThis().getClass().getName();
           if (packages.indexOf("$$EnhancerBySpringCGLIB$$") > -1) { // 如果是CGLIB动态生成的类
               try {
                   packages = packages.substring(0, packages.indexOf("$$"));
   
               } catch (Exception ex) {
                   ex.printStackTrace();
               }
           }*/

        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();

        Class<?> targetClass = Class.forName(targetName);
        Method[] method = targetClass.getMethods();
        MethodValue methode = new MethodValue();
        for (Method m : method) {
            if (m.getName().equals(methodName)) {
                @SuppressWarnings("rawtypes")
                Class[] tmpCs = m.getParameterTypes();
                if (tmpCs.length == arguments.length) {
                    MethodLog methodCache = m.getAnnotation(MethodLog.class);
                    if (methodCache != null) {
                        methode.setRemark(methodCache.remark());
                        methode.setOption(methodCache.option());
                        methode.setName(targetName + "." + methodName);
                        methode.setArguments(arguments);
                    }
                    break;
                }
            }
        }
        return methode;
    }

    /**
     * 获取请求ip
     *
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !"".equals(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip.substring(0, index);
            } else {
                return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (ip != null && !"".equals(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        String remoteAddr = request.getRemoteAddr();
        return remoteAddr.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : remoteAddr;
    }

    public EasyUIJson queryLogListByPage(Syslog log, Integer page, Integer rows) {

        PageHelper.startPage(page, rows);

        List<Syslog> userList = logMapper.queryLogList(log);

        PageInfo<Syslog> pageInfo = new PageInfo<Syslog>(userList);

        EasyUIJson uiJson = new EasyUIJson();

        uiJson.setTotal(pageInfo.getTotal());

        uiJson.setRows(userList);
        return uiJson;

    }

    public void insertLog(Syslog syslog) {
        User user = getLoginUser();
        String flag = user.getFlag();
        if (OUT.equals(flag)) {
            syslog.setLoginId(user.getId());
            syslog.setLoginName(user.getLogin_id());
            long startTimeMillis = System.currentTimeMillis();
            String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTimeMillis);
            syslog.setIpAddress(getIp(getRequest()));
            syslog.setOptDate(startTime);
            logMapper.insertLog(syslog);
        }
    }

}
