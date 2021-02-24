package top.gerritchang.tools.controller;

import top.gerritchang.tools.utils.HttpUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
public class DownloadController {

    private final String HTML = "";

    @RequestMapping("/downloadDemo")
    public void downloadDemo(HttpServletResponse response){
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=Demo.html");
            OutputStream out = response.getOutputStream();
            out.write(HTML.getBytes());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("download")
    public void downloadFileFromMongoDB(HttpServletRequest request, HttpServletResponse response){
        String userAgent = request.getHeader("USER-AGENT");
        String fileName = "";
        try {
            fileName = URLDecoder.decode(request.getParameter("fileName"),"utf-8");
            if (StringUtils.contains(userAgent, "MSIE")
                    || StringUtils.contains(userAgent, "Trident")
                    || StringUtils.contains(userAgent, "Edge")) {//IE浏览器
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else if (StringUtils.contains(userAgent, "Mozilla")) {//google,火狐浏览器
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } else {
                fileName = URLEncoder.encode(fileName, "UTF-8");//其他浏览器
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Map<String, String[]> paramsTemp = request.getParameterMap();
        Map params = new HashMap();
        Iterator<Map.Entry<String, String[]>> iterator = paramsTemp.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String[]> entry = iterator.next();
            params.put(entry.getKey(),entry.getValue()[0]);
        }
        String uri = request.getParameter("uri");
        params.remove("uri");
        params.remove("fileName");
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
        OutputStream os = null;
        String urlPath = HttpUtils.getGETURL(uri,params);
        try {
            URL url = new URL(urlPath);
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.connect();
            BufferedInputStream bin = new BufferedInputStream(httpURLConnection.getInputStream());
            os = response.getOutputStream();
            byte[] b = new byte[2048];
            int length;
            while ((length = bin.read(b)) > 0) {
                os.write(b, 0, length);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
