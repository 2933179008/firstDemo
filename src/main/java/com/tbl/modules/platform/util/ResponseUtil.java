package com.tbl.modules.platform.util;

import com.tbl.common.utils.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseUtil {

    /**
     * 输出一段JS代码
     *
     * @param function JS代码，不需要拼上&lt;script type="text/javascript"&gt;和&lt;/script&gt;
     */
    public static void printJS(HttpServletResponse response, String function)
    {
        printObject(response, "<script type=\"text/javascript\">" + function + "</script>");
    }

    /**
     * 将内容输出到response
     *
     * @param obj 待输出的对象，ContentType为text/html;charset=UTF-8
     */
    public static void printObject(HttpServletResponse response, Object obj)
    {
        printObject(response, obj, "html");
    }

    /**
     * 将内容输出到response
     *
     * @param obj 待输出的对象
     * @param contenttype 内容类型，用于设置response的ContentType：
     *            <ul>
     *            <li>html ---> html类型类容(默认)</li>
     *            <li>xml ---> xml类型内容</li>
     *            <ul>
     */
    public static void printObject(HttpServletResponse response, Object obj, String contenttype)
    {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        if (!StringUtils.isEmptyString(contenttype) && "xml".equals(contenttype.toLowerCase()))
        {
            response.setContentType("text/xml;charset=UTF-8");
        }
        else
        {
            response.setContentType("text/html;charset=UTF-8");
        }

        PrintWriter out = null;
        try
        {
            out = response.getWriter();
            out.print(obj);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (out != null)
            {
                out.flush();
                out.close();
            }
        }
    }
}
