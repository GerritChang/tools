package top.gerritchang.tools.qrimage.controller;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URLDecoder;

@Controller
public class GenerateQrController {

    private final String QRIMAGE = "qrimage";

    @RequestMapping("/qrImage")
    public String getQrImage(){
        return QRIMAGE;
    }

    @RequestMapping("/createQRcode")
    public void createQRcode(HttpServletRequest request, HttpServletResponse response) {
        try {
            String content = request.getParameter("content");
            content = URLDecoder.decode(content,"utf-8");
            content = new String(content.getBytes("GB2312"),"8859_1");
            ByteArrayOutputStream out = QRCode.from(content).to(ImageType.PNG).stream();
            response.setContentType("image/png");
            response.setContentLength(out.size());
            OutputStream os = response.getOutputStream();
            os.write(out.toByteArray());
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
