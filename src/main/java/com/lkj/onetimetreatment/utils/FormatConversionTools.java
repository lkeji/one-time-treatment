package com.lkj.onetimetreatment.utils;

import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;

/**
 * @author: likj
 * @create: 2020-05-14 09:51
 * @description: 格式转换工具类
 * @program: one-time-treatment
 */
public class FormatConversionTools {

    /**
     * @param td
     * @return java.lang.String
     * @author likj
     * @date 2020/5/14
     * word中表格里面的数据
     */
    public static String formatText(TableCell td) {
        String string = "";
        for (int k = 0; k < td.numParagraphs(); k++) {
            Paragraph para = td.getParagraph(k);
            string = para.text().replace("", "");
        }
        return string;
    }

    /**
     * 功能描述: MultipartFile TO File
     * @param
     * @return
     * @author likj
     * @date 2020/5/14 11:13
     */
    public static File multipartFileToFile(MultipartFile multipartFile ) {
        int n;
        File f = null;
        // 输出文件的新name 就是指上传后的文件名称
        System.out.println("getName:"+multipartFile.getName());
        // 输出源文件名称 就是指上传前的文件名称
        System.out.println("Oriname:"+multipartFile.getOriginalFilename());
        // 创建文件
        f = new File(multipartFile.getOriginalFilename());
        try (InputStream in  = multipartFile.getInputStream(); OutputStream os = new FileOutputStream(f)){
            // 得到文件流。以文件流的方式输出到新文件
            // 可以使用byte[] ss = multipartFile.getBytes();代替while
            byte[] buffer = new byte[4096];
            while ((n = in.read(buffer,0,4096)) != -1){
                os.write(buffer,0,n);
            }
            // 读取文件第一行
            BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
            System.out.println(bufferedReader.readLine());
            // 输出路径
            bufferedReader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        // 输出file的URL
        try {
            System.out.println(f.toURI().toURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        // 输出文件的绝对路径
        System.out.println(f.getAbsolutePath());
        // 操作完上的文件 需要删除在根目录下生成的文件
        File file = new File(f.toURI());
        if (file.delete()){
            System.out.println("删除成功");
        }else {
            System.out.println("删除失败");

        }
        return file;
    }




}
