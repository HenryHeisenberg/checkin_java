package cn.ssm.web;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.boot.system.ApplicationHome;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.ssm.base.BaseResult;
import cn.ssm.config.WebMvcConfig;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @PostMapping("/img")
    public Object upload(HttpServletRequest request, @RequestParam("img") MultipartFile[] imgs) {
        long currentTimeMillis = System.currentTimeMillis();
        String picPath="/OTA/";
        /************* 图片保存模块 ****************/
        HttpSession session = request.getSession(true);
        // 得到项目在服务器的真实根路径(绝对路径)，如：/tomcat/webapp/项目名/images
        // String path = session.getServletContext().getRealPath("images");
        /***** 获取jar所在目录 *****/
        UploadController.class.getClassLoader().getResource("");
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        path = new File(path).getParentFile().toString();
        try {
            path = URLDecoder.decode(path, "utf-8") + picPath;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return BaseResult.success("服务器繁忙");
        }
//		ApplicationHome home = new ApplicationHome(getClass());
//		File jarFile = home.getSource();
//		String path = jarFile.getParentFile().toString() + "/OTA/";
        /***** 获取jar所在目录 *****/
        // 得到数据库中保存的路径
        String contextPath = session.getServletContext().getContextPath() + picPath;
        String fileName = null;
        for (MultipartFile img : imgs) {
            // 如果没有文件上传，MultipartFile也不会为null，可以通过调用getSize()方法获取文件的大小来判断是否有上传文件
            if (img.getSize() > 0) {
                // 得到文件的原始名称，如：美女.png
                fileName = currentTimeMillis + "-" + img.getOriginalFilename();
                // 通过文件的原始名称，可以对上传文件类型做限制，如：只能上传jpg和png的图片文件
                if (fileName.lastIndexOf(".") != -1) {
                    String substring = fileName.substring(fileName.lastIndexOf(".") + 1);
                    if (!substring.equals("jpg") && !substring.equals("png") && !substring.equals("jpeg")) {
                        BaseResult.fail("只支持jpg,png,jpeg类型的图片");
                    }
                }
                File file = new File(path, fileName);
                try {
                    File existFile = new File(path);
                    if (!existFile.exists()) {
                        existFile.mkdirs();
                    }
                    img.transferTo(file);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                    return BaseResult.success("服务器繁忙");
                } catch (IOException e) {
                    e.printStackTrace();
                    return BaseResult.success("服务器繁忙");
                }
            }
            // 跳出循环，只处理一张图片
            break;
        }
        /************* 图片保存模块 ****************/
        return BaseResult.success(picPath+fileName);
    }

}
