package com.qianwang.web.controller;

import com.qianwang.util.security.CaptchaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

@Controller
@RequestMapping("/captcha/**")
public class CaptchaController {
    /**
     *
     */
    private static final Logger LOG = LoggerFactory.getLogger(CaptchaController.class);

    @RequestMapping(value = "/generate", method = {RequestMethod.GET})
    public void generate(HttpServletRequest request, HttpServletResponse response) {
        Map<String, BufferedImage> validMap = CaptchaUtil.generate();
        for (Entry<String, BufferedImage> entry : validMap.entrySet()) {
            request.getSession().setAttribute(CaptchaUtil.VALID_CODE_MARK, entry.getKey());
            // 禁止图像缓存
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "No-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");
            // 创建二进制输出流并输出
            ServletOutputStream out = null;
            try {
                out = response.getOutputStream();
                ImageIO.write(entry.getValue(), "jpeg", out);
                out.flush();
            } catch (IOException e) {
                LOG.error("error write stream in CaptchaController->create:" + e.getMessage());
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        LOG.error("error close stream in CaptchaController->create:" + e.getMessage());
                    }
                }
            }
        }
    }
}
